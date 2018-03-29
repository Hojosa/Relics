package mysticwater.world.dimension;

import mysticwater.init.ModWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderMysticLands extends WorldProvider
{
	
	@Override
	public void createBiomeProvider()
	{
		//this.biomeProvider = new BiomeProvider(ModWorld.MYSTIC_CAVE)
	}

	@Override
	public DimensionType getDimensionType()
	{
		// TODO Auto-generated method stub
		return ModWorld.MYSTIC_LANDS;
	}
	
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return false;
	}
	
	@Override
	public boolean canRespawnHere()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int x, int z)
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float par1, float par2)
	{
		return new Vec3d(0.3D, 0.5D, 0.8D);
	}
	
//	@Override
//	public IChunkGenerator createChunGenerator()
//	{
//		return new MysticLandsChunkProvider(worldObj, worldObj.getSeed());
//	}
	

}
