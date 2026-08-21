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
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
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
 * Lumi - это прирученная и дружелюбная лиса с особым поведением
 */
public class LumiFox extends Fox {
    
    // Конструктор для создания сущности LumiFox
    public LumiFox(EntityType<? extends Fox> entityType, Level level) {
        super(entityType, level);
        // Устанавливаем лису прирученной по умолчанию
        setTamed(true);
    }
    
    /**
     * Регистрация целей ИИ для LumiFox
     * Переопределяем методы родителя, чтобы сделать лису более дружелюбной
     */
    @Override
    protected void registerGoals() {
        // Приоритетные цели
        this.goalSelector.addGoal(0, new FloatGoal(this)); // Плавание
        
        // Цели взаимодействия с игроком
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, true)); // Следование за владельцем
        
        // Боевые цели
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true)); // Атака в ближнем бою
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this)); // Атака тех, кто ранил владельца
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this)); // Атака тех, кого ранил владелец
        
        // Повседневные цели
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F)); // Смотреть на игрока
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8D)); // Случайное перемещение
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D)); // Избегание воды
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
        
        // Если лиса не имеет владельца, устанавливаем текущего игрока
        if (!this.isOwnedBy(player)) {
            if (!this.level().isClientSide) {
                this.setOwnerUUID(player.getUUID());
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        
        return super.mobInteract(player, hand);
    }
    
    /**
     * Проверка, принадлежит ли лиса игроку
     * @param player игрок для проверки
     * @return true если лиса принадлежит игроку
     */
    public boolean isOwnedBy(Player player) {
        return this.getOwnerUUID() != null && this.getOwnerUUID().equals(player.getUUID());
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
     * Создание потомства LumiFox
     * @param animal родительское животное
     * @return новая сущность LumiFox
     */
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, Animal animal) {
        LumiFox lumiFox = (LumiFox) EntityType.FOX.create(serverLevel);
        if (lumiFox != null) {
            lumiFox.setTamed(true); // Потомство тоже прирученное
            if (this.getOwnerUUID() != null) {
                lumiFox.setOwnerUUID(this.getOwnerUUID()); // Наследует владельца
            }
        }
        return lumiFox;
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
    
    /**
     * Звук смерти
     * @return SoundEvent
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }
    
    /**
     * Сохранение дополнительных данных NBT
     * @param compound тег для записи данных
     */
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        // Можно добавить дополнительные данные здесь
    }
    
    /**
     * Загрузка дополнительных данных NBT
     * @param compound тег с данными
     */
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        // Загружаем дополнительные данные здесь
    }
}
