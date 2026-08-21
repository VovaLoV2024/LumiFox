package net.vovawolf.lumifox.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vovawolf.lumifox.LumiFox;
import net.vovawolf.lumifox.entity.LumiFox;

/**
 * Класс регистрации сущностей мода LumiFox
 * Использует DeferredRegister для регистрации всех сущностей
 */
public class ModEntityTypes {
    
    // Deferred Register для сущностей
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(EntityType.class, LumiFox.MODID);
    
    /**
     * Регистрация типа сущности LumiFox
     * Используем ванильный EntityType.FOX как основу
     * Категория: CREATURE (животное)
     */
    public static final DeferredHolder<EntityType<?>, EntityType<LumiFox>> LUMI_FOX = ENTITY_TYPES.register(
            "lumi_fox",
            () -> EntityType.Builder.of(LumiFox::new, MobCategory.CREATURE)
                    .sized(0.6F, 0.7F) // Размер как у обычной лисы
                    .clientTrackingRange(8) // Дальность отслеживания клиентом
                    .build(LumiFox.MODID + ":lumi_fox")
    );
}
