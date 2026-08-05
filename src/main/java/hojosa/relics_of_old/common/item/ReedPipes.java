package hojosa.relics_of_old.common.item;

import hojosa.relics_of_old.common.init.RelicsSounds;
import hojosa.relics_of_old.lib.item.RelicsItem;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ReedPipes extends RelicsItem {
	
    private static final int[] NOTES = {0, 3, 7, 9, 12};
    private static final int[] ALT_NOTES = {-1, 2, 5, 8, 11};

    public ReedPipes() {
        super(1);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        int note = getNoteFromPitch(player, player.isCrouching() ? ALT_NOTES : NOTES) + 1;
        if (!level.isClientSide) {
            level.playSound(null, player.blockPosition(),
                    RelicsSounds.FLUTE_PLAY.get(), SoundSource.PLAYERS,
                    1.0f, getNotePitch(note));
        }
        stack.getOrCreateTag().putInt("fluteNote", note);
        stack.getOrCreateTag().putFloat("noteTime", 0.0f);

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!(entity instanceof Player player)) return;

        float noteTime = stack.getOrCreateTag().getFloat("noteTime");
        int savedNote = stack.getOrCreateTag().getInt("fluteNote");

        int currentNote = getNoteFromPitch(player, player.isCrouching() ? ALT_NOTES : NOTES) + 1;
        float pitch = getNotePitch(currentNote);

        // note changed — play attack sound
        if (currentNote != savedNote) {
            stack.getOrCreateTag().putInt("fluteNote", currentNote);
            noteTime = 0.0f;
            if (!level.isClientSide) {
                level.playSound(null, player.blockPosition(),
                        RelicsSounds.FLUTE_PLAY.get(), SoundSource.PLAYERS,
                        1.0f, pitch);
            }
        } else {
            noteTime += pitch;
        }

        // sustain loop
        if (noteTime >= 5.0f) {
            noteTime -= 5.0f;
            if (!level.isClientSide) {
                level.playSound(null, player.blockPosition(),
						RelicsSounds.FLUTE_SUSTAIN.get(), SoundSource.PLAYERS,
                        1.0f, pitch);
            }
        }
        stack.getOrCreateTag().putFloat("noteTime", noteTime);
    }
    
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        stack.getOrCreateTag().putFloat("noteTime", 0.0f);
    }

    // maps player look pitch (-90 to 90) to a note index in the given scale
    private static int getNoteFromPitch(Player player, int[] noteArray) {
        double span = (180.0f - (player.getXRot() + 90.0f)) / 180.0f;
        int index = (int) (span * (noteArray.length - 1) + 0.5);
        index = Math.max(0, Math.min(index, noteArray.length - 1));
        return noteArray[index];
    }

    private static float getNotePitch(int note) {
        return (float) Math.pow(2.0, (double) (note - 12) / 12.0);
    }   
}