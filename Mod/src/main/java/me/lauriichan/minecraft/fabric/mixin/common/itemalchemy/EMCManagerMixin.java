package me.lauriichan.minecraft.fabric.mixin.common.itemalchemy;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import me.lauriichan.minecraft.fabric.common.itemalchemy.ITagKey;
import me.lauriichan.minecraft.fabric.common.itemalchemy.emc.EMCDefaults;
import me.lauriichan.minecraft.fabric.common.itemalchemy.emc.EMCManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.pitan76.itemalchemy.ItemAlchemy;
import net.pitan76.itemalchemy.emcs.EMCDef;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.network.ServerNetworking;

@Mixin(net.pitan76.itemalchemy.EMCManager.class)
public abstract class EMCManagerMixin {
    
    @Overwrite
    public static void add(Item item, long emc) {
        EMCManager.DEFAULT.add(item, emc);
    }

    @Overwrite(remap = false)
    public static void add(String item, long emc) {
        EMCManager.DEFAULT.add(Identifier.tryParse(item), emc);
    }

    @Overwrite(remap = false)
    public static void add2(net.pitan76.mcpitanlib.api.tag.TagKey<Item> tagKey, long emc) {
        EMCManager.DEFAULT.add(TagKey.of(RegistryKeys.ITEM, ((ITagKey) tagKey).getId()), emc);
    }

    @Overwrite(remap = false)
    public static void addDef(EMCDef def) {
        EMCDefaults.DEFAULT.addDefinition(def);
    }
    
    @Overwrite
    public static boolean contains(Item item) {
        return EMCManager.DEFAULT.has(item);
    }
    
    @Overwrite(remap = false)
    public static boolean contains(String item) {
        return EMCManager.DEFAULT.has(Identifier.tryParse(item));
    }

    @Overwrite(remap = false)
    public static void defaultMap() {
        EMCDefaults.DEFAULT.applyToDefault();
    }
    
    @Overwrite
    public static long get(Item item) {
        return EMCManager.DEFAULT.get(item);
    }
    
    @Overwrite
    public static long get(ItemStack itemStack) {
        return EMCManager.DEFAULT.get(itemStack);
    }

    @Overwrite(remap = false)
    public static List<EMCDef> getDefs() {
        return EMCDefaults.DEFAULT.getDefinitions();
    }
    
    @Overwrite(remap = false)
    public static Map<String, Long> getMap() {
        return EMCManager.DEFAULT.toItemAlchemyCompatMap();
    }
    
    @Overwrite(remap = false)
    public static void init(MinecraftServer server) {
        if (server.getOverworld().isClient()) {
            return;
        }
        if (!EMCManager.DEFAULT.load()) {
            EMCDefaults.DEFAULT.applyToDefault();
        }
    }
    
    @Overwrite
    public static void loadDefaultEMCs(ResourceManager resourceManager) {
        EMCDefaults.DEFAULT.reload(resourceManager);
    }
    
    @Overwrite
    public static void remove(Item item) {
        EMCManager.DEFAULT.remove(item);
    }
    
    @Overwrite
    public static void set(Item item, long emc) {
        EMCManager.DEFAULT.set(item, emc, false);
    }

    @Overwrite(remap = false)
    public static void set(String item, long emc) {
        EMCManager.DEFAULT.set(Identifier.tryParse(item), emc, false);
    }
    
    @Overwrite(remap = false)
    public static void setMap(Map<String, Long> map) {
        EMCManager.DEFAULT.fromItemAlchemyCompatMap(map);
    }
    
    @Overwrite
    public static void syncS2C_emc_map(ServerPlayerEntity player) {
        if (player.networkHandler == null || EMCManager.DEFAULT.isEmpty()) {
            return;
        }
        PacketByteBuf buf = PacketByteUtil.create();
        PacketByteUtil.writeMap(buf, EMCManager.DEFAULT.toItemAlchemyCompatMap());
        ServerNetworking.send(player, ItemAlchemy.id("sync_emc_map"), buf);
    }
    
}
