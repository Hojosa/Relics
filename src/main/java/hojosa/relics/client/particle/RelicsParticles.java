package hojosa.relics.client.particle;

import hojosa.relics.lib.References;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RelicsParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = 
			DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, References.MOD_ID);
	
	public static final RegistryObject<SimpleParticleType> STAR_PATTICLES = 
			PARTICLE_TYPES.register(References.UnlocalizedName.STAR_PARTICLE, () -> new SimpleParticleType(true));

}
