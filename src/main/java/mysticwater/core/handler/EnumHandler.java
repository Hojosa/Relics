package mysticwater.core.handler;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.EnumHelper;

public class EnumHandler
{
	public static ToolMaterial flame = EnumHelper.addToolMaterial("flame", 2, 800, 7.0F, 2.5F, 10);
	public static ToolMaterial leaf = EnumHelper.addToolMaterial("leafl", 2, 800, 7.0F, 2.5F, 10);

	
	public static enum Category {COLOR1, COLOR2, GLASS, LAPIS, Test};
	public static enum Typ {WHITE}
	public static final String[] Color1 = new String[] {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};
	public static final String[] Color2 = new String[] {"silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};
	
	public enum EnumColor1 implements IStringSerializable
	{
		WHITE(0, "white"),
		ORGANGE(1, "orange"),
		MAGENTA(2, "magenta"),
		LIGHTBLUE(3, "light_blue"),
		YELLOW(4, "yellow"),
		LIME(5, "lime"),
		PINK(6, "pink"),
		GRAY(7, "gray"),
		SILVER(8, "silver"),
		CYAN(9, "cyan"),
		PURPLE(10, "purple"),
		BLUE(11, "blue"),
		BROWN(12, "brown"),
		GREEN(13, "green"),
		RED(14, "red"),
		BLACK(15, "black");
		
	
		private int ID;
		private String name;
	
		private EnumColor1(int ID, String name)
		{
			this.ID = ID;
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		public int getID()
		{
			return ID;
		}
		
		@Override
		public String toString()
		{
			return getName();
		}
	}
	
	public enum EnumColor2 implements IStringSerializable
	{
		SILVER(0, "silver"),
		CYAN(1, "cyan"),
		PURPLE(2, "purple"),
		BLUE(3, "blue"),
		BROWN(4, "brown"),
		GREEN(5, "green"),
		RED(6, "red"),
		BLACK(7, "black");
	
		private int ID;
		private String name;
	
		private EnumColor2(int ID, String name)
		{
			this.ID = ID;
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		public int getID()
		{
			return ID;
		}
		
		@Override
		public String toString()
		{
			return getName();
		}
	}
}
