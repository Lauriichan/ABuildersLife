package me.lauriichan.minecraft.fabric.common.itemalchemy.command;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static com.mojang.brigadier.arguments.BoolArgumentType.*;
import static me.lauriichan.minecraft.fabric.common.itemalchemy.command.EMCManagerArgumentType.*;

import java.util.UUID;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.RootCommandNode;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import me.lauriichan.minecraft.fabric.common.abuilderslife.command.Command;
import me.lauriichan.minecraft.fabric.common.abuilderslife.command.ICommandContext;
import me.lauriichan.minecraft.fabric.common.abuilderslife.command.ICommandNode;
import me.lauriichan.minecraft.fabric.common.itemalchemy.emc.EMCManager;
import net.minecraft.server.command.ServerCommandSource;

public final class ItemAlchemyCommand extends Command {

    private static final Object2ObjectArrayMap<UUID, EMCManager> managerMap = new Object2ObjectArrayMap<>();

    public static void reset() {
        managerMap.clear();
    }

    public ItemAlchemyCommand() {
        super("&#F6A04AItemAlchemy &8>> &7");
    }

    @Override
    protected void modify(RootCommandNode<ServerCommandSource> rootNode,
        ICommandNode node) {
        node.remove("itemalchemy");
    }

    @Override
    protected LiteralArgumentBuilder<ServerCommandSource> build() {
        return literal("itemalchemy")
            .then(literal("manager")
                .then(literal("create")
                    .then(argument("name", word())
                        .then(argument("load", bool())
                            .executes(cmd(this::managerCreate)))
                        .executes(cmd(this::managerCreate))))
                .then(literal("select")
                    .then(argument("manager", EMC_MANAGER)
                        .executes(cmd(this::managerSelect))))
                .then(literal("view").executes(cmd(this::managerView)))
                .then(literal("info").executes(cmd(this::managerInfo)))
                .then(literal("load")
                    .then(argument("manager", EMC_MANAGER)
                        .executes(cmd(this::managerLoad)))
                    .executes(cmd(this::managerLoad)))
                .then(literal("save")
                    .then(argument("manager", EMC_MANAGER)
                        .executes(cmd(this::managerSave)))
                    .executes(cmd(this::managerSave))))
            .then(literal("emc")
                .then(argument("manager", EMC_MANAGER)
                    .then(literal("set"))
                    .then(literal("remove"))
                    .then(literal("clear"))));
    }

    private void managerLoad(ICommandContext<ServerCommandSource> ctx) {
        EMCManager manager = ctx.get("manager", EMCManager.class);
        if (manager != null) {
            if (manager.load()) {
                msg(ctx, "Successfully loaded emc data for emc manager '{0}'.", manager.name());
                return;
            }
            msg(ctx, "Failed to load emc data for emc manager '{0}'.", manager.name());
            return;
        }
        msg(ctx, "Loading emc data for all managers.");
        queue(EMCManager::loadAll).thenRun(() -> msg(ctx, "Loading of emc data for all emc managers is complete."));
    }

    private void managerSave(ICommandContext<ServerCommandSource> ctx) {
        EMCManager manager = ctx.get("manager", EMCManager.class);
        if (manager != null) {
            if (manager.save()) {
                msg(ctx, "Successfully saved emc data for emc manager '{0}'.", manager.name());
                return;
            }
            msg(ctx, "Failed to save emc data for emc manager '{0}'.", manager.name());
            return;
        }
        msg(ctx, "Saving emc data for all managers.");
        queue(EMCManager::saveAll).thenRun(() -> msg(ctx, "Saving of emc data for all emc managers is complete."));
    }

    private void managerInfo(ICommandContext<ServerCommandSource> ctx) {
        EMCManager manager = managerMap.get(uuid(ctx));
        if (manager == null) {
            msg(ctx, "You have no emc manager selected.");
            return;
        }
        msg(ctx, "You currently have the '{0}' emc manager selected.", manager.name());
    }

    private void managerView(ICommandContext<ServerCommandSource> ctx) {

    }

    private void managerSelect(ICommandContext<ServerCommandSource> ctx) {

    }

    private void managerCreate(ICommandContext<ServerCommandSource> ctx) {
        String managerName = ctx.get("name", String.class);
        EMCManager manager = EMCManager.getOrCreate(managerName);
        managerMap.put(uuid(ctx), manager);
        msg(ctx, "Successfully created and selected emc manager '{0}'.", managerName);
        if (!ctx.get("load", Boolean.class, false)) {
            return;
        }
        if (manager.load()) {
            msg(ctx, "Successfully loaded emc data for emc manager '{0}'", managerName);
            return;
        }
        msg(ctx, "Failed to load emc data for emc manager '{0}'", managerName);
    }

}
