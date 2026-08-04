package hojosa.relics_of_old.common.init;

import com.mojang.serialization.Codec;

import hojosa.relics_of_old.lib.References;
import hojosa.relics_of_old.lib.RelicsParticleOptions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RelicsParticles {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, References.MOD_ID);

	public static final RegistryObject<SimpleParticleType> FLAME_PATTICLES = PARTICLE_TYPES.register(References.UnlocalizedName.FLAME_PARTICLE, () -> new SimpleParticleType(true));
	public static final RegistryObject<SimpleParticleType> STAR_BEAM_TORCH_PATTICLES = PARTICLE_TYPES.register(References.UnlocalizedName.STAR_BEAM_TORCH_PARTICLES, () -> new SimpleParticleType(true));

	public static final RegistryObject<SimpleParticleType> STAR_BEAM_GRIND_PATTICLES = PARTICLE_TYPES.register(References.UnlocalizedName.STAR_BEAM_GRIND_PARTICLES, () -> new SimpleParticleType(true));

	public static final RegistryObject<SimpleParticleType> SPARKLE_PARTICLES = PARTICLE_TYPES.register(References.UnlocalizedName.SPARKLE_PATTICLES, () -> new SimpleParticleType(true));

	public static final RegistryObject<ParticleType<RelicsParticleOptions>> RUNE_PARTICLE = PARTICLE_TYPES.register(References.UnlocalizedName.RUNE_PARTICLE,
			() -> new ParticleType<>(true, RelicsParticleOptions.DESERIALIZER) {
				@Override
				public Codec<RelicsParticleOptions> codec() {
					return RelicsParticleOptions.codec(RUNE_PARTICLE);
				}
			});

	public static final RegistryObject<SimpleParticleType> FLARE_PARTICLE = PARTICLE_TYPES.register(References.UnlocalizedName.FLARE_PARTICLE, () -> new SimpleParticleType(true));

	public static final RegistryObject<ParticleType<RelicsParticleOptions>> MAGIC_SCRAMBLE_PARTICLE = PARTICLE_TYPES.register(References.UnlocalizedName.MAGIC_SCRAMBLE_PARTICLE,
	          () -> new ParticleType<>(true, RelicsParticleOptions.DESERIALIZER) {
	              @Override
	              public Codec<RelicsParticleOptions> codec() {
	                  return RelicsParticleOptions.codec(MAGIC_SCRAMBLE_PARTICLE);
	              }
	          });
}