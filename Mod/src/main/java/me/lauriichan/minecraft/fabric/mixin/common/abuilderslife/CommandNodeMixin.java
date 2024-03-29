package me.lauriichan.minecraft.fabric.mixin.common.abuilderslife;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import me.lauriichan.minecraft.fabric.common.abuilderslife.command.ICommandNode;

@Mixin(CommandNode.class)
public abstract class CommandNodeMixin<S> implements ICommandNode {

    @Final
    @Shadow
    private Map<String, CommandNode<S>> children;

    @Final
    @Shadow
    private Map<String, LiteralCommandNode<S>> literals;

    @Final
    @Shadow
    private Map<String, ArgumentCommandNode<S, ?>> arguments;

    @Override
    public void remove(String command) {
        children.remove(command);
        literals.remove(command);
        arguments.remove(command);
    }

}
