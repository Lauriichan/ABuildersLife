package me.lauriichan.minecraft.fabric.mixin.common.itemalchemy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.lauriichan.minecraft.fabric.common.itemalchemy.ITagKey;
import net.minecraft.util.Identifier;
import net.pitan76.mcpitanlib.api.tag.TagKey;

@Mixin(TagKey.class)
public abstract class TagKeyMixin implements ITagKey {

    @SuppressWarnings("rawtypes")
    @Shadow
    private net.minecraft.registry.tag.TagKey tagKey;

    @Override
    public Identifier getId() {
        return tagKey.id();
    }

}
