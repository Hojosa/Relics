package hojosa.relics_of_old.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BadBowItem extends BowItem {
	
	public BadBowItem() {
        super(new Item.Properties().durability(384));
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        // Force minimum charge (3 ticks) regardless of actual draw time
        super.releaseUsing(stack, level, entity, this.getUseDuration(stack) - 3);
    }
}