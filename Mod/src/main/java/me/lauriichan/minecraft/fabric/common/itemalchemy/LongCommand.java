package me.lauriichan.minecraft.fabric.common.itemalchemy;

import com.mojang.brigadier.arguments.LongArgumentType;

import net.minecraft.entity.Entity;
import net.pitan76.mcpitanlib.api.command.argument.RequiredCommand;

public abstract class LongCommand extends RequiredCommand<Entity> {

    @Override
    public final LongArgumentType getArgumentType() {
        return LongArgumentType.longArg(0);
    }

}
