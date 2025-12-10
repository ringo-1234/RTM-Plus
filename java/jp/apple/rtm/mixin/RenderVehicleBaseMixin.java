package jp.apple.rtm.mixin;

import org.lwjgl.opengl.GL11;
import jp.ngt.ngtlib.renderer.GLHelper;
import jp.ngt.ngtlib.renderer.NGTTessellator;
import jp.ngt.rtm.entity.train.EntityTrainBase;
import jp.ngt.rtm.entity.train.util.TrainState;
import jp.ngt.rtm.modelpack.cfg.VehicleBaseConfig.Rollsign;
import jp.ngt.rtm.modelpack.modelset.ModelSetVehicleBase;
import net.minecraft.util.ResourceLocation;
import jp.ngt.rtm.entity.vehicle.EntityVehicleBase;
import jp.ngt.rtm.entity.vehicle.RenderVehicleBase;
import net.minecraft.client.Minecraft;
import jp.apple.rtm.RollsignStore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
@Mixin(value = RenderVehicleBase.class, remap = false)
public abstract class RenderVehicleBaseMixin {

    @Overwrite(remap = false)
    protected void renderRollsign(EntityVehicleBase vehicle, ModelSetVehicleBase modelset) {
        GL11.glPushMatrix();

        final ResourceLocation tex2 = RollsignStore.get(modelset);

        boolean useSecond = false;
        if (tex2 != null &&
                modelset.getConfig().rollsigns2 != null &&
                modelset.getConfig().rollsigns2.length > 0) {

            int cycleMs = modelset.getConfig().rollsignAlternateCycle;
            int cycleTicks = cycleMs / 50; // 50ms = 1tick
            if (cycleTicks > 0) {
                long time = vehicle.world.getTotalWorldTime();
                useSecond = (time / cycleTicks) % 2 == 1;
            }
        }

        ResourceLocation texture = useSecond ? tex2 : modelset.rollsignTexture;
        Rollsign[] signs   = useSecond ? modelset.getConfig().rollsigns2   : modelset.getConfig().rollsigns;
        String[] names     = useSecond ? modelset.getConfig().rollsignNames2 : modelset.getConfig().rollsignNames;

        if (texture == null || signs == null || signs.length == 0 || names == null || names.length == 0) {
            GL11.glPopMatrix();
            return;
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        for (Rollsign rollsign : signs) {
            if (!rollsign.disableLighting) {
                GLHelper.disableLighting();
                GLHelper.setLightmapMaxBrightness();
            }

            float f0 = (rollsign.uv[3] - rollsign.uv[2]) / names.length;
            float uMin = rollsign.uv[0], uMax = rollsign.uv[1];

            float f1 = 0.0F;
            if (vehicle instanceof EntityTrainBase) {
                EntityTrainBase train = (EntityTrainBase) vehicle;
                f1 = rollsign.doAnimation
                        ? train.getRollsignAnimation()
                        : train.getVehicleState(TrainState.TrainStateType.Destination);
            }

            float vMin = rollsign.uv[2] + f0 * f1;
            float vMax = rollsign.uv[2] + f0 * (f1 + 1.0F);

            NGTTessellator t = NGTTessellator.instance;
            t.startDrawingQuads();
            for (float[][] pos : rollsign.pos) {
                t.addVertexWithUV(pos[3][0], pos[3][1], pos[3][2], uMin, vMin);
                t.addVertexWithUV(pos[2][0], pos[2][1], pos[2][2], uMin, vMax);
                t.addVertexWithUV(pos[1][0], pos[1][1], pos[1][2], uMax, vMax);
                t.addVertexWithUV(pos[0][0], pos[0][1], pos[0][2], uMax, vMin);
            }
            t.draw();

            if (!rollsign.disableLighting) {
                GLHelper.enableLighting();
            }
        }

        GL11.glPopMatrix();
    }
}