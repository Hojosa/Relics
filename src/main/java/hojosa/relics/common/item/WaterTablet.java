package hojosa.relics.common.item;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;


public class WaterTablet extends RelicsItem implements ICurioItem
{
	public WaterTablet(int stackSize, Rarity raity){
		super(stackSize, raity);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
		return CuriosApi.createCurioProvider(new ICurio() {
		    
	        @Override
	        public ItemStack getStack() {
	          return stack;
	        }
	        
	    	@Override
	    	public boolean canEquipFromUse(SlotContext slotContext) {
	    		return true;
	    	}
	    });
	}
	
	public boolean isEquipped(@Nullable LivingEntity entity) {
		return entity != null && CuriosApi.getCuriosHelper().findFirstCurio(entity, this).isPresent();
	}
}
