package me.lauriichan.minecraft.fabric.common.abuilderslife.command;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.RootCommandNode;

import me.lauriichan.minecraft.fabric.ABuildersLife;
import me.lauriichan.minecraft.fabric.common.abuilderslife.StringUtil;
import me.lauriichan.minecraft.fabric.common.abuilderslife.message.ComponentParser;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public abstract class Command {

    public static final String ARROW_SYMBOL = "»";

    private final String prefix;

    public Command(String prefix) {
        this.prefix = prefix == null ? "" : prefix.replace(">>", ARROW_SYMBOL);
    }

    protected void modify(RootCommandNode<ServerCommandSource> rootNode, ICommandNode node) {}

    protected abstract LiteralArgumentBuilder<ServerCommandSource> build();
    
    public static CompletableFuture<Void> queue(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, ABuildersLife.SERVICE);
    }
    
    public static <V> CompletableFuture<V> queue(Supplier<V> supplier) {
        return CompletableFuture.supplyAsync(supplier, ABuildersLife.SERVICE);
    }
    
    public static com.mojang.brigadier.Command<ServerCommandSource> cmd(Runnable runnable) {
        return ctx -> {
            runnable.run();
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        };
    }

    @SuppressWarnings("unchecked")
    public static com.mojang.brigadier.Command<ServerCommandSource> cmd(Consumer<ICommandContext<ServerCommandSource>> consumer) {
        return ctx -> {
            consumer.accept((ICommandContext<ServerCommandSource>) ctx);
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        };
    }

    public static LiteralArgumentBuilder<ServerCommandSource> literal(String literal) {
        return literal(literal, 4);
    }

    public static LiteralArgumentBuilder<ServerCommandSource> literal(String literal, int permissionLevel) {
        LiteralArgumentBuilder<ServerCommandSource> builder = LiteralArgumentBuilder.literal(literal);
        builder.requires(source -> source.hasPermissionLevel(permissionLevel));
        return builder;
    }

    public static LiteralArgumentBuilder<ServerCommandSource> literal(String literal, Predicate<ServerCommandSource> requirement) {
        LiteralArgumentBuilder<ServerCommandSource> builder = LiteralArgumentBuilder.literal(literal);
        builder.requires(requirement);
        return builder;
    }

    public static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> type) {
        return argument(name, type, 4);
    }

    public static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> type, int permissionLevel) {
        RequiredArgumentBuilder<ServerCommandSource, T> argument = RequiredArgumentBuilder.argument(name, type);
        argument.requires(source -> source.hasPermissionLevel(permissionLevel));
        return argument;
    }

    public static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> type,
        Predicate<ServerCommandSource> requirement) {
        RequiredArgumentBuilder<ServerCommandSource, T> argument = RequiredArgumentBuilder.argument(name, type);
        argument.requires(requirement);
        return argument;
    }

    public static UUID uuid(ICommandContext<ServerCommandSource> context) {
        ServerPlayerEntity entity = context.source().getPlayer();
        return entity == null ? null : entity.getUuid();
    }

    public void msg(ICommandContext<ServerCommandSource> context, Text message) {
        context.source().sendMessage(message);
    }

    public void msg(ICommandContext<ServerCommandSource> context, String message) {
        msg(context, true, message);
    }

    public void msg(ICommandContext<ServerCommandSource> context, Formatting defaultColor, String message) {
        msg(context, true, defaultColor, message);
    }

    public void msg(ICommandContext<ServerCommandSource> context, TextColor defaultColor, String message) {
        msg(context, true, defaultColor, message);
    }

    public void msg(ICommandContext<ServerCommandSource> context, String message, Object... arguments) {
        msg(context, true, message, arguments);
    }

    public void msg(ICommandContext<ServerCommandSource> context, Formatting defaultColor, String message, Object... arguments) {
        msg(context, true, defaultColor, message, arguments);
    }

    public void msg(ICommandContext<ServerCommandSource> context, TextColor defaultColor, String message, Object... arguments) {
        msg(context, true, defaultColor, message, arguments);
    }

    public void msg(ICommandContext<ServerCommandSource> context, boolean prefix, String message) {
        msg(context, ComponentParser.parse(prefix ? prefix(message) : message));
    }

    public void msg(ICommandContext<ServerCommandSource> context, boolean prefix, Formatting defaultColor, String message) {
        msg(context, ComponentParser.parse(prefix ? prefix(message) : message, defaultColor));
    }

    public void msg(ICommandContext<ServerCommandSource> context, boolean prefix, TextColor defaultColor, String message) {
        msg(context, ComponentParser.parse(prefix ? prefix(message) : message, defaultColor));
    }

    public void msg(ICommandContext<ServerCommandSource> context, boolean prefix, String message, Object... arguments) {
        msg(context, prefix, StringUtil.format(message, arguments));
    }

    public void msg(ICommandContext<ServerCommandSource> context, boolean prefix, Formatting defaultColor, String message,
        Object... arguments) {
        msg(context, prefix, defaultColor, StringUtil.format(message, arguments));
    }

    public void msg(ICommandContext<ServerCommandSource> context, boolean prefix, TextColor defaultColor, String message,
        Object... arguments) {
        msg(context, prefix, defaultColor, StringUtil.format(message, arguments));
    }

    public String prefix(String message) {
        return prefix + message;
    }

}
