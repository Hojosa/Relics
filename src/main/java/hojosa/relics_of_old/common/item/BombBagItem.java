package hojosa.relics_of_old.common.item;

import java.util.List;

import hojosa.relics_of_old.common.entity.BombEntity;
import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class BombBagItem extends RelicsItem {

	public static final int MAX_BOMBS = 50;
	private static final String TAG_BOMBS = "BombCount";

	public BombBagItem() {
		super(new Properties().stacksTo(1));
	}

	public static int getBombCount(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		return tag != null ? tag.getInt(TAG_BOMBS) : 0;
	}

	public static void setBombCount(ItemStack stack, int count) {
		stack.getOrCreateTag().putInt(TAG_BOMBS, Math.max(0, Math.min(count, MAX_BOMBS)));
	}

	public static boolean isBagFull(ItemStack stack) {
		return getBombCount(stack) == MAX_BOMBS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int count = getBombCount(stack);

		if (count <= 0 && !player.getAbilities().instabuild) {
			return InteractionResultHolder.fail(stack);
		}

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));

		if (!level.isClientSide) {
			BombEntity bomb = new BombEntity(level, player, BombEntity.LONG_FUSE_TIME);
			level.addFreshEntity(bomb);
		}

		if (!player.getAbilities().instabuild) {
			setBombCount(stack, count - 1);
		}

		player.swing(hand);
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (entity instanceof BombEntity bomb) {
			bomb.fizzle();
			return true;
		}
		return false;
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack bag, Slot slot, ClickAction action, Player player) {
		if (bag.getCount() != 1)
			return false;
		System.out.println(action);
		System.out.println(player.isShiftKeyDown());
		int count = getBombCount(bag);

		if (action == ClickAction.SECONDARY && count > 0) {
			// right click bag on empty slot -> extract 1 bomb
			if (slot.getItem().isEmpty()) {
				slot.safeInsert(new ItemStack(RelicsItems.BOMB.get(), 1));
				setBombCount(bag, count - 1);
				player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 1.0f);
				return true;
			} else if (slot.getItem().getItem() == RelicsItems.BOMB.get() && slot.getItem().getCount() < slot.getItem().getMaxStackSize()) {
				// extract 1 more bomb to existing bomb stack
				slot.getItem().grow(1);
				setBombCount(bag, count - 1);
				player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 1.0f);
				return true;
			}
		}

		// shift right click (PRIMARY) on bomb in a slot -> fill bomb stack up to max
		if (action == ClickAction.PRIMARY && slot.getItem().getItem() == RelicsItems.BOMB.get() && count > 0) {
			int slotCount = slot.getItem().getCount();
			int maxStack = slot.getItem().getMaxStackSize();
			int space = maxStack - slotCount;
			int toExtract = Math.min(count, space);
			if (toExtract > 0) {
				slot.getItem().grow(toExtract);
				setBombCount(bag, count - toExtract);
				player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack bag, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (bag.getCount() != 1)
			return false;
		if (action != ClickAction.SECONDARY || !slot.allowModification(player))
			return false;

		int count = getBombCount(bag);

		// right click bombs onto the bag -> insert
		if (other.getItem() == RelicsItems.BOMB.get()) {
			int space = MAX_BOMBS - count;
			int toInsert = Math.min(other.getCount(), space);
			if (toInsert > 0) {
				other.shrink(toInsert);
				setBombCount(bag, count + toInsert);
				player.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 1.0f);
				return true;
			}
		}

		// right click empty cursor on bag -> extract 1 bomb to cursor
		if (other.isEmpty() && count > 0) {
			access.set(new ItemStack(RelicsItems.BOMB.get(), 1));
			setBombCount(bag, count - 1);
			player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 1.0f);
			return true;
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		int count = getBombCount(stack);
		tooltip.add(Component.translatable("item.relics_of_old.bomb_bag.count", count, MAX_BOMBS).withStyle(count >= MAX_BOMBS ? ChatFormatting.YELLOW : ChatFormatting.GRAY));
	}
}