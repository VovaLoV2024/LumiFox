package net.vovawolf.lumifox.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vovawolf.lumifox.LumiMod;
import net.vovawolf.lumifox.entity.LumiFox;
import net.vovawolf.lumifox.item.LumiFoxSpawnEggItem;

/**
 * Класс регистрации предметов мода LumiFox
 * Использует DeferredRegister для регистрации всех предметов
 */
public class ModItems {
    
    // Deferred Register для предметов
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LumiMod.MODID);
    
    // Яйцо призыва LumiFox
    public static final DeferredHolder<Item, LumiFoxSpawnEggItem> LUMI_FOX_SPAWN_EGG = ITEMS.register(
            "lumi_fox_spawn_egg",
            () -> new LumiFoxSpawnEggItem(new Item.Properties().stacksTo(64))
    );
}
