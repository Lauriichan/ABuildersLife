package me.lauriichan.minecraft.fabric.common.itemalchemy;

import me.lauriichan.minecraft.fabric.common.abuilderslife.ModArgumentType;
import net.minecraft.entity.Entity;
import net.pitan76.mcpitanlib.api.command.argument.RequiredCommand;

public abstract class ModCommand extends RequiredCommand<Entity> {

    @Override
    public final ModArgumentType getArgumentType() {
        return ModArgumentType.INSTANCE;
    }

}
