package hojosa.relics_of_old.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FortuneCookieItem extends RelicsItem {
	private static final List<String> FORTUNES = new ArrayList<>();
	private static final Random RAND = new Random();

	static {
		storeFortunes();
	}

	public FortuneCookieItem() {
		super(new Item.Properties().stacksTo(16).food(new FoodProperties.Builder().nutrition(1).saturationMod(0.0f).alwaysEat().fast().build()));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (!level.isClientSide && entity instanceof Player player) {
			String fortune = FORTUNES.get(RAND.nextInt(FORTUNES.size()));
			player.sendSystemMessage(Component.literal(fortune).withStyle(ChatFormatting.ITALIC));
		}
		return super.finishUsingItem(stack, level, entity);
	}

	private static void storeFortunes() {
		FORTUNES.add("Don't believe everything you eat.");
		FORTUNES.add("They say the best defense is a good oak fence.");
		FORTUNES.add("Emeralds don't grow on trees.");
		FORTUNES.add("Health begets wealth.");
		FORTUNES.add("Herobrine isn't real.");
		FORTUNES.add("This cookie intentionally left blank.");
		FORTUNES.add("They say the Phoenix likes sunflowers.");
		FORTUNES.add("Things that like fire don't like ice.");
		FORTUNES.add("Wet things are more conductive.");
		FORTUNES.add("Don't get sloppy when working with teleportation.");
		FORTUNES.add("The full moon is magical.");
		FORTUNES.add("Don't waste moonlight by sleeping.");
		FORTUNES.add("Nudity is magical.");
		FORTUNES.add("Magic begins and ends with the stars.");
		FORTUNES.add("Don't bother with astrology.");
		FORTUNES.add("Look up to see something blue.");
		FORTUNES.add("The thing that's about to happen is just a coincidence.");
		FORTUNES.add("Fortune cookies are magical.");
		FORTUNES.add("Stardust is magical.");
		FORTUNES.add("You are magical.");
		FORTUNES.add("Redstone connects things to other things.");
		FORTUNES.add("Gold is a very pure element.");
		FORTUNES.add("Sometimes these things are useless.");
		FORTUNES.add("Friendship is magical.");
		FORTUNES.add("The stars shine on everyone equally.");
		FORTUNES.add("Lawn mowing is no way to make a living.");
		FORTUNES.add("Retracing your steps is like not going anywhere.");
		FORTUNES.add("Take notes, it helps.");
		FORTUNES.add("A tuning fork can help when it's too bright to see.");
		FORTUNES.add("If you make a mistake, try again from the beginning.");
		FORTUNES.add("Remember to clean up after yourself.");
		FORTUNES.add("Don't take feathers for granted.");
		FORTUNES.add("Don't forget to sleep in real life.");
		FORTUNES.add("Insomnia is magical.");
		FORTUNES.add("You can read a cookie but you can't eat a book.");
		FORTUNES.add("Eat more fortune cookies.");
		FORTUNES.add("Orientation doesn't matter.");
		FORTUNES.add("I ship Jean/Steve.");
		FORTUNES.add("Copy this fortune cookie into your signature.");
		FORTUNES.add("I see a cryptic foreshadowing in your future.");
		FORTUNES.add("A shovel helps for digging up fragile things.");
		FORTUNES.add("You can't see the stars if it's not dark enough.");
		FORTUNES.add("Try using pistons.");
		FORTUNES.add("The kobolds love you.");
		FORTUNES.add("Lapis lazuli is associated with enchantment.");
		FORTUNES.add("A boomerang returns more than itself.");
		FORTUNES.add("The Phoenix despises undead.");
		FORTUNES.add("All rituals need a focus, even if it's you.");
		FORTUNES.add("Diamonds signify perfection.");
		FORTUNES.add("Starstone represents the astral realm.");
		FORTUNES.add("Emeralds are related to living beings.");
		FORTUNES.add("It is said that the stars favor brave adventurers.");
		FORTUNES.add("Offerings are rewarded at the whim of the spirits.");
		FORTUNES.add("The Phoenix watches over all the denizens of the World.");
		FORTUNES.add("Casting a spell perfectly gives a little more oomph.");
		FORTUNES.add("Many spells are dangerous to use underwater.");
		FORTUNES.add("The spirits lose interest if offerings are too frequent.");
		FORTUNES.add("Spoiler alert.");
		FORTUNES.add("The sacred symbols of the Phoenix are fire and gold.");
		FORTUNES.add("Staves can leak magic if you hit something hard enough.");
		FORTUNES.add("Spirits see mundane items as a request for a similar gift.");
		FORTUNES.add("There's a way to extract azurite without breaking it.");
		FORTUNES.add("Any spirit appreciates a gift of starstone.");
		FORTUNES.add("A phoenix quill is needed for magical writings.");
		FORTUNES.add("Starstone can power up a sky lens.");
		FORTUNES.add("Summoning signifiers always include part of a being's home.");
		FORTUNES.add("A summoning focus usually appeals to the target's interest.");
		FORTUNES.add("An altar can make offerings both easier and more effective.");
	}
}
