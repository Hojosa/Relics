package mysticwater.base;

import net.minecraft.item.ItemStack;

public interface ISwordPedestalData
{
	
	public ItemStack sword = null;
	public int rotation = 0;
	//public int sinShift;
	public int rotationSpeed = 4;
	public int[] colorRGBA = new int[]{255,255,255,48};
	public boolean isFloating = false;
	public boolean isRotating = false;
	public boolean lightBeamEnabled = false;
	public boolean enchantmentGlimmer = false;
	public String pedestalName = "Basic";
	public int baseRotation = 0;
	public boolean clockwiseRotation = false;

}
