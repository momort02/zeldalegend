package com.zeldalegend.mod.items;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    private static final int COOLDOWN_TICKS = 15 * 20; // 15 secondes
    private static final double RAY_RANGE    = 64.0;
    private static final float  RAY_DAMAGE   = 8.0f;
    private static final int    FIRE_TICKS   = 5 * 20; // 5 secondes de feu

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) {
            return InteractionResult.PASS;
        }

        if (player.getHealth() >= player.getMaxHealth() && !level.isClientSide) {
            Vec3 start = player.getEyePosition();
            Vec3 look  = player.getLookAngle();
            Vec3 end   = start.add(look.scale(RAY_RANGE));

            // Recherche de toutes les entités dans le couloir du rayon
            AABB searchBox = player.getBoundingBox().expandTowards(look.scale(RAY_RANGE)).inflate(1.0);
            Optional<EntityHitResult> hit = level.getEntities(player, searchBox)
                .stream()
                .filter(e -> e instanceof LivingEntity && e != player)
                .map(e -> {
                    AABB box = e.getBoundingBox().inflate(0.3);
                    return box.clip(start, end).map(pos -> new EntityHitResult(e, pos)).orElse(null);
                })
                .filter(java.util.Objects::nonNull)
                .min(Comparator.comparingDouble(r -> r.getLocation().distanceToSqr(start)));

            if (hit.isPresent()) {
                Entity target = hit.get().getEntity();
                target.hurt(level.damageSources().playerAttack(player), RAY_DAMAGE);
                target.setRemainingFireTicks(FIRE_TICKS);

                // Particules de flammes le long du rayon côté serveur
                Vec3 hitPos = hit.get().getLocation();
                spawnRayParticles(level, start, hitPos);
            } else {
                // Rayon dans le vide : particules sur toute la longueur
                spawnRayParticles(level, start, end);
            }

            level.playSound(null, player.blockPosition(),
                SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0f, 1.6f);
            player.getCooldowns().addCooldown(player.getItemInHand(hand), COOLDOWN_TICKS);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    /** Spawn des particules de flamme le long du trajet du rayon (serveur uniquement). */
    private void spawnRayParticles(Level level, Vec3 from, Vec3 to) {
        if (level.isClientSide) return;
        net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) level;
        Vec3 dir = to.subtract(from);
        double length = dir.length();
        Vec3 step = dir.normalize();
        for (double d = 0; d < length; d += 0.5) {
            Vec3 p = from.add(step.scale(d));
            serverLevel.sendParticles(
                net.minecraft.core.particles.ParticleTypes.FLAME,
                p.x, p.y, p.z,
                1, 0.05, 0.05, 0.05, 0.01
            );
        }
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§6✦ L'Épée qui repousse le Mal ✦"));
        tooltip.add(Component.literal("§7Forgée par les Trois Déesses"));
        tooltip.add(Component.literal("§b→ Ralentit et affaiblit les ennemis"));
        tooltip.add(Component.literal("§c→ Pouvoir décuplé sous les 25% de vie"));
        tooltip.add(Component.literal("§e→ Clic droit à cœur plein : rayon de feu (cd 15s)"));
    }
}