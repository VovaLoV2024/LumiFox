package net.vovawolf.lumifox.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vovawolf.lumifox.entity.LumiFox;
import net.vovawolf.lumifox.registry.ModEntityTypes;

import java.util.List;

/**
 * Предмет яйца призыва для LumiFox
 * Позволяет игрокам призывать умную лисичку Lumi
 */
public class LumiFoxSpawnEggItem extends SpawnEggItem {
    
    // Цвета яйца (основной оранжевый #FF7F00 и вторичный голубой #00BFFF)
    private static final int PRIMARY_COLOR = 0xFF7F00; // Оранжевый цвет
    private static final int SECONDARY_COLOR = 0x00BFFF; // Голубой цвет
    
    /**
     * Конструктор яйца призыва LumiFox
     * @param properties свойства предмета
     */
    public LumiFoxSpawnEggItem(Properties properties) {
        super(ModEntityTypes.LUMI_FOX.get(), PRIMARY_COLOR, SECONDARY_COLOR, properties);
    }
    
    /**
     * Обновление тега сущности перед спавном
     * Устанавливаем лису прирученной по умолчанию
     * @param stack стек предмета
     * @param level уровень
     * @param player игрок
     * @param pos позиция
     * @param spawnType тип спавна
     */
    public void applyEntityTag(ItemStack stack, ServerLevel level, Player player, BlockPos pos, MobSpawnType spawnType) {
        // Создаем тег для установки прирученного состояния
        CompoundTag entityTag = new CompoundTag();
        entityTag.putBoolean("Tamed", true);
        
        // Устанавливаем UUID владельца как текущего игрока
        if (player != null) {
            CompoundTag ownerTag = new CompoundTag();
            ownerTag.putUUID("Owner", player.getUUID());
            entityTag.put("owner", ownerTag);
        }
        
        stack.getOrCreateTag().put("EntityTag", entityTag);
    }
    
    /**
     * Добавление подсказки к предмету
     * @param stack стек предмета
     * @param level уровень
     * @param tooltipComponents список компонентов тултипа
     * @param flag флаг тултипа
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.lumifox.lumi_fox_spawn_egg.desc")
                .withStyle(net.minecraft.ChatFormatting.GOLD));
        tooltipComponents.add(Component.literal("Умная лисичка Lumi - ваш верный друг!"));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}
