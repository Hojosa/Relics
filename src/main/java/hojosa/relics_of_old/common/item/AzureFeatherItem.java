package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.init.RelicsItems;
import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.common.mana.IMana;
import hojosa.relics_of_old.common.player.PlayerGlideData;
import hojosa.relics_of_old.common.player.PlayerMana;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AzureFeatherItem extends RelicsItem implements IMana {

	public static final float FEATHER_BOOST = 16.0f;

	public AzureFeatherItem() {
		super(64, Rarity.RARE);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		// must be mid-air (LG2: player.onGround check)
		if (player.onGround())
			return InteractionResultHolder.pass(stack);

		PlayerGlideData glide = PlayerGlideData.get(player);
		if (glide == null)
			return InteractionResultHolder.pass(stack);

		// check for mantle and mana
		boolean hasAzure = RelicsItems.AZURE_MANTLE.get().isEquipped(player);
		boolean hasPhoenix = RelicsItems.PHOENIX_MANTLE.get().isEquipped(player);
		if (!hasAzure && !hasPhoenix)
			return InteractionResultHolder.pass(stack);

		PlayerMana mana = PlayerMana.get(player);
		if (mana == null || mana.getAvailableMana() <= 0.0f)
			return InteractionResultHolder.pass(stack);

		boolean used = false;
		float ratio = hasPhoenix ? 7.0f : 4.0f;

		if (glide.isGliding()) {
			// already gliding — add boost to ceiling
			glide.setGlideCharge(glide.getGlideCharge() + FEATHER_BOOST);
			used = true;
		} else {
			// starting glide — need fallDistance > 23 (azure) or phoenix equipped
			if (glide.getSkylensTagCharge() > 0 || hasPhoenix || player.fallDistance > 23.0f) {
				glide.setGlideCharge((float) player.getY() + FEATHER_BOOST + player.fallDistance);
				glide.setGlideRatio(ratio);
				used = true;
			}
		}
		if (used) {
			if (!player.isCreative()) {
				stack.shrink(1);
			}

			// velocity push in look direction (LG2: 0.25 boost)
			Vec3 look = player.getLookAngle();
			player.setDeltaMovement(player.getDeltaMovement().add(look.scale(0.25)));

			// sounds (LG2: feather + whirlwind at pitch 1.5)
			level.playSound(null, player.blockPosition(), RelicsSounds.FEATHER.get(), SoundSource.PLAYERS, 0.3f, 1.0f);
			level.playSound(null, player.blockPosition(), RelicsSounds.WHIRLWIND.get(), SoundSource.PLAYERS, 0.3f, 1.5f);

			// mana cost
			if (mana != null && !player.isCreative()) {
				mana.expendMana(player, getManaCost());
			}

			return InteractionResultHolder.success(stack);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public float getManaCost() {
		return 12.0f;
	}
}