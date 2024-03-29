package me.lauriichan.minecraft.fabric.common.abuilderslife.command;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.command.CommandSource;

public final class ModArgumentType implements ArgumentType<ModContainer> {

    public static final ModArgumentType MOD = new ModArgumentType();

    public static <S> ModContainer getMod(CommandContext<S> context, String name) {
        return context.getArgument(name, ModContainer.class);
    }
    
    private ModArgumentType() {}

    @Override
    public ModContainer parse(StringReader reader) throws CommandSyntaxException {
        return FabricLoader.getInstance().getModContainer(reader.readString()).get();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(FabricLoader.getInstance().getAllMods().stream().map(container -> container.getMetadata().getId()), builder);
    }

}
