package net.Rampage.mob_block_farming.util;


public interface IHarvester {
    int getFoodPointCost();
    // Return true if can and has accepted output. Return false if can not and will not produce output (inventory full, etc.)
    boolean acceptHarvesterOutput(MobBlockType mobBlockType);
}
