package dev.kvnmtz.createmobspawners.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.kvnmtz.createmobspawners.CreateMobSpawners;
import dev.kvnmtz.createmobspawners.item.registry.ModItems;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulCatcherRenderer extends CustomRenderedItemModelRenderer {
    protected static final PartialModel GEAR = PartialModel.of(CreateMobSpawners.asResource("item/soul_catcher/gear"));
    protected static final PartialModel GEAR_EMPTY = PartialModel.of(CreateMobSpawners.asResource("item/soul_catcher/gear_empty"));

    @Override
    protected void render(ItemStack itemStack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext itemDisplayContext, PoseStack ms, MultiBufferSource multiBufferSource, int light, int overlay) {
        var isEmpty = itemStack.getItem() == ModItems.EMPTY_SOUL_CATCHER.get();

        renderer.render(model.getOriginalModel(), light);

        if (isEmpty) {
            // X pivot is already 8
            var yOffset = -2.6515/16f;
            var zOffset = 1.6515/16f;

            // Y & Z pivots need to be centered (8) in order to rotate around X
            ms.translate(0, -yOffset, -zOffset);
            ms.mulPose(Axis.XP.rotationDegrees(45));
            ms.translate(0, yOffset, zOffset);

            renderer.render(GEAR_EMPTY.get(), light);
        } else {
            // X pivot is already 8
            var yOffset = 3.5/16f;

            // X & Y pivots need to be centered (8) in order to rotate around Z
            ms.translate(0, -yOffset, 0);
            float worldTime = AnimationTickHolder.getRenderTime() / 10;
            float angle = worldTime * 25;
            ms.mulPose(Axis.ZP.rotationDegrees(angle));
            ms.translate(0, yOffset, 0);

            renderer.render(GEAR.get(), light);
        }
    }
}
