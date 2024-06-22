package hojosa.relics.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;


public class FireTablet extends RelicsItem //implements ICurioItem
{
	public FireTablet(int stackSize, Rarity raity){
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
	        public void curioTick(SlotContext slotContext) {
	        	slotContext.entity().addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20));
	        }
	        
	    	@Override
	    	public boolean canEquipFromUse(SlotContext slotContext) {
	    		return true;
	    	}
	    });
	}
}

