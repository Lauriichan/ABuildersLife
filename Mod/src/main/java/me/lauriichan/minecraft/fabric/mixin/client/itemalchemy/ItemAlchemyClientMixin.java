package me.lauriichan.minecraft.fabric.mixin.client.itemalchemy;

import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.lauriichan.minecraft.fabric.common.itemalchemy.emc.EMCManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.pitan76.itemalchemy.ItemAlchemyClient;

@Mixin(ItemAlchemyClient.class)
public abstract class ItemAlchemyClientMixin {
    
    @Overwrite
    private static List<Text> getEmcText(ItemStack itemStack) {
        long emc = EMCManager.DEFAULT.getSingle(itemStack);
        if (emc == 0) {
            return Collections.emptyList();
        }
        ObjectArrayList<Text> list = new ObjectArrayList<>();
        list.add(Text.literal("EMC: " + emc).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
        if (itemStack.getCount() > 1) {
            list.add(Text.literal("Stack EMC: " + (emc * itemStack.getCount())).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
        }
        return list;
    }

}
