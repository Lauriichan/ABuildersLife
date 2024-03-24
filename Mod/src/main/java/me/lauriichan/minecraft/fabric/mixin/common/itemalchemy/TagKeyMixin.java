package me.lauriichan.minecraft.fabric.mixin.common.itemalchemy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.lauriichan.minecraft.fabric.common.itemalchemy.ITagKey;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.pitan76.mcpitanlib.api.tag.TagKey;

@Mixin(TagKey.class)
public abstract class TagKeyMixin implements ITagKey {

    @SuppressWarnings("rawtypes")
    @Shadow
    private net.minecraft.registry.tag.TagKey tagKey;

    @Override
    public Iterable<RegistryEntry<Item>> entries() {
        return Registries.ITEM.iterateEntries(net.minecraft.registry.tag.TagKey.of(RegistryKeys.ITEM, tagKey.id()));
    }

}
