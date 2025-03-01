package hojosa.relics.common.item;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

public class InfuseableItem extends RelicsItem{
	
	private RegistryObject<RelicsItem> infused_item;
	
	public InfuseableItem(int stackSize, Rarity raity, RegistryObject<RelicsItem> infusedStarPiece) {
		super(stackSize, raity);
		this.infused_item=infusedStarPiece;
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		pTooltipComponents.add(Component.translatable("Spend 3 levels to infuse"));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
		System.out.println(pPlayer.experienceLevel);
		if(pPlayer.experienceLevel >= 3) {
            pPlayer.startUsingItem(pUsedHand);
            //sound
            return InteractionResultHolder.consume(itemstack);
		}
		return super.use(pLevel, pPlayer, pUsedHand);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		System.out.println(((Player)pLivingEntity).experienceLevel);
		if(((Player)pLivingEntity).experienceLevel >= 3) {
			((Player)pLivingEntity).giveExperienceLevels(-3);
			pStack.shrink(1);
			((Player)pLivingEntity).addItem(new ItemStack(this.getInfusedItem().get()));
			return pStack;
		}
		return super.finishUsingItem(pStack, pLevel, pLivingEntity);
	}
	
	@Override
	public int getUseDuration(ItemStack pStack) {
		return 30;
	}
	
	private RegistryObject<RelicsItem> getInfusedItem() {
		return this.infused_item;
	}
}
