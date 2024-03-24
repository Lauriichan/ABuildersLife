package me.lauriichan.minecraft.fabric.mixin.common.itemalchemy;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lauriichan.minecraft.fabric.common.itemalchemy.EMCCreativeTab;
import me.lauriichan.minecraft.fabric.common.itemalchemy.ITagKey;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.tag.TagKey;
import net.pitan76.itemalchemy.EMCManager;

@Mixin(EMCManager.class)
public abstract class EMCManagerMixin {

    @Shadow(remap = false)
    private static Map<String, Long> map;

    @Shadow
    private static void add(Item item, long emc) {}

    @Shadow
    private static String itemToId(Item item) {
        return null;
    }

    @Overwrite
    public static long get(Item item) {
        return map.getOrDefault(itemToId(item), 0L);
    }

    @Overwrite
    public static long get(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return 0L;
        }
        return map.getOrDefault(itemToId(itemStack.getItem()), 0L) * itemStack.getCount();
    }

    @Overwrite(remap = false)
    public static void set(String item, long emc) {
        map.put(item, emc);
    }

    @Overwrite(remap = false)
    public static void add2(TagKey<Item> tagKey, long emc) {
        Item item;
        for (RegistryEntry<Item> holder : ((ITagKey) tagKey).entries()) {
            item = holder.value();
            if (item == null) {
                continue;
            }
            add(item, emc);
        }
    }
    
    @Inject(method = "init(Lnet/minecraft/server/MinecraftServer;)V", at = @At("TAIL"), remap = false)
    private static void init(MinecraftServer server, CallbackInfo info) {
        EMCCreativeTab.TAB.refresh();
    }

    @Overwrite
    public static void setEmcFromRecipes(World world) {
        // Do nothing
    }

    @Overwrite
    public static boolean addEmcFromRecipe(ItemStack outStack, Recipe<?> recipe, List<Recipe<?>> unsetRecipes, boolean last) {
        Item item;
        if (outStack == null || outStack.isEmpty() || EMCManager.contains(item = outStack.getItem())) {
            return false;
        }
        long totalEmc = 0;
        for (Ingredient ingredient : recipe.getIngredients()) {
            ItemStack[] matching = ingredient.getMatchingStacks();
            long itemEmc = 0;
            if (matching.length > 1) {
                itemEmc = Long.MAX_VALUE;
                for (int i = 0; i < matching.length; i++) {
                    long emc = getEmc(matching[i]);
                    if (emc <= 0 || emc > itemEmc) {
                        continue;
                    }
                    itemEmc = emc;
                }
                if (itemEmc == Long.MAX_VALUE) {
                    if (!last && !unsetRecipes.contains(recipe)) {
                        unsetRecipes.add(recipe);
                    }
                    return false;
                }
            } else if (matching.length == 1) {
                itemEmc = getEmc(matching[0]);
            }
            if (itemEmc <= 0) {
                if (!last && !unsetRecipes.contains(recipe)) {
                    unsetRecipes.add(recipe);
                }
                return false;
            }
            totalEmc += itemEmc;
        }
        if (!last && outStack.getCount() > totalEmc) {
            if (!unsetRecipes.contains(recipe)) {
                unsetRecipes.add(recipe);
            }
            return false;
        }
        EMCManager.set(item, Math.floorDiv(totalEmc, outStack.getCount()));
        return true;
    }

    private static long getEmc(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return 0;
        }
        return get(itemStack.getItem());
    }

}
