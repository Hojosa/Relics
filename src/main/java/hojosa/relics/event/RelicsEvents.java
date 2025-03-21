package hojosa.relics.event;

import java.util.List;
import java.util.Random;

import be.florens.expandability.api.forge.LivingFluidCollisionEvent;
import hojosa.relics.common.entity.FallingStarEntity;
import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.common.init.RelicsTags;
import hojosa.relics.common.player.StarFallChance;
import hojosa.relics.common.player.StarFallChanceProvider;
import hojosa.relics.lib.References;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class RelicsEvents {

	@SubscribeEvent
	public static void fluidWalker(LivingFluidCollisionEvent event) {
		if(event.getFluidState().is(FluidTags.WATER) && RelicsItems.WATER_TABLET.get().isEquipped(event.getEntity())) {
			event.setResult(Event.Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public static void addVillagerTrages(VillagerTradesEvent event) {
		if(event.getType() == VillagerProfession.LIBRARIAN) {
			Int2ObjectMap<List<ItemListing>> trades = event.getTrades();
			ItemStack tablet = new ItemStack(RelicsItems.WATER_TABLET.get(), 1);
			int villagerLevel = 3;
			
			trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(new ItemStack(Items.EMERALD, 64), tablet, 1, 8, 0));
		}
	}
	
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).isPresent()) {
                event.addCapability(new ResourceLocation(References.MOD_ID, "properties"), new StarFallChanceProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(oldStore -> {
                event.getOriginal().getCapability(StarFallChanceProvider.PLAYER_STAR_FALL).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
    
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if(event.getEntity() instanceof ServerPlayer targetPlayer) {
        	if(targetPlayer.getInventory().contains(new ItemStack(RelicsItems.PHOENIX_FEATHER.get()))){
        		targetPlayer.getInventory().getItem(targetPlayer.getInventory().findSlotMatchingItem(new ItemStack(RelicsItems.PHOENIX_FEATHER.get()))).shrink(1);
        		targetPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 220));
        		targetPlayer.setHealth(10);
        		//play music
        		//particles
        		event.setCanceled(true);
        	}
        }
    }
    
    @SubscribeEvent
    public static void onEntityItemPickup(EntityItemPickupEvent event) {
    	if(event.getItem().getItem().is(RelicsTags.Items.HEART)) {
    		event.getEntity().heal(6);
    		event.getItem().remove(RemovalReason.DISCARDED);
    		event.setCanceled(true);
    		//sound?
    	}
    }
    
    //we drop the heart via event, because there is no loottable for hostile mobs, only 1 per each mob and mod compat would be a nightmare otherwise
    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event) {
    	if(event.getEntity() instanceof Enemy) {
    		if(new Random().nextInt(0, 17) == 4)
    			event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(RelicsItems.HEART.get().asItem())));
    		if(new Random().nextInt(0, 10) == 4)
    			event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(RelicsItems.EMERALD_SHARD.get().asItem())));

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
                if(event.player.level().isNight() && event.player.tickCount % 1200 == 0 && star.getStarChance() == new Random().nextInt(0, 100)) { // Once Every 10 Seconds on Avg
                	star.rollNewChance();
                	event.player.level().addFreshEntity(new FallingStarEntity(event.player));
                }
            });
        }
    }
}