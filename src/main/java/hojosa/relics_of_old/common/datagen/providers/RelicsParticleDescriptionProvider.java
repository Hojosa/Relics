package hojosa.relics_of_old.common.datagen.providers;

import hojosa.relics_of_old.client.particle.RelicsParticles;
import hojosa.relics_of_old.lib.RelicsUtil;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ParticleDescriptionProvider;

public class RelicsParticleDescriptionProvider extends ParticleDescriptionProvider {

	public RelicsParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
		super(output, fileHelper);
	}

	@Override
	protected void addDescriptions() {
		sprite(RelicsParticles.FLAME_PATTICLES.get(), RelicsUtil.modLoc("fire_0"));
		sprite(RelicsParticles.STAR_BEAM_TORCH_PATTICLES.get(), RelicsUtil.modLoc("star_dust"));
		sprite(RelicsParticles.STAR_BEAM_GRIND_PATTICLES.get(), RelicsUtil.modLoc("star_dust"));
	}
	
	@Override
	public String getName() {
		return "Relics Particles";
	}
}