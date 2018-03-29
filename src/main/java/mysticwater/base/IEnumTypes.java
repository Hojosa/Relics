package mysticwater.base;

import net.minecraft.util.IStringSerializable;

public interface IEnumTypes
{

	//public enum VariantTypes {WHITE, ORGANGE, MAGENTA, LIGHTBLUE, YELLOW, LIME, PINK, GRAY, SILVER, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK, GlASS, LAPIS;}
	public enum SlabColor1 implements IStringSerializable {WHITE, ORANGE, MAGENTA, LIGHTBLUE, YELLOW, LIME, PINK, GRAY;

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return name().toLowerCase();
	}}
	
	public enum SlabColor2 implements IStringSerializable {SILVER, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK;
		@Override
		public String getName()
		{
			// TODO Auto-generated method stub
			return name().toLowerCase();
		}}
	
	public enum Other implements IStringSerializable {LAPIS;
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return name().toLowerCase();
	}}
	
	public enum Glass implements IStringSerializable {GLASS;
		@Override
		public String getName()
		{
			// TODO Auto-generated method stub
			return name().toLowerCase();
		}}
	
	public enum Category {COLOR, SLABCOLOR1GLASS, SLABCOLOR2GLASS, OTHER, GLASS};
	
	//public Enum[] getUsedEnumValues(Category cat);
//	//public enum VariantTypes implements IStringSerializable{
//		;
//		@Override
//		public String getName()
//		{
//			// TODO Auto-generated method stub
//			return this.getName();
//		}};
//	//public Enum[][] ArrayOfTypes = new Enum[][]{ColorSet1.values(), ColorSet2.values(), Other.values()};
	
	//returns the needed enum for the blockstate
	//0 = ColorSet1, 1 = ColorSet2, 2 Other
	//Enum getEnumSet(int setNum);

}
