package me.lauriichan.minecraft.fabric.mixin.common.itemalchemy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.lauriichan.minecraft.fabric.common.itemalchemy.ABFLiteralCommand;
import net.pitan76.itemalchemy.command.ItemAlchemyCommand;
import net.pitan76.mcpitanlib.api.command.LiteralCommand;
import net.pitan76.mcpitanlib.api.event.ServerCommandEvent;
import net.pitan76.mcpitanlib.api.util.TextUtil;

@Mixin(ItemAlchemyCommand.class)
public abstract class ItemAlchemyCommandMixin extends LiteralCommand {
    
    @Inject(method = "execute(Lnet/pitan76/mcpitanlib/api/event/ServerCommandEvent;)V", at = @At("TAIL"), remap = false)
    public void mixin_execute(ServerCommandEvent event, CallbackInfo info) {
        event.sendSuccess(TextUtil.literal("[ItemAlchemy (ABL)]:\n- /itemalchemy abf"), false);
    }

    @Inject(method = "init()V", at = @At("HEAD"), remap = false)
    public void mixin_init(CallbackInfo info) {
        addArgumentCommand("abf", new ABFLiteralCommand());
    }

}
