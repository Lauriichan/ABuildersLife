package me.lauriichan.minecraft.fabric.common.itemalchemy;

import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

public interface ITagKey {

    Iterable<RegistryEntry<Item>> entries();
    
}
