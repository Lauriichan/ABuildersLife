package me.lauriichan.minecraft.fabric.common.itemalchemy.command;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import me.lauriichan.minecraft.fabric.common.itemalchemy.emc.EMCManager;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.command.CommandSource;

public final class EMCManagerArgumentType implements ArgumentType<EMCManager> {

    public static final EMCManagerArgumentType EMC_MANAGER = new EMCManagerArgumentType();

    public static <S> ModContainer getMod(CommandContext<S> context, String name) {
        return context.getArgument(name, ModContainer.class);
    }
    
    private EMCManagerArgumentType() {}

    @Override
    public EMCManager parse(StringReader reader) throws CommandSyntaxException {
        return EMCManager.get(reader.readString());
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(EMCManager.names(), builder);
    }

}
