package com.xir.NHUtilities.mixins.late.ExtraUtilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rwtema.extrautils.item.ItemHealingAxe;

@SuppressWarnings("UnusedMixin")
@Mixin(value = ItemHealingAxe.class, remap = false)
public class EnhanceExUHealingAxe_Mixin {

    @Inject(
        method = "onUpdate",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FoodStats;addStats(IF)V", shift = At.Shift.AFTER),
        require = 1,
        remap = true)
    private void nhu$enhanceHealingAxe(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4,
        boolean par5, CallbackInfo ci) {
        ((EntityPlayer) par3Entity).getFoodStats()
            .addStats(20, 5.0F);
    }

    @Inject(
        method = "hitEntity(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;)Z",
        at = @At("RETURN"),
        remap = true)
    private void nhu$healOnHit(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker,
        CallbackInfoReturnable<Boolean> cir) {
        // 只有玩家才触发生命转移效果
        if (!(attacker instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) attacker;

        // 基础转移生命值（2 心）
        final float baseTransfer = 2.0F;

        if (target.isEntityUndead()) {
            // 对亡灵造成 4 倍伤害，玩家承受 baseTransfer 点伤害
            player.attackEntityFrom(DamageSource.magic, baseTransfer);
            target.attackEntityFrom(DamageSource.causePlayerDamage(player), baseTransfer * 4.0F);
        } else {
            // 对活体生物：玩家承受 baseTransfer 点伤害，目标回复 baseTransfer*1.1 点生命
            player.attackEntityFrom(DamageSource.magic, baseTransfer);
            target.heal(baseTransfer * 4.0F);
        }
    }
}
