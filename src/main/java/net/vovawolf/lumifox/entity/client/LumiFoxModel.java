package net.vovawolf.lumifox.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vovawolf.lumifox.LumiFox;
import net.vovawolf.lumifox.entity.LumiFox;

/**
 * Модель для умной лисички Lumi
 * Использует стандартную модель лисы из Minecraft
 */
@OnlyIn(Dist.CLIENT)
public class LumiFoxModel<T extends LumiFox> extends FoxModel<T> {
    
    // Основной слой модели
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(LumiFox.MODID, "lumi_fox"), "main");
    
    /**
     * Конструктор модели
     * @param root корневая часть модели
     */
    public LumiFoxModel(ModelPart root) {
        super(root);
    }
    
    /**
     * Создание определения слоя модели
     * Возвращает построитель слоя с геометрией лисы
     * @return LayerDefinition
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = FoxModel.createBodyLayer();
        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
