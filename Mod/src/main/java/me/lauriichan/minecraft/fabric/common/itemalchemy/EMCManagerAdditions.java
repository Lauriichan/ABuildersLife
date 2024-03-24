package me.lauriichan.minecraft.fabric.common.itemalchemy;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.world.World;
import net.pitan76.itemalchemy.EMCManager;
import net.pitan76.mcpitanlib.api.util.RecipeUtil;

public final class EMCManagerAdditions {

    private EMCManagerAdditions() {
        throw new UnsupportedOperationException();
    }

    public static void remapEmcManager(World world) {
        ObjectArrayList<Recipe<?>> unused = new ObjectArrayList<>();
        ObjectArrayList<Recipe<?>> list = new ObjectArrayList<>(world.getRecipeManager().values());
        for (Recipe<?> recipe : list) {
            try {
                EMCManager.addEmcFromRecipe(RecipeUtil.getOutput(recipe, world), recipe, unused, false);
            } catch (NoClassDefFoundError | Exception ignore) {
            }
        }
        list.clear();
        for (Recipe<?> recipe : unused) {
            try {
                EMCManager.addEmcFromRecipe(RecipeUtil.getOutput(recipe, world), recipe, null, true);
            } catch (NoClassDefFoundError | Exception ignore) {
            }
        }
    }

    public static void setEMC(Item[] items, long emc) {
        if (emc == 0) {
            removeEMC(items);
            return;
        }
        for (Item item : items) {
            EMCManager.set(item, emc);
        }
    }

    public static void removeEMC(Item[] items) {
        for (Item item : items) {
            EMCManager.remove(item);
        }
    }

    public static void removeEMC(String item) {
        EMCManager.getMap().remove(item);
    }

}
