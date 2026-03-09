package net.Rampage.mob_block_farming.sound;

import net.Rampage.mob_block_farming.block.entity.custom.BlenderBlockEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class BlenderLoopingSound extends AbstractTickableSoundInstance {
    private final BlenderBlockEntity blenderBlockEntity;

    public BlenderLoopingSound(BlenderBlockEntity blenderBlockEntity) {
        super(ModSounds.BLENDER_LOOP.get(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());

        this.blenderBlockEntity = blenderBlockEntity;
        this.looping = true;
        this.delay = 0;
        this.x = blenderBlockEntity.getBlockPos().getX() + 0.5f;
        this.y = blenderBlockEntity.getBlockPos().getY() + 0.5f;
        this.z = blenderBlockEntity.getBlockPos().getZ() + 0.5f;
    }

    @Override
    public void tick() {
        if (blenderBlockEntity.isRemoved()) {
            this.stop();
        }
    }
}
