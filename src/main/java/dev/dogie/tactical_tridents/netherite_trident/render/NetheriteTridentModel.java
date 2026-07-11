package dev.dogie.tactical_tridents.netherite_trident.render;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.dogie.tactical_tridents.TacticalTridents;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

// i am never changing the model again cus wtf did i even do to make this work

// I changed this model :3 - Krystal

public class NetheriteTridentModel extends Model {
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TacticalTridents.MODID, "textures/entity/netherite_trident.png");

	private final ModelPart trident;
	private final ModelPart body;

	public NetheriteTridentModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.trident = root.getChild("trident");
		this.body = this.trident.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition trident = partdefinition.addOrReplaceChild("trident", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -15.0F, -0.5F, 1.0F, 30.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 18).addBox(2.5F, -14.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 18).addBox(-3.5F, -14.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 0.0F));

		PartDefinition body = trident.addOrReplaceChild("body", CubeListBuilder.create().texOffs(4, 23).addBox(0.5F, -11.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 23).addBox(-1.5F, -11.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 16).addBox(-2.5F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 16).addBox(1.5F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 26).addBox(-3.5F, -11.0F, 0.0F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i1, int i2, int i3) {
		trident.render(poseStack, vertexConsumer, i1, i2, i3);
	}
}
