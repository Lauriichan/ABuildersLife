package me.lauriichan.minecraft.fabric.common.abuilderslife.command;

import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import me.lauriichan.minecraft.fabric.common.itemalchemy.command.EMCManagerArgumentType;
import me.lauriichan.minecraft.fabric.common.itemalchemy.command.ItemAlchemyCommand;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public final class Commands {
    
    public static void registerArgumentTypes() {
        regDynamic("item", ItemArgumentType.class, ItemArgumentType::new);
        regDynamic("item_tag", ItemTagArgumentType.class, ItemTagArgumentType::new);
        regStatic("mod", ModArgumentType.class, ModArgumentType.MOD);
        regStatic("itemalchemy/emc_manager", EMCManagerArgumentType.class, EMCManagerArgumentType.EMC_MANAGER);
    }
    
    private static <A extends ArgumentType<?>> void regDynamic(String id, Class<A> type, Function<CommandRegistryAccess, A> func) {
        ArgumentTypeRegistry.registerArgumentType(new Identifier("abuilderslife", id), type, ConstantArgumentSerializer.of(func));
    }
    private static <A extends ArgumentType<?>> void regStatic(String id, Class<A> type, A instance) {
        ArgumentTypeRegistry.registerArgumentType(new Identifier("abuilderslife", id), type, ConstantArgumentSerializer.of(() -> instance));
    }

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment) {
        Commands commands = new Commands(dispatcher, access);
        commands.register(new ItemAlchemyCommand());
    }

    private final CommandDispatcher<ServerCommandSource> dispatcher;
    private final CommandRegistryAccess access;
    private final Reference2ObjectArrayMap<Class<? extends ArgumentType<?>>, ArgumentType<?>> argumentTypes = new Reference2ObjectArrayMap<>();

    private Commands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access) {
        this.dispatcher = dispatcher;
        this.access = access;
    }
    
    final void register(Command command) {
        command.modify(dispatcher.getRoot(), (ICommandNode) dispatcher.getRoot());
        dispatcher.register(command.build());
    }

    private <V extends ArgumentType<?>> V get(Class<V> type,
        Function<CommandRegistryAccess, V> func) {
        return type
            .cast(argumentTypes
                .computeIfAbsent(type, (i) -> func.apply(access)));
    }

    public ItemArgumentType item() {
        return get(ItemArgumentType.class, ItemArgumentType::new);
    }
    
    public ItemTagArgumentType tag() {
        return get(ItemTagArgumentType.class, ItemTagArgumentType::new);
    }

}
