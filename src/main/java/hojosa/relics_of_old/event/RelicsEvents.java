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
import hojosa.relics_of_old.common.init.RelicsConfig;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.item.entity.EmeraldShardItemEntity;
import hojosa.relics_of_old.common.item.entity.HeartItemEntity;
import hojosa.relics_of_old.common.player.StarFallChance;
import hojosa.relics_of_old.common.player.StarFallChanceProvider;
import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsUtil;
import hojosa.relics_of_old.network.PhoenixParticlePacket;
import hojosa.relics_of_old.network.RelicsNetwork;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import vazkii.patchouli.api.PatchouliAPI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class RelicsEvents {
	private static Random random = new Random();

	@SubscribeEvent
	public static void fluidWalker(LivingFluidCollisionEvent event) {
		if(event.getFluidState().is(FluidTags.WATER) && RelicsItems.WATER_TABLET.get().isEquipped(event.getEntity())) {
			event.setResult(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public static void addVillagerTrades(VillagerTradesEvent event) {
		if(event.getType() == VillagerProfession.CLERIC) {
			Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
			ItemStack tablet = new ItemStack(RelicsItems.WATER_TABLET.get(), 1);
			int villagerLevel = 3;
			
			trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 64), tablet, 1, 8, 0));
		}
		if(event.getType() == VillagerProfession.LIBRARIAN) {
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
        if(event.getObject() instanceof Player && !event.getObject().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(References.MOD_ID, "properties"), new StarFallChanceProvider());
            }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(
            		oldStore -> event.getOriginal().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(
							newStore -> newStore.copyFrom(oldStore)));
        }
    }
    
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if(event.getEntity() instanceof ServerPlayer targetPlayer && targetPlayer.getInventory().contains(new ItemStack(RelicsItems.PHOENIX_FEATHER.get()))){
        		targetPlayer.getInventory().getItem(targetPlayer.getInventory().findSlotMatchingItem(new ItemStack(RelicsItems.PHOENIX_FEATHER.get()))).shrink(1);
        		targetPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 220));
        		targetPlayer.setHealth(10);
        		event.getEntity().level().playSound(targetPlayer, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), RelicsSounds.REVIVE.get(), SoundSource.BLOCKS, 1f, 1f);
        		RelicsNetwork.getInstance().sendToTrackingAndSelf(new PhoenixParticlePacket(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ()), targetPlayer);
        		event.setCanceled(true);
        	}
    }
    
    //we drop hearts and emerald shards via event, because there is no loot table for hostile mobs, only 1 per each mob and mod compat would be a nightmare otherwise
    //also, we use our own ItemEntity when dropping this way. the heart "should" be unobtainable outside of this, and the emerald shard sound is only needed when dropped this way. 
    //this also saves us the onEntityItemPickup event for the heart
    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event) {
    	if(event.getEntity() instanceof Enemy) {
    		if(RelicsConfig.COMMON.doEmeraldShardsDropFromMobs.get()  && random.nextInt(0, RelicsConfig.COMMON.heartChance.get()) == RelicsConfig.COMMON.heartChance.get()/2)
    			event.getDrops().add(new HeartItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(RelicsItems.HEART.get().asItem())));
    		if(RelicsConfig.COMMON.doHeartsDropFromMobs.get()  && random.nextInt(0, RelicsConfig.COMMON.emeraldChance.get()) == RelicsConfig.COMMON.emeraldChance.get()/2)
    			event.getDrops().add(new EmeraldShardItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(RelicsItems.EMERALD_SHARD.get().asItem())));
    	}
    }
    
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(StarFallChance.class);
    }
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            event.player.getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(star -> {
            	//1200 ticks, 100 chance
                if(event.player.level().isNight() && event.player.tickCount % 1200 == 0 && star.getStarChance() == random.nextInt(0, 100)) { // Once Every 10 Seconds on Avg
                	star.rollNewChance();
                	event.player.level().addFreshEntity(new FallingStarEntity(event.player));
                }
            });
        }
    }
    
    @SubscribeEvent
    public static void onEntityDismount(EntityMountEvent event) {
    	//cancel dismount on shift for our star beam entity
        if (event.isDismounting() && event.getEntityMounting().isShiftKeyDown() && event.getEntityBeingMounted() instanceof StarBeamEntity) {
            event.setCanceled(true);
        }
    }
    
    //remap our blocks & items to the new modid, to be removed later
    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
    	// For blocks
    	for (MissingMappingsEvent.Mapping<Block> mapping : event.getMappings(ForgeRegistries.Keys.BLOCKS, "relics")) {
    	    Block remappedBlock = ForgeRegistries.BLOCKS.getValue(
    	    		ResourceLocation.fromNamespaceAndPath(References.MOD_ID, mapping.getKey().getPath())
    	    );
    	    System.out.println("trying to remap blocks: " + remappedBlock);
    	    if (remappedBlock != null) {
    	    	System.out.println("remapped: " + remappedBlock);
    	        mapping.remap(remappedBlock);
    	    }
    	}
    	// For items
    	for (MissingMappingsEvent.Mapping<Item> mapping : event.getMappings(ForgeRegistries.Keys.ITEMS, "relics")) {
    	    Item remappedItem = ForgeRegistries.ITEMS.getValue(
    	    		ResourceLocation.fromNamespaceAndPath(References.MOD_ID, mapping.getKey().getPath())
    	    );
    	    System.out.println("trying to remap items: " + remappedItem);
    	    if (remappedItem != null) {
    	    	System.out.println("remapped: " + remappedItem);
    	        mapping.remap(remappedItem);
    	    }
    	}
    }
    
    //rewrite our old guide book to the new id if present. 
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    	//fix our guidebook
        Player player = event.getEntity();
        Item guideBook = ForgeRegistries.ITEMS.getValue(new ResourceLocation("patchouli", "guide_book"));
        if (guideBook == null) return;
        
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == guideBook && stack.hasTag()) {
                CompoundTag tag = stack.getTag();
                if (tag.contains("patchouli:book") && 
                    tag.getString("patchouli:book").equals("relics:tome")) {
                    tag.putString("patchouli:book", "relics_of_old:tome");
                }
            }
        }
        
        //also convert our advancements
        if (!(event.getEntity() instanceof ServerPlayer playerSMP)) return;
        // get the advancements file for this player
        ServerLevel level = playerSMP.serverLevel();
        File advancementsFile = new File(
            level.getServer().getWorldPath(LevelResource.PLAYER_ADVANCEMENTS_DIR).toFile(),
            playerSMP.getStringUUID() + ".json"
        );
        
        if (!advancementsFile.exists()) return;
        
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
}