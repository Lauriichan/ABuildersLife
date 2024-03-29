package me.lauriichan.minecraft.fabric.common.abuilderslife.command;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ItemStringReader;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;

public final class ItemTagArgumentType implements ArgumentType<RegistryEntryList<Item>> {
    
    private final RegistryWrapper<Item> registryWrapper;

    public ItemTagArgumentType(CommandRegistryAccess access) {
        this.registryWrapper = access.createWrapper(RegistryKeys.ITEM);
    }

    @Override
    public RegistryEntryList<Item> parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        try {
            ItemStringReader itemStringReader = new ItemStringReader(registryWrapper, reader, true);
            itemStringReader.readTag();
            return itemStringReader.result.right()
                .orElseThrow(() -> new IllegalStateException("Parser returned unexpected tag name"));
        } catch (CommandSyntaxException commandSyntaxException) {
            reader.setCursor(i);
            throw commandSyntaxException;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(this.registryWrapper.streamTagKeys().map(TagKey::id), builder, String.valueOf('#'));
    }

}
