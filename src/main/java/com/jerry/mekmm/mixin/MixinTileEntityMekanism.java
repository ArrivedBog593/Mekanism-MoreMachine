package com.jerry.mekmm.mixin;

import com.jerry.mekmm.common.item.ItemConnector;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TileEntityMekanism.class, remap = false)
public class MixinTileEntityMekanism {

    @Unique
    private ItemStack mekanismMoreMachine$stack;

    @Inject(method = "openGui", at = @At(value = "INVOKE", target = "Lmekanism/common/tile/base/TileEntityMekanism;isDirectional()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void getStack(Player player, CallbackInfoReturnable<InteractionResult> cir, ItemStack stack){
        mekanismMoreMachine$stack = stack;
    }


    @Inject(method = "openGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getMainHandItem()Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.BY), cancellable = true)
    public void mixinOpenGui(Player player, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemConnector) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }
}
