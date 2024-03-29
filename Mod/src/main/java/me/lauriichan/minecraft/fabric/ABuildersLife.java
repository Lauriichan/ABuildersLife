package me.lauriichan.minecraft.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.registry.Registries;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.lauriichan.minecraft.fabric.common.abuilderslife.command.Commands;
import me.lauriichan.minecraft.fabric.common.itemalchemy.EMCCreativeTab;

public final class ABuildersLife implements ModInitializer {

    public static final ExecutorService SERVICE = Executors.newFixedThreadPool(3);
    public static final Logger LOGGER = LoggerFactory.getLogger("abuilderslife");

    @Override
    public void onInitialize() {
        Commands.registerArgumentTypes();
        CommandRegistrationCallback.EVENT.register(Commands::registerCommands);

        EMCCreativeTab.TAB.getTab().register(Registries.ITEM_GROUP);
    }

}