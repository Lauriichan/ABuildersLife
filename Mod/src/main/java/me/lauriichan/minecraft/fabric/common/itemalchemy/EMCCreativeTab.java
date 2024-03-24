package me.lauriichan.minecraft.fabric.common.itemalchemy;

import me.lauriichan.minecraft.fabric.common.abuilderslife.CreativeTab;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.pitan76.itemalchemy.EMCManager;

public class EMCCreativeTab {

    public static final EMCCreativeTab TAB = new EMCCreativeTab();

    private final CreativeTab tab = new CreativeTab(new Identifier("itemalchemy", "emc_tab"), "EMC Tab");

    public CreativeTab getTab() {
        return tab;
    }

    public void refresh() {
        tab.clear();
        EMCManager.getMap().keySet().stream().map(str -> Registries.ITEM.get(Identifier.tryParse(str))).forEach(item -> tab.add(item));
        tab.refresh();
    }

}
