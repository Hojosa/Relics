package hojosa.relics.common.item;

import java.util.List;

import hojosa.relics.lib.RelicsUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class LostPage extends RelicsItem{
	private String additionalText;

	public LostPage(int stackSize, String addText) {
		super(stackSize, Rarity.RARE);
		additionalText=addText;
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		pTooltipComponents.add(Component.translatable(additionalText));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
		
		return InteractionResultHolder.consume(itemstack);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
	    if (pLivingEntity instanceof ServerPlayer serverPlayer) {
	    	ServerPlayer player = serverPlayer;
	    	Advancement adv = player.getServer().getAdvancements().getAdvancement(RelicsUtil.modLoc((pStack.getItem().toString())));
	    	if(!player.getAdvancements().getOrStartProgress(adv).isDone()) {
	    		CriteriaTriggers.CONSUME_ITEM.trigger(player, pStack);
				pStack.shrink(1);
	    	}
	      }
		return pStack;
	}
	
	@Override
	public int getUseDuration(ItemStack pStack) {
		return 1;
	}
}
