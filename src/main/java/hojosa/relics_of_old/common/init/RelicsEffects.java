package hojosa.relics_of_old.common.init;

import hojosa.relics_of_old.common.effect.EnderLockEffect;
import hojosa.relics_of_old.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, References.MOD_ID);

	public static final RegistryObject<MobEffect> ENDER_LOCK = EFFECTS.register("ender_lock", EnderLockEffect::new);
	
}