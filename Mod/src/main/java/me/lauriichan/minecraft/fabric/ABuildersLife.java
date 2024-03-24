package me.lauriichan.minecraft.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.lauriichan.minecraft.fabric.common.abuilderslife.ItemArrayArgumentType;
import me.lauriichan.minecraft.fabric.common.abuilderslife.ModArgumentType;
import me.lauriichan.minecraft.fabric.common.itemalchemy.EMCCreativeTab;

public final class ABuildersLife implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("abuilderslife");

    @Override
    public void onInitialize() {
        ArgumentTypeRegistry.registerArgumentType(new Identifier("abuilderslife", "itemarray"), ItemArrayArgumentType.class,
            ConstantArgumentSerializer.of(ItemArrayArgumentType::new));
        ArgumentTypeRegistry.registerArgumentType(new Identifier("abuilderslife", "mod"), ModArgumentType.class,
            ConstantArgumentSerializer.of(() -> ModArgumentType.INSTANCE));

        EMCCreativeTab.TAB.getTab().register(Registries.ITEM_GROUP);
    }

}