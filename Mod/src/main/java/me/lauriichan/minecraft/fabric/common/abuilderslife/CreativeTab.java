package me.lauriichan.minecraft.fabric.common.abuilderslife;

import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.lauriichan.minecraft.fabric.ABuildersLife;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CreativeTab {

    private final ObjectArrayList<Item> itemDefs = new ObjectArrayList<>();

    private final RegistryKey<ItemGroup> key;
    private final Supplier<Item> iconSupplier;

    private final String name;

    public CreativeTab(Identifier id, String name) {
        this(id, name, null);
    }

    public CreativeTab(Identifier id, String name, Supplier<Item> iconSupplier) {
        this.key = RegistryKey.of(RegistryKeys.ITEM_GROUP, id);
        this.iconSupplier = iconSupplier;
        this.name = name;
    }

    public RegistryKey<ItemGroup> key() {
        return key;
    }

    public void register(Registry<ItemGroup> registry) {
        Registry.register(registry, key,
            FabricItemGroup.builder().displayName(Text.literal(name)).icon(this::createIcon).entries(this::buildDisplayItems).build());
    }
    
    public void refresh() {
        ItemGroup group = Registries.ITEM_GROUP.get(key);
        if (group == null || ItemGroups.displayContext == null) {
            return;
        }
        group.updateEntries(ItemGroups.displayContext);
    }

    public void clear() {
        itemDefs.clear();
    }

    public void add(Item item) {
        if (item == null) {
            return;
        }
        itemDefs.add(item);
    }

    private Item supplyIcon() {
        if (itemDefs.isEmpty()) {
            return null;
        }
        return itemDefs.get(0);
    }

    private ItemStack createIcon() {
        Item def = iconSupplier == null ? supplyIcon() : iconSupplier.get();
        if (def == null) {
            return Items.END_CRYSTAL.getDefaultStack();
        }
        return def.getDefaultStack();
    }

    private void buildDisplayItems(ItemGroup.DisplayContext parameters, ItemGroup.Entries output) {
        for (Item itemDef : itemDefs) {
            try {
                output.add(itemDef);
            } catch(IllegalArgumentException iae) {
                // Something is wrong here?
                ABuildersLife.LOGGER.debug("Failed to add item '" + Registries.ITEM.getId(itemDef) + "'", iae);
            }
        }
    }

}
