package com.tunefulturnip.strongerleads;

import com.tunefulturnip.strongerleads.item.StrongerLeadsItems;
import com.tunefulturnip.strongerleads.item.component.LeadRecord;
import com.tunefulturnip.strongerleads.item.component.StrongerLeadsDataComponents;
import com.tunefulturnip.strongerleads.recipe.StrongerLeadsRecipeSerializer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(StrongerLeadsMod.MODID)
public class StrongerLeadsMod {
    public static final String MODID = "stronger_leads";

    public StrongerLeadsMod(IEventBus modEventBus, ModContainer modContainer) {
        StrongerLeadsAttachment.ATTACHMENT_TYPES.register(modEventBus);
        StrongerLeadsDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        StrongerLeadsRecipeSerializer.RECIPE_SERIALIZERS.register(modEventBus);
        StrongerLeadsItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::buildCreativeModeTabContents);

        modContainer.registerConfig(ModConfig.Type.COMMON, StrongerLeadsConfig.COMMON_SPEC);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(StrongerLeadsMod::registerItemModelProperties);
    }

    public static void registerItemModelProperties() {
        ItemProperties.register(StrongerLeadsItems.LONGER_LEAD.get(), ResourceLocation.fromNamespaceAndPath(MODID, "length"),
                (stack, world, living, i) -> {
                    LeadRecord record = stack.get(StrongerLeadsDataComponents.LEAD);
                    if (record != null)
                        return record.length();
                    else return 0;
                });
        ItemProperties.register(StrongerLeadsItems.LONGER_LEAD.get(), ResourceLocation.fromNamespaceAndPath(MODID, "strength"),
                (stack, world, living, i) -> {
                    LeadRecord record = stack.get(StrongerLeadsDataComponents.LEAD);
                    if (record != null)
                        return record.strength();
                    else return 0;
                });
    }

    public void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.insertAfter(new ItemStack(Items.NAME_TAG), createCreativeModStack(0,-1), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NAME_TAG), createCreativeModStack(0,-2), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NAME_TAG), createCreativeModStack(0,-3), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(new ItemStack(Items.LEAD), createCreativeModStack(0,4), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.LEAD), createCreativeModStack(0,3), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.LEAD), createCreativeModStack(0,2), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.LEAD), createCreativeModStack(0,1), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.LEAD), createCreativeModStack(1,0), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    public static ItemStack createCreativeModStack(int strength, int length) {
        ItemStack stack = new ItemStack(StrongerLeadsItems.LONGER_LEAD.get());
        stack.set(StrongerLeadsDataComponents.LEAD, new LeadRecord((byte) strength, (byte) length));
        return stack;
    }
}
