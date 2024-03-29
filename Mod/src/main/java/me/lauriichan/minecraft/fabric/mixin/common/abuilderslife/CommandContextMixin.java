package me.lauriichan.minecraft.fabric.mixin.common.abuilderslife;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;

import me.lauriichan.minecraft.fabric.common.abuilderslife.command.ICommandContext;

@Mixin(CommandContext.class)
public abstract class CommandContextMixin<S> implements ICommandContext<S> {
    
    @Shadow
    @Final
    private static Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;

    @Shadow
    @Final
    private Map<String, ParsedArgument<S, ?>> arguments;
    
    @Shadow
    public abstract S getSource();

    @Override
    public S source() {
        return getSource();
    }
    
    @Override
    public <V> V get(String name, Class<V> type) {
        final ParsedArgument<S, ?> argument = arguments.get(name);
        if (argument == null) {
            return null;
        }
        final Object result = argument.getResult();
        if (PRIMITIVE_TO_WRAPPER.getOrDefault(type, type).isAssignableFrom(result.getClass())) {
            return type.cast(result);
        }
        return null;
    }
    
    @Override
    public boolean has(String name) {
        return arguments.containsKey(name);
    }
    
    @Override
    public boolean has(String name, Class<?> type) {
        final ParsedArgument<S, ?> argument = arguments.get(name);
        if (argument == null) {
            return false;
        }
        return PRIMITIVE_TO_WRAPPER.getOrDefault(type, type).isAssignableFrom(argument.getResult().getClass());
    }
    
}
