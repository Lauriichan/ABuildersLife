package me.lauriichan.minecraft.fabric.common.itemalchemy.emc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.util.RecipeUtil;

public final class EMCHelper {

    public static void remap(EMCManager manager, World world) {
        remap(manager, world, 0);
    }

    public static void remap(EMCManager manager, World world, int depth) {
        depth = Math.max(depth + 1, 1);
        ObjectArraySet<Recipe<?>> unmapped = new ObjectArraySet<>();
        ObjectArrayList<Recipe<?>> list = new ObjectArrayList<>(world.getRecipeManager().values());
        for (int i = 0; i < depth; i++) {
            boolean last = i + 1 == depth;
            for (Recipe<?> recipe : list) {
                ItemStack output = getOutput(recipe, world);
                if (output == null) {
                    continue;
                }
                remapRecipeEmc(manager, output, recipe, unmapped, last);
            }
            list.clear();
            if (!last) {
                list.addAll(unmapped);
            }
            unmapped.clear();
        }
    }

    private static void remapRecipeEmc(EMCManager manager, ItemStack output, Recipe<?> recipe, ObjectArraySet<Recipe<?>> unmapped,
        boolean last) {
        Item item;
        if (output == null || output.isEmpty() || manager.has(item = output.getItem())) {
            return;
        }
        long totalEmc = 0;
        for (Ingredient ingredient : recipe.getIngredients()) {
            ItemStack[] matching = ingredient.getMatchingStacks();
            long itemEmc = 0;
            if (matching.length > 1) {
                itemEmc = Long.MAX_VALUE;
                for (int i = 0; i < matching.length; i++) {
                    long emc = manager.get(matching[i]);
                    if (emc <= 0 || emc > itemEmc) {
                        continue;
                    }
                    itemEmc = emc;
                }
                if (itemEmc == Long.MAX_VALUE) {
                    if (!last) {
                        unmapped.add(recipe);
                    }
                    return;
                }
            } else if (matching.length == 1) {
                itemEmc = manager.get(matching[0]);
            }
            if (itemEmc <= 0) {
                if (!last) {
                    unmapped.add(recipe);
                }
                return;
            }
            totalEmc += itemEmc;
        }
        if (!last && output.getCount() > totalEmc) {
            unmapped.add(recipe);
            return;
        }
        manager.set(item, Math.floorDiv(totalEmc, output.getCount()), false);
    }

    private static ItemStack getOutput(Recipe<?> recipe, World world) {
        try {
            return RecipeUtil.getOutput(recipe, world);
        } catch (NoClassDefFoundError | Exception ignore) {
            return null;
        }
    }

}
