package hojosa.relics.event;

import java.util.List;

import be.florens.expandability.api.forge.LivingFluidCollisionEvent;
import hojosa.relics.common.init.RelicsItems;
import hojosa.relics.lib.References;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MODID)
public class RelicsEvents {

	@SubscribeEvent
	public static void fluidWalker(LivingFluidCollisionEvent event) {
		if(event.getFluidState().is(FluidTags.WATER) && RelicsItems.WATER_TABLET.get().isEquipped(event.getEntityLiving())) {
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
}
