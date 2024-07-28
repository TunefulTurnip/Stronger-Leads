package com.tunefulturnip.strongerleads.item;

import com.tunefulturnip.strongerleads.StrongerLeadsMod;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class StrongerLeadsItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(StrongerLeadsMod.MODID);
    public static final DeferredItem<StrongerLeadItem> LONGER_LEAD = ITEMS.register("longer_lead", ()-> new StrongerLeadItem(new Item.Properties()));
}
