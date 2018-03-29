package mysticwater.core.handler;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.util.EnumHelper;

public class EnumHandler
{
	public static ToolMaterial flame = EnumHelper.addToolMaterial("flame", 2, 800, 7.0F, 2.5F, 10);
	public static ToolMaterial leaf = EnumHelper.addToolMaterial("leaf", 2, 800, 7.0F, 2.5F, 10);
	public static enum Category {COLOR, COLOR1, COLOR2, GLASSCOLOR_0, GLASSCOLOR_1 , OTHER, GLASS};
	public static enum Other {LAPIS, GLASS};
	public static final String[] Color1 = new String[] {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray"};
	public static final String[] Color2 = new String[] {"silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};
	public enum ColorSet1 {WHITE, ORGANGE, MAGENTA, LIGHTBLUE, YELLOW, LIME, PINK, GRAY;}
	public enum ColorSet2 {SILVER, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK;}
	
	public enum ColorSet implements IStringSerializable
	{
		/*WHITE,ORGANGE,MAGENTA,LIGHTBLUE,YELLOW,LIME,PINK,GRAY,
		SILVER,CYAN,PURPLE,BLUE,BROWN,GREEN,RED,BLACK;*/
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
		BLACK(15, "black"),
		LAPIS(16, "lapis"),
		GLASS(17, "glass");
		
		private int ID;
		private String name;
	
		private ColorSet(int ID, String name)
		{
			this.ID = ID;
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return this.name;
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
		
//		public static LinkedList<Enum<ColorSet>> toList()
//		{
//			LinkedList<Enum<ColorSet>> enumList = new LinkedList<Enum<ColorSet>>();
//			for(ColorSet e : ColorSet.values())
//			{
//				enumList.add(e);
//			}
//			return enumList;
//		}
		
		private static LinkedList<Enum<ColorSet>> toList()
		{
			LinkedList<Enum<ColorSet>> enumList = new LinkedList<Enum<ColorSet>>();
			if(enumList.isEmpty())
			{
				for(ColorSet e : ColorSet.values())
				{
					enumList.add(e);
					
				}
				
				//System.out.println(enumList + " LIST TRUE");
			}
			return enumList;
		}
//			System.out.println(enumList + " LIST");
//			if(shortList)
//			{
//				LinkedList<Enum<ColorSet>> subList = new LinkedList<Enum<ColorSet>>(enumList);
//				List<Enum<ColorSet>> outList = new LinkedList<Enum<ColorSet>>();
//				System.out.println(subList.subList(0, 8) + " SUB");
//				
//				for(Enum<ColorSet> e : subList.subList(0, 8))
//				{
//					outList.add(e);
//				}
//				return outList;
		//	}
		public static List getStateList(Category cat, boolean fullList)
		{
			Map<Category, List<Enum<ColorSet>>> stateMap = new LinkedHashMap<Category,List<Enum<ColorSet>>>();
			
			if(fullList)
			{
				System.out.println(ColorSet.toList().size() + " size");
				stateMap.put(Category.COLOR, ColorSet.toList().subList(0, 16));
				stateMap.put(Category.OTHER, ColorSet.toList().subList(16, 16));
				stateMap.put(Category.GLASS, ColorSet.toList().subList(17, 17));
				
				
			}
			else
			{
			//System.out.println(ColorSet.toList().subList(17, 18) + " yourThere?");
				stateMap.put(Category.GLASSCOLOR_0, ColorSet.toList().subList(0, 8));
				stateMap.put(Category.GLASSCOLOR_1, ColorSet.toList().subList(8, 16));
				stateMap.put(Category.OTHER, ColorSet.toList().subList(16, 17));
				stateMap.put(Category.GLASS, ColorSet.toList().subList(17, 18));
			}
			//System.out.println(stateMap.get(cat) + " OUT");
			return stateMap.get(cat);
		}
		
//		public static List<Enum<ColorSet>> toList(int amount)
//		{
//			LinkedList<Enum<ColorSet>> colorSetList = new LinkedList<Enum<ColorSet>>();
//			
//			for(ColorSet e : ColorSet.values())
//			{
//				colorSetList.add(e);
//			}
//			
//			List<Enum<ColorSet>> colorSubList = colorSetList.subList(0, amount);
//			colorSubList.forEach(s -> colorSetList.pollFirst());
//			
//			
//			return enumList;
//		}
//	}
	
//	public enum ColorSet1  implements IStringSerializable
//	{
//		WHITE(0, "white"),
//		ORGANGE(1, "orange"),
//		MAGENTA(2, "magenta"),
//		LIGHTBLUE(3, "light_blue"),
//		YELLOW(4, "yellow"),
//		LIME(5, "lime"),
//		PINK(6, "pink"),
//		GRAY(7, "gray");
//	
//		private int ID;
//		private String name;
//	
//		private ColorSet1(int ID, String name)
//		{
//			this.ID = ID;
//			this.name = name;
//		}
//		
//		@Override
//		public String getName()
//		{
//			return name;
//		}
//		
//		public int getID()
//		{
//			return ID;
//		}
//		
//		
//		@Override
//		public String toString()
//		{
//			return getName();
//		}
//	}
//	
//	public enum ColorSet2 implements IStringSerializable
//	{
//		SILVER(0, "silver"),
//		CYAN(1, "cyan"),
//		PURPLE(2, "purple"),
//		BLUE(3, "blue"),
//		BROWN(4, "brown"),
//		GREEN(5, "green"),
//		RED(6, "red"),
//		BLACK(7, "black");
//	
//		private int ID;
//		private String name;
//	
//		private ColorSet2(int ID, String name)
//		{
//			this.ID = ID;
//			this.name = name;
//		}
//		
//		@Override
//		public String getName()
//		{
//			return name;
//		}
//		
//		public int getID()
//		{
//			return ID;
//		}
//		
//		@Override
//		public String toString()
//		{
//			return getName();
//		}
//	}
//	
//	public enum OtherSet implements IStringSerializable
//	{
//		GLASS(0, "glass"),
//		LAPIS(1, "lapis");
//
//		
//		private int ID;
//		private String name;
//		
//		private OtherSet(int ID, String name)
//		{
//			this.ID = ID;
//			this.name = name;
//		}
//		
//		@Override
//		public String getName()
//		{
//			return name;
//		}
//		
//		public int getID()
//		{
//			return ID;
//		}
//		
//		@Override
//		public String toString()
//		{
//			return getName();
//		}
//		
//		
//		
//	}
	}		
}
