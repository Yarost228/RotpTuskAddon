package com.rotpaddon.exampleaddon.init;

import com.rotpaddon.exampleaddon.AddonMain;
import com.rotpaddon.exampleaddon.item.TeaItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.standobyte.jojo.init.ModItems.MAIN_TAB;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AddonMain.MOD_ID);

    public static final RegistryObject<TeaItem> TEA = ITEMS.register("tea_bottle",
            () -> new TeaItem(new Item.Properties().tab(MAIN_TAB).stacksTo(16)));
}
