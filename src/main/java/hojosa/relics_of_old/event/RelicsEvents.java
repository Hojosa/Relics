package hojosa.relics_of_old.event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import be.florens.expandability.api.forge.LivingFluidCollisionEvent;
import hojosa.relics_of_old.Relics;
import hojosa.relics_of_old.common.entity.FallingStarEntity;
import hojosa.relics_of_old.common.entity.StarBeamEntity;
import hojosa.relics_of_old.common.entity.attacks.QuakeEntity;
import hojosa.relics_of_old.common.init.RelicsConfig;
import hojosa.relics_of_old.common.init.RelicsEffects;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.item.BombBagItem;
import hojosa.relics_of_old.common.item.EmptyMedallion;
import hojosa.relics_of_old.common.item.HeadbandOfValor;
import hojosa.relics_of_old.common.item.RelicsAmulet;
import hojosa.relics_of_old.common.item.WhirlwindBoots;
import hojosa.relics_of_old.common.item.entity.EmeraldShardItemEntity;
import hojosa.relics_of_old.common.item.entity.HeartItemEntity;
import hojosa.relics_of_old.common.player.StarFallChance;
import hojosa.relics_of_old.common.player.StarFallChanceProvider;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil;
import hojosa.relics_of_old.lib.RelicsUtil.ElementType;
import hojosa.relics_of_old.network.PhoenixParticlePacket;
import hojosa.relics_of_old.network.RelicsNetwork;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.EnderManAngerEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.patchouli.api.PatchouliAPI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class RelicsEvents {
	private static Random random = new Random();

	@SubscribeEvent
	public static void fluidWalker(LivingFluidCollisionEvent event) {
		if (event.getFluidState().is(FluidTags.WATER)) {
			if (RelicsItems.WATER_TABLET.get().isEquipped(event.getEntity())) {
				event.setResult(Event.Result.ALLOW);
			} else if (event.getEntity() instanceof Player player && player.isSprinting() && player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof WhirlwindBoots) {
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

	@SubscribeEvent
	public static void addVillagerTrades(VillagerTradesEvent event) {
		if (event.getType() == VillagerProfession.CLERIC) {
			Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
			ItemStack tablet = new ItemStack(RelicsItems.WATER_TABLET.get(), 1);
			int villagerLevel = 3;

			trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 64), tablet, 1, 8, 0));
		}
		if (event.getType() == VillagerProfession.LIBRARIAN) {
			Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
			ItemStack book = PatchouliAPI.get().getBookStack(RelicsUtil.modLoc("tome"));
			int villagerLevel = 2;

			trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 1), book, 12, 1, 0));
		}
	}

	@SubscribeEvent
	public static void addWandererTrades(WandererTradesEvent event) {
		event.getGenericTrades().add((trader, random) -> new MerchantOffer(new ItemStack(Items.EMERALD, 1), PatchouliAPI.get().getBookStack(RelicsUtil.modLoc("tome")), 12, 1, 0));
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player && !event.getObject().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).isPresent()) {
			event.addCapability(ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "properties"), new StarFallChanceProvider());
		}
	}

	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			event.getOriginal().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL)
					.ifPresent(oldStore -> event.getOriginal().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(newStore -> newStore.copyFrom(oldStore)));
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof ServerPlayer targetPlayer && targetPlayer.getInventory().contains(new ItemStack(RelicsItems.PHOENIX_FEATHER.get()))) {
			targetPlayer.getInventory().getItem(targetPlayer.getInventory().findSlotMatchingItem(new ItemStack(RelicsItems.PHOENIX_FEATHER.get()))).shrink(1);
			targetPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 65, 1));
			targetPlayer.setHealth(1);
			targetPlayer.invulnerableTime = 65;
			targetPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 28, 3));
			targetPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 65, 4));
			targetPlayer.removeEffect(MobEffects.POISON);
			targetPlayer.removeEffect(MobEffects.WITHER);
			targetPlayer.setRemainingFireTicks(60);
			event.getEntity().level().playSound(targetPlayer, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), RelicsSounds.REVIVE.get(), SoundSource.BLOCKS, 1f, 1f);
			RelicsNetwork.getInstance().sendToTrackingAndSelf(new PhoenixParticlePacket(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ()), targetPlayer);
			event.setCanceled(true);
		}
	}

	// we drop hearts and emerald shards via event, because there is no loot table
	// for hostile mobs, only 1 per each mob and mod compat would be a nightmare
	// otherwise
	// also, we use our own ItemEntity when dropping this way. the heart "should" be
	// unobtainable outside of this, and the emerald shard sound is only needed when
	// dropped this way.
	// this also saves us the onEntityItemPickup event for the heart
	@SubscribeEvent
	public static void onLivingDropsEvent(LivingDropsEvent event) {
		if (event.getEntity() instanceof Enemy) {
			if (RelicsConfig.COMMON.doEmeraldShardsDropFromMobs.get() && random.nextInt(0, RelicsConfig.COMMON.heartChance.get()) == RelicsConfig.COMMON.heartChance.get() / 2)
				event.getDrops().add(new HeartItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(RelicsItems.HEART.get().asItem())));
			if (RelicsConfig.COMMON.doHeartsDropFromMobs.get() && random.nextInt(0, RelicsConfig.COMMON.emeraldChance.get()) == RelicsConfig.COMMON.emeraldChance.get() / 2)
				event.getDrops()
						.add(new EmeraldShardItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(RelicsItems.EMERALD_SHARD.get().asItem())));
		}
	}

	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.register(StarFallChance.class);
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.side == LogicalSide.SERVER) {
			event.player.getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(star -> {
				// 1200 ticks, 100 chance
				if (event.player.level().isNight() && event.player.tickCount % 1200 == 0 && star.getStarChance() == random.nextInt(0, 100)) { // Once Every 10 Seconds on Avg
					star.rollNewChance();
					event.player.level().addFreshEntity(new FallingStarEntity(event.player));
				}
			});
		}
	}

	@SubscribeEvent
	public static void onEntityDismount(EntityMountEvent event) {
		// cancel dismount on shift for our star beam entity
		if (event.isDismounting() && event.getEntityMounting().isShiftKeyDown() && event.getEntityBeingMounted() instanceof StarBeamEntity) {
			event.setCanceled(true);
		}
	}

	// remap our blocks & items to the new modid, to be removed later
	@SubscribeEvent
	public static void onMissingMappings(MissingMappingsEvent event) {
		// For blocks
		for (MissingMappingsEvent.Mapping<Block> mapping : event.getMappings(ForgeRegistries.Keys.BLOCKS, "relics")) {
			Block remappedBlock = ForgeRegistries.BLOCKS.getValue(ResourceLocation.fromNamespaceAndPath(References.MOD_ID, mapping.getKey().getPath()));
			System.out.println("trying to remap blocks: " + remappedBlock);
			if (remappedBlock != null) {
				System.out.println("remapped: " + remappedBlock);
				mapping.remap(remappedBlock);
			}
		}
		// For items
		for (MissingMappingsEvent.Mapping<Item> mapping : event.getMappings(ForgeRegistries.Keys.ITEMS, "relics")) {
			Item remappedItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(References.MOD_ID, mapping.getKey().getPath()));
			System.out.println("trying to remap items: " + remappedItem);
			if (remappedItem != null) {
				System.out.println("remapped: " + remappedItem);
				mapping.remap(remappedItem);
			}
		}
	}

	// rewrite our old guide book to the new id if present.
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		// fix our guidebook
		Player player = event.getEntity();
		Item guideBook = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("patchouli", "guide_book"));
		if (guideBook == null)
			return;

		for (ItemStack stack : player.getInventory().items) {
			if (stack.getItem() == guideBook && stack.hasTag()) {
				CompoundTag tag = stack.getTag();
				if (tag.contains("patchouli:book") && tag.getString("patchouli:book").equals("relics:tome")) {
					tag.putString("patchouli:book", "relics_of_old:tome");
				}
			}
		}

		// also convert our advancements
		if (!(event.getEntity() instanceof ServerPlayer playerSMP))
			return;
		// get the advancements file for this player
		ServerLevel level = playerSMP.serverLevel();
		File advancementsFile = new File(level.getServer().getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).toFile(), playerSMP.getStringUUID() + ".json");

		if (!advancementsFile.exists())
			return;

		try {
			String content = Files.readString(advancementsFile.toPath());
			if (content.contains("\"relics:")) {
				String migrated = content.replace("\"relics:", "\"relics_of_old:");
				Files.writeString(advancementsFile.toPath(), migrated);
				// reload advancements for this player
				playerSMP.getAdvancements().reload(level.getServer().getAdvancements());
			}
		} catch (IOException e) {
			Relics.LOGGER.error("Failed to migrate advancements for player {}", playerSMP.getStringUUID(), e);
		}
	}

	// this fires after the item has been picked up
	@SubscribeEvent
	public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
		if (event.getStack().is(Items.EMERALD) && event.getEntity() instanceof ServerPlayer serverPlayer) {
			serverPlayer.connection.send(new ClientboundSoundEntityPacket(RelicsSounds.EMERALD_PICKUP.getHolder().get(), SoundSource.PLAYERS, serverPlayer, 1.0f, 1.0f, 1L));
		}
	}

	// this fires before the item gets picked up
	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent event) {

		if (event.getItem().getItem().is(RelicsItems.BOMB.get()) && event.getEntity() instanceof ServerPlayer serverPlayer) {
			ItemStack bombStack = event.getItem().getItem();
			for (ItemStack slot : serverPlayer.getInventory().items) {
				if (!(slot.getItem() instanceof BombBagItem))
					continue;
				if (BombBagItem.isBagFull(slot))
					continue;

				int current = BombBagItem.getBombCount(slot);
				int space = BombBagItem.MAX_BOMBS - current;

				int toInsert = Math.min(bombStack.getCount(), space);
				BombBagItem.setBombCount(slot, current + toInsert);
				bombStack.shrink(toInsert);

				if (bombStack.isEmpty()) {
					serverPlayer.take(event.getItem(), toInsert);
					event.getItem().discard();
					event.setCanceled(true);
					return;
				}
			}

		}
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		// only run when a player is damaged
		if ((event.getEntity() instanceof ServerPlayer player)) {

			// get all amulets that are equipped, this should only ever be one, but other
			// mods can add additonal charm slots and there is usally the one universal
			// curios slot as well
			CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
				List<SlotResult> amulets = handler.findCurios(stack -> stack.getItem() instanceof RelicsAmulet);
				for (SlotResult result : amulets) {
					RelicsAmulet amulet = (RelicsAmulet) result.stack().getItem();
					ElementType type = amulet.getAmuletType();
					if (RelicsUtil.matchesDamageType(type, event.getSource()) && amulet.hasCharges(result.stack())) {
						switch (type) {
						case FIRE -> {
							int damage = (int) Math.ceil(event.getAmount());
							amulet.consumeCharge(result.stack(), damage);
							player.heal(damage);
							player.invulnerableTime = player.invulnerableTime;
							player.level().playSound(null, player.blockPosition(), RelicsSounds.HEART.get(), SoundSource.PLAYERS, 0.2f, 1.0f);
							event.setCanceled(true);
							return;
						}
						case EARTH -> {
							int damage = (int) Math.ceil(event.getAmount());
							amulet.consumeCharge(result.stack(), damage);

							float scale = Math.min(damage / 20.0f, 1.0f);
							double radius = 3.0 + 12.0 * scale;

							player.level().addFreshEntity(new QuakeEntity(player.level(), player.position(), player, radius, damage));
							player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.4f + 3.0f * scale, 1.2f - scale * 0.8f);
							event.setCanceled(true);
						}
						default -> {
						}
						}
					}
				}
			});
			// empty medallion charging
			if (!event.isCanceled()) {
				int chargeAmount = (int) Math.ceil(event.getAmount());
				for (ElementType type : new ElementType[] { ElementType.FIRE, ElementType.EARTH, ElementType.WIND }) {
					if (RelicsUtil.matchesDamageType(type, event.getSource())) {
						ItemStack medallionStack = EmptyMedallion.findEmptyMedallion(player, type);
						if (medallionStack != null) {
							((EmptyMedallion) medallionStack.getItem()).charge(medallionStack, chargeAmount, player, player.level());
						}
						break;
					}
				}
			}

			// titan band drop interaction
			CompoundTag data = player.getPersistentData();
			if (player.getFirstPassenger() != null && data.getBoolean("TitanLift")) {
				ItemStack bandStack = RelicsItems.TITAN_BAND.get().getEquippedStack(player);
				if (bandStack != null) {
					Entity passenger = player.getFirstPassenger();
					passenger.stopRiding();
					player.level().playSound(null, player.blockPosition(), RelicsSounds.THROW.get(), SoundSource.PLAYERS, 0.3f, 1.0f);
					bandStack.getOrCreateTag().putBoolean("TitanLift", false);
				}
			}
		}
		// headband of valor, player attack bonus
		if (event.getSource().getDirectEntity() instanceof Player player && !player.level().isClientSide) {
			ItemStack headSlot = player.getItemBySlot(EquipmentSlot.HEAD);
			if (headSlot.getItem() instanceof HeadbandOfValor headband && HeadbandOfValor.isNaked(player)) {
				int oldAmount = (int) event.getAmount();
				int newAmount = headband.getBonus(oldAmount);
				event.setAmount(newAmount);
				player.level().playSound(null, event.getEntity().blockPosition(), RelicsSounds.ESCALATE.get(), SoundSource.PLAYERS, 0.5f, 0.8f + player.getRandom().nextFloat() * 0.4f);
				headSlot.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.HEAD));
			}
		}

	}

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		Player player = event.getEntity();
		if (!player.getMainHandItem().isEmpty())
			return;
		if (player.getFirstPassenger() != null)
			return;
		if (!(event.getTarget() instanceof LivingEntity target))
			return;

		ItemStack bandStack = RelicsItems.TITAN_BAND.get().getEquippedStack(player);
		if (bandStack == null)
			return;

		// mount on both sides for client prediction
		target.startRiding(player, true);

		// server-only: durability, state, sounds
		if (!event.getLevel().isClientSide) {
			bandStack.hurtAndBreak(1, player, p -> {
			});
			bandStack.getOrCreateTag().putBoolean("TitanLift", true);
			player.level().playSound(null, player.blockPosition(), RelicsSounds.LIFT.get(), SoundSource.PLAYERS, 0.3f, 1.0f);
			if (target instanceof Mob mob) {
				mob.playAmbientSound();
			}
		}
		event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getEntity();
		if (player.getFirstPassenger() != null && player.getPersistentData().getBoolean("TitanLift") && event.isCancelable()) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onAttackEntity(AttackEntityEvent event) {
		Player player = event.getEntity();
		if (player.getFirstPassenger() != null && player.getPersistentData().getBoolean("TitanLift")) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onEnderManAnger(EnderManAngerEvent event) {
		EnderMan enderman = event.getEntity();
		if (!enderman.isCreepy() || enderman.getTarget() != event.getPlayer())
			return;
		// in case the enderman ports away (rain, water touched) and isCreepy or
		// getTarget is not reset, we skip if he is to far away (16 blocks)
		if (enderman.distanceToSqr(event.getPlayer()) > 256.0)
			return;

		if (event.getPlayer() instanceof ServerPlayer player) {
			CompoundTag data = player.getPersistentData();
			long lastCharge = data.getLong("EnderMedallionLastCharge");
			if (player.level().getGameTime() - lastCharge < 400)
				return;

			ItemStack stack = EmptyMedallion.findEmptyMedallion(player, ElementType.ENDER);
			if (stack != null) {
				((EmptyMedallion) stack.getItem()).charge(stack, 5, player, player.level());
				data.putLong("EnderMedallionLastCharge", player.level().getGameTime());
			}
		}
	}

	@SubscribeEvent
	public static void onEnderTeleport(EntityTeleportEvent.EnderEntity event) {
		if (event.getEntityLiving().hasEffect(RelicsEffects.ENDER_LOCK.get())) {
			event.setCanceled(true);
		}
	}
}