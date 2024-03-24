package me.lauriichan.minecraft.fabric.common.abuilderslife;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.datafixers.util.Either;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.command.argument.ItemStringReader.ItemResult;
import net.minecraft.command.argument.ItemStringReader.TagResult;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

public final class ItemArrayArgumentType implements ArgumentType<Item[]> {

    public static <S> Item[] getItemArray(CommandContext<S> context, String name) {
        return context.getArgument(name, Item[].class);
    }
    
    private final RegistryWrapper<Item> registryWrapper;

    public ItemArrayArgumentType(CommandRegistryAccess commandRegistryAccess) {
        this.registryWrapper = commandRegistryAccess.createWrapper(RegistryKeys.ITEM);
    }

    @Override
    public Item[] parse(StringReader reader) throws CommandSyntaxException {
        Either<ItemResult, TagResult> either = ItemStringReader.itemOrTag(registryWrapper, reader);
        if (either.left().isPresent()) {
            return new Item[] {
                either.left().get().item().value()
            };
        }
        return either.right().get().tag().stream().map(RegistryEntry::value).toArray(Item[]::new);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return ItemStringReader.getSuggestions(this.registryWrapper, builder, true);
    }

}
