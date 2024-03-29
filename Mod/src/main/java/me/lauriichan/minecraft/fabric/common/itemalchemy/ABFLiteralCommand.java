package me.lauriichan.minecraft.fabric.common.itemalchemy;

import com.mojang.brigadier.arguments.LongArgumentType;

import me.lauriichan.minecraft.fabric.common.abuilderslife.ItemArrayArgumentType;
import me.lauriichan.minecraft.fabric.common.abuilderslife.ModArgumentType;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.pitan76.itemalchemy.EMCManager;
import net.pitan76.mcpitanlib.api.command.CommandSettings;
import net.pitan76.mcpitanlib.api.command.LiteralCommand;
import net.pitan76.mcpitanlib.api.event.ServerCommandEvent;
import net.pitan76.mcpitanlib.api.util.TextUtil;

public final class ABFLiteralCommand extends LiteralCommand {
    @Override
    public void execute(ServerCommandEvent event) {
        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Usage:"), false);
        event.sendSuccess(TextUtil.literal("/itemalchemy abf emc"), false);
        event.sendSuccess(TextUtil.literal("/itemalchemy abf def"), false);
    }

    @Override
    public void init(CommandSettings settings) {
        settings.permissionLevel(2);

        addArgumentCommand("emc", new LiteralCommand() {
            @Override
            public void execute(ServerCommandEvent event) {
                event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Usage (emc):"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy abf emc clear"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy abf emc save"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy abf emc update"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy abf emc remap"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy abf emc set [Item/Tag] [EMC]"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy abf emc remove [Item/Tag]"), false);
            }

            @Override
            public void init(CommandSettings settings) {
                addArgumentCommand("clear", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        EMCManager.getMap().clear();
                        EMCManager.config.configMap.clear();
                        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully cleared EMC"), false);
                    }
                });
                addArgumentCommand("save", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        EMCManager.config.configMap.clear();
                        EMCManager.config.configMap.putAll(EMCManager.getMap());
                        if (EMCManager.config.save(EMCManager.getConfigFile())) {
                            event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully saved EMC"), false);
                        } else {
                            event.sendFailure(TextUtil.literal("[ItemAlchemy (ABL)] Failed to save EMC"));
                        }
                    }
                });
                addArgumentCommand("remap", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Remapping emc..."), false);
                        EMCManagerAdditions.remapEmcManager(event.getWorld());
                        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully remapped emc."), false);
                    }
                });
                addArgumentCommand("update", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        World world = event.getWorld();
                        if (world.isClient()) {
                            event.sendFailure(TextUtil.literal("[ItemAlchemy (ABL)] This can only be done on a server"));
                            return;
                        }
                        EMCCreativeTab.TAB.refresh();
                        for (ServerPlayerEntity entity : ((ServerWorld) world).getPlayers()) {
                            EMCManager.syncS2C_emc_map(entity);
                        }
                        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully updated EMC for all players"), false);
                    }
                });
                addArgumentCommand("set", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        event.sendFailure(TextUtil.literal("[ItemAlchemy (ABL)] Example: /itemalchemy abf emc set [Item/Tag] [EMC]"));
                    }

                    @Override
                    public void init(CommandSettings settings) {
                        addArgumentCommand(new ItemArrayCommand() {
                            @Override
                            public void execute(ServerCommandEvent event) {
                                event.sendFailure(
                                    TextUtil.literal("[ItemAlchemy (ABL)] Example: /itemalchemy abf emc set [Item/Tag] [EMC]"));
                            }

                            @Override
                            public String getArgumentName() {
                                return "item";
                            }

                            @Override
                            public void init(CommandSettings settings) {
                                addArgumentCommand(new LongCommand() {
                                    @Override
                                    public void execute(ServerCommandEvent event) {
                                        Item[] items = ItemArrayArgumentType.getItemArray(event.getContext(), "item");
                                        long emc = LongArgumentType.getLong(event.getContext(), getArgumentName());
                                        EMCManagerAdditions.setEMC(items, emc);
                                        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully set EMC to " + emc), false);
                                    }

                                    @Override
                                    public String getArgumentName() {
                                        return "emc";
                                    }
                                });
                            }
                        });
                    }
                });
                addArgumentCommand("remove", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        event.sendFailure(TextUtil.literal("[ItemAlchemy (ABL)] Example: /itemalchemy abf emc remove [Item/Tag]"));
                    }

                    @Override
                    public void init(CommandSettings settings) {
                        addArgumentCommand(new ItemArrayCommand() {
                            @Override
                            public void execute(ServerCommandEvent event) {
                                Item[] items = ItemArrayArgumentType.getItemArray(event.getContext(), getArgumentName());
                                EMCManagerAdditions.removeEMC(items);
                                event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully removed EMC"), false);
                            }

                            @Override
                            public String getArgumentName() {
                                return "item";
                            }
                        });
                    }
                });
                addArgumentCommand("removemod", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        event.sendFailure(TextUtil.literal("[ItemAlchemy (ABL)] Example: /itemalchemy abf emc removemod [ModId]"));
                    }

                    @Override
                    public void init(CommandSettings settings) {
                        addArgumentCommand("mod", new ModCommand() {
                            @Override
                            public void execute(ServerCommandEvent event) {
                                ModContainer container = ModArgumentType.getMod(event.getContext(), getArgumentName());
                                Registries.ITEM.getIds().stream().filter(id -> container.getMetadata().getId().equals(id.getNamespace()))
                                    .map(Identifier::toString).forEach(EMCManagerAdditions::removeEMC);
                                event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully removed EMC"), false);
                            }

                            @Override
                            public String getArgumentName() {
                                return "mod";
                            }
                        });
                    }
                });
            }
        });
        addArgumentCommand("def", new LiteralCommand() {
            @Override
            public void execute(ServerCommandEvent event) {
                event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Usage (def):"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy def savefile"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy def loadfile"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy def savemanager"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy def loadmanager"), false);
                event.sendSuccess(TextUtil.literal("/itemalchemy def clear"), false);
            }

            public void init(CommandSettings settings) {
                addArgumentCommand("savefile", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        if (EMCDefinitionManager.saveToFile()) {
                            event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully saved EMC definitions"), false);
                        } else {
                            event.sendFailure(TextUtil.literal("[ItemAlchemy (ABL)] Failed to save EMC definitions"));
                        }
                    }
                });
                addArgumentCommand("loadfile", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        if (EMCDefinitionManager.loadFromFile()) {
                            event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully loaded EMC definitions"), false);
                        } else {
                            event.sendFailure(TextUtil.literal("[ItemAlchemy (ABL)] Failed to load EMC definitions"));
                        }
                    }
                });
                addArgumentCommand("savemanager", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        EMCDefinitionManager.saveFromManager();
                        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully save EMC Manager to EMC definitions"), false);
                    }
                });
                addArgumentCommand("loadmanager", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        EMCDefinitionManager.loadToManager();
                        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully loaded EMC definitions to EMC Manager"),
                            false);
                    }
                });
                addArgumentCommand("clear", new LiteralCommand() {
                    @Override
                    public void execute(ServerCommandEvent event) {
                        EMCDefinitionManager.clear();
                        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)] Successfully cleared EMC definitions"), false);
                    }
                });
            }
        });
    }
}
