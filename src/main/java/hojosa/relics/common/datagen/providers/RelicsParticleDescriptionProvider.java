package hojosa.relics.common.datagen.providers;

import hojosa.relics.client.particle.RelicsParticles;
import hojosa.relics.lib.RelicsUtil;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ParticleDescriptionProvider;

public class RelicsParticleDescriptionProvider extends ParticleDescriptionProvider {

	public RelicsParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
		super(output, fileHelper);
	}

	@Override
	protected void addDescriptions() {
		sprite(RelicsParticles.STAR_PATTICLES.get(), RelicsUtil.modLoc("star_particle"));
	}
	
	@Override
	public String getName() {
		return "Relics Particles";
	}
}
