package net.vovawolf.lumifox.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.common.Tags;

/**
 * Класс умной лисички Lumi, наследуется от ванильной лисы
 * Lumi - это дружелюбная и пассивная лиса, которая следует за игроком с сладкими ягодами
 */
public class LumiFox extends Fox {
    
    // Конструктор для создания сущности LumiFox
    public LumiFox(EntityType<? extends Fox> entityType, Level level) {
        super(entityType, level);
    }
    
    /**
     * Регистрация целей ИИ для LumiFox
     * Переопределяем методы родителя, чтобы сделать лису более дружелюбной
     */
    @Override
    protected void registerGoals() {
        // Приоритетные цели
        this.goalSelector.addGoal(0, new FloatGoal(this)); // Плавание
        
        // Следование за игроком с сладкими ягодами
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.0D, Items.SWEET_BERRIES, false));
        
        // Повседневные цели
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F)); // Смотреть на игрока
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8D)); // Случайное перемещение
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D)); // Избегание воды
    }
    
    /**
     * Создание атрибутов для LumiFox
     * @return поставщик атрибутов
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D) // Здоровье как у обычной лисы
                .add(Attributes.MOVEMENT_SPEED, 0.3D) // Скорость передвижения
                .add(Attributes.ATTACK_DAMAGE, 2.0D) // Урон атаки
                .add(Attributes.FOLLOW_RANGE, 32.0D); // Дальность следования
    }
    
    /**
     * Обработка взаимодействия с игроком
     * @param player игрок
     * @param hand рука
     * @return результат взаимодействия
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        // Если игрок держит сладкие ягоды, можно покормить лису
        if (itemstack.is(Items.SWEET_BERRIES)) {
            if (!this.level().isClientSide) {
                this.heal(2.0F); // Лечим лису
                if (!player.isCreative()) {
                    itemstack.shrink(1); // Уменьшаем количество ягод
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        
        return super.mobInteract(player, hand);
    }
    
    /**
     * Определение, может ли лиса быть размножена
     * @param itemStack предмет для проверки
     * @return true если предмет подходит для размножения
     */
    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.SWEET_BERRIES);
    }
    
    /**
     * Звук AMBIENT (окружающие звуки)
     * @return SoundEvent
     */
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.FOX_AMBIENT;
    }
    
    /**
     * Звук при получении урона
     * @param damageSource источник урона
     * @return SoundEvent
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.FOX_HURT;
    }
}
