package me.lauriichan.minecraft.fabric.common.itemalchemy;

import me.lauriichan.minecraft.fabric.common.abuilderslife.ItemArrayArgumentType;
import net.minecraft.entity.Entity;
import net.pitan76.mcpitanlib.api.command.CommandRegistry;
import net.pitan76.mcpitanlib.api.command.argument.RequiredCommand;

public abstract class ItemArrayCommand extends RequiredCommand<Entity> {

    @SuppressWarnings("deprecation")
    @Override
    public final ItemArrayArgumentType getArgumentType() {
        return new ItemArrayArgumentType(CommandRegistry.latestCommandRegistryAccess);
    }

}
