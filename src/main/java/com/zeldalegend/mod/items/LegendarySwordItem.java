package com.zeldalegend.mod.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LegendarySwordItem extends Item {

    public LegendarySwordItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (attacker instanceof Player player) {
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 1));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
            if (player.getHealth() < player.getMaxHealth() * 0.25f) {
                player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 100, 1));
                player.addEffect(new MobEffectInstance(MobEffects.SPEED, 100, 0));
            }
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getHealth() >= player.getMaxHealth() && !level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            Vec3 look = player.getLookAngle();
            Vec3 pos = player.position().add(0, player.getEyeHeight(), 0);

            for (int i = 0; i < 3; i++) {
                double spread = (i - 1) * 0.15;
                Vec3 dir = new Vec3(
                    look.x + spread * look.z,
                    look.y,
                    look.z - spread * look.x
                ).normalize().scale(3.0);

                LargeFireball fireball = new LargeFireball(serverLevel, player, dir, 1);
                fireball.setPos(pos.x, pos.y, pos.z);
                serverLevel.addFreshEntity(fireball);
            }

            level.playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.5f, 1.8f);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§6✦ L'Épée qui repousse le Mal ✦"));
        tooltip.add(Component.literal("§7Forgée par les Trois Déesses"));
        tooltip.add(Component.literal("§b→ Ralentit et affaiblit les ennemis"));
        tooltip.add(Component.literal("§c→ Pouvoir décuplé sous les 25% de vie"));
        tooltip.add(Component.literal("§e→ Clic droit à cœur plein : tir de rayons"));
    }
}