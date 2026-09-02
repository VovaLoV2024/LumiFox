package net.vovawolf.lumifox.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vovawolf.lumifox.entity.LumiFox;

/**
 * Рендерер для умной лисички Lumi
 * Использует ванильные текстуры лисы
 */
@OnlyIn(Dist.CLIENT)
public class LumiFoxRenderer extends MobRenderer<LumiFox, FoxModel<LumiFox>> {
    
    // Текстуры ванильных лис (красная и снежная)
    private static final ResourceLocation RED_FOX_LOCATION = new ResourceLocation("textures/entity/fox/fox.png");
    private static final ResourceLocation SNOW_FOX_LOCATION = new ResourceLocation("textures/entity/fox/fox_sleep.png");
    
    /**
     * Конструктор рендерера
     * @param context контекст рендерера сущностей
     */
    public LumiFoxRenderer(EntityRendererProvider.Context context) {
        super(context, new FoxModel<>(context.bakeLayer(LumiFoxModel.LAYER_LOCATION)), 0.4F);
    }
    
    /**
     * Получение текстуры для лисы
     * В зависимости от типа лисы возвращаем соответствующую текстуру
     * @param entity сущность лисы
     * @return ResourceLocation текстуры
     */
    @Override
    public ResourceLocation getTextureLocation(LumiFox entity) {
        // Используем красную текстуру по умолчанию
        // Можно добавить логику для выбора между красной и снежной текстурой
        return RED_FOX_LOCATION;
    }
}
