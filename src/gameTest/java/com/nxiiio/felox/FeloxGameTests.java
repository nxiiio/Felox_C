package com.nxiiio.felox;

import com.nxiiio.felox.entity.FeloxEntity;
import com.nxiiio.felox.registry.ModEntities;
import com.nxiiio.felox.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(Felox.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FeloxGameTests {
    private FeloxGameTests() {}

    @GameTest(template = "platform", timeoutTicks = 40)
    public static void registeredEntitySurvivesSaveLoad(GameTestHelper helper) {
        preparePlatform(helper);
        FeloxEntity felox = spawnFelox(helper);
        helper.assertTrue(felox.getMaxHealth() == 40.0F, "Felox health attributes were not registered");
        helper.assertTrue(felox.getAttribute(Attributes.FLYING_SPEED) != null, "Missing flight speed attribute");
        helper.assertTrue(felox.getAttribute(Attributes.ATTACK_DAMAGE) != null, "Missing future attack attribute");
        helper.assertTrue(felox.isPersistenceRequired(), "Development Felox should persist");
        helper.assertTrue(felox.getAnimatableInstanceCache() != null, "Missing GeckoLib instance cache");

        CompoundTag saved = new CompoundTag();
        helper.assertTrue(felox.save(saved), "Could not save Felox");
        felox.discard();
        var restored = EntityType.loadEntityRecursive(saved, helper.getLevel(), entity -> entity);
        helper.assertTrue(restored instanceof FeloxEntity, "Saved entity did not resolve to felox:felox");
        FeloxEntity loaded = (FeloxEntity) restored;
        helper.assertTrue(helper.getLevel().addFreshEntity(loaded), "Could not re-add restored Felox");
        helper.assertTrue(loaded.isPersistenceRequired(), "Persistence did not survive save/load");
        helper.runAfterDelay(5, () -> {
            helper.assertTrue(loaded.isAlive(), "Restored Felox failed to tick");
            helper.succeed();
        });
    }

    @GameTest(template = "platform", timeoutTicks = 40)
    public static void spawnEggCreatesFelox(GameTestHelper helper) {
        preparePlatform(helper);
        var player = helper.makeMockSurvivalPlayer();
        ItemStack egg = new ItemStack(ModItems.FELOX_SPAWN_EGG.get());
        player.setItemInHand(InteractionHand.MAIN_HAND, egg);
        BlockPos floor = helper.absolutePos(new BlockPos(3, 0, 3));
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(floor), Direction.UP, floor, false);
        var result = egg.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
        helper.assertTrue(result.consumesAction(), "Spawn egg interaction was not accepted");
        helper.assertTrue(egg.isEmpty(), "Survival spawn egg was not consumed");
        helper.assertEntityPresent(ModEntities.FELOX.get(), new BlockPos(3, 1, 3));
        helper.succeed();
    }

    @GameTest(template = "platform", timeoutTicks = 120)
    public static void passiveEvenAfterPlayerDamage(GameTestHelper helper) {
        preparePlatform(helper);
        FeloxEntity felox = spawnFelox(helper);
        // The vanilla connected mock has no Netty channel for Forge's login handshake.
        // A survival mock supplies a real player damage source without a fake network login.
        var player = helper.makeMockSurvivalPlayer();
        player.setPos(Vec3.atBottomCenterOf(helper.absolutePos(new BlockPos(3, 1, 2))));
        float playerHealth = player.getHealth();
        helper.runAtTickTime(20, () -> {
            helper.assertTrue(felox.hurt(helper.getLevel().damageSources().playerAttack(player), 1.0F),
                    "Felox must remain vulnerable: health=" + felox.getHealth()
                            + ", invulnerable=" + felox.isInvulnerable()
                            + ", cooldown=" + felox.invulnerableTime
                            + ", lastDamage=" + felox.getLastDamageSource()
                            + ", position=" + felox.position()
                            + ", eye=" + felox.getEyePosition()
                            + ", eyeBlock=" + helper.getLevel().getBlockState(BlockPos.containing(felox.getEyePosition()))
                            + ", feetBlock=" + helper.getLevel().getBlockState(felox.blockPosition()));
            helper.assertTrue(felox.getHealth() < felox.getMaxHealth(), "Damage did not reduce health");
        });
        // Check every tick so transient targeting cannot hide behind a final-state assertion.
        for (int tick = 1; tick <= 100; tick++) {
            helper.runAtTickTime(tick, () -> {
                helper.assertTrue(felox.isAlive(), "Felox died during the passive test");
                helper.assertTrue(felox.getTarget() == null, "Felox acquired an attack target");
                helper.assertTrue(player.getHealth() == playerHealth, "Felox damaged the player");
            });
        }
        helper.runAtTickTime(101, helper::succeed);
    }

    private static void preparePlatform(GameTestHelper helper) {
        // Structure files have a vertical placement offset. Build the arena in helper-relative
        // coordinates so neither a roof nor a misplaced floor can cause suffocation/iframes.
        for (BlockPos pos : BlockPos.betweenClosed(0, 0, 0, 6, 4, 6)) {
            helper.setBlock(pos, pos.getY() == 0 ? Blocks.STONE : Blocks.AIR);
        }
    }

    private static FeloxEntity spawnFelox(GameTestHelper helper) {
        // Use the normal spawn path: GameTestHelper.spawn() forces persistence itself,
        // which would mask a regression in our entity's persistence policy.
        FeloxEntity felox = ModEntities.FELOX.get().spawn(helper.getLevel(),
                helper.absolutePos(new BlockPos(3, 1, 3)), MobSpawnType.COMMAND);
        helper.assertTrue(felox != null, "Felox failed to spawn");
        helper.assertTrue(helper.getLevel().noCollision(felox), "Test arena intersects the Felox hitbox");
        return felox;
    }
}
