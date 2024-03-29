package me.lauriichan.minecraft.fabric.mixin.common.itemalchemy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import me.lauriichan.minecraft.fabric.common.itemalchemy.ITagKey;
import me.lauriichan.minecraft.fabric.common.itemalchemy.emc.EMCManager;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.pitan76.itemalchemy.emcs.EMCDef;

@Mixin(EMCDef.class)
public abstract class EMCDefMixin {
    
    @Overwrite
    public static void add(Item item, long emc) {
        EMCManager.DEFAULT.add(item, emc);
    }

    @Overwrite(remap = false)
    public static void add(String id, long emc) {
        EMCManager.DEFAULT.add(Identifier.tryParse(id), emc);
    }

    @Overwrite
    public static void addByTag(Identifier identifier, long emc) {
        EMCManager.DEFAULT.add(TagKey.of(RegistryKeys.ITEM, identifier), emc);
    }

    @Overwrite(remap = false)
    public static void addByTag(String id, long emc) {
        addByTag(new Identifier(id), emc);
    }

    @Overwrite(remap = false)
    public static void addByTag(net.pitan76.mcpitanlib.api.tag.TagKey<Item> tagKey, long emc) {
        EMCManager.DEFAULT.add(TagKey.of(RegistryKeys.ITEM, ((ITagKey) tagKey).getId()), emc);
    }
    
}
