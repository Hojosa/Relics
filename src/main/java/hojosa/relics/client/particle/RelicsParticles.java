package hojosa.relics.client.particle;

import hojosa.relics.lib.References;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsParticles {
	
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = 
			DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, References.MOD_ID);
	
	public static final RegistryObject<SimpleParticleType> FLAME_PATTICLES = 
			PARTICLE_TYPES.register(References.UnlocalizedName.FLAME_PARTICLE, () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> STAR_BEAM_TORCH_PATTICLES = 
			PARTICLE_TYPES.register(References.UnlocalizedName.STAR_BEAM_TORCH_PARTICLES, () -> new SimpleParticleType(true));

	public static final RegistryObject<SimpleParticleType> STAR_BEAM_GRIND_PATTICLES = 
			PARTICLE_TYPES.register(References.UnlocalizedName.STAR_BEAM_GRIND_PARTICLES, () -> new SimpleParticleType(true));
}