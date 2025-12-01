package com.pluralcraft.items;

import com.pluralcraft.PluralCraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry for all PluralCraft items
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PluralCraft.MOD_ID);

    // The ID card/passport item
    public static final RegistryObject<Item> SYSTEM_ID = ITEMS.register("system_id",
            () -> new SystemIDItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        PluralCraft.LOGGER.info("Registered PluralCraft items!");
    }
}
