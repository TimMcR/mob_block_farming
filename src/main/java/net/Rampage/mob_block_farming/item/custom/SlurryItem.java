package net.Rampage.mob_block_farming.item.custom;

import net.Rampage.mob_block_farming.util.SlurryType;
import net.minecraft.world.item.Item;

public class SlurryItem extends Item {
    private final SlurryType slurryType;

    public SlurryItem(Properties pProperties, SlurryType slurryType) {
        super(pProperties);
        this.slurryType = slurryType;
    }

    public SlurryType getSlurryType(){
        return slurryType;
    }
}
