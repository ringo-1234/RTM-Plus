package jp.apple.rtm.mixin;

import jp.apple.rtm.RollsignStore;
import jp.ngt.rtm.modelpack.ModelPackManager;
import jp.ngt.rtm.modelpack.cfg.VehicleBaseConfig;
import jp.ngt.rtm.modelpack.modelset.ModelSetVehicleBase;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelSetVehicleBase.class, remap = false, priority = 2000)
public abstract class ModelSetVehicleBaseMixin {

    @Inject(method = "constructOnClient", at = @At("TAIL"), remap = false)
    private void injectConstructOnClient(CallbackInfo ci) {
        ModelSetVehicleBase<?> self = (ModelSetVehicleBase<?>)(Object)this;
        VehicleBaseConfig cfg = self.getConfig();

        ResourceLocation tex2 = null;
        if (cfg.rollsignTexture2 != null) {
            String path = cfg.rollsignTexture2.toString();
            if (!path.isEmpty()) tex2 = ModelPackManager.INSTANCE.getResource(path);
        }
        RollsignStore.put(self, tex2);
    }
}