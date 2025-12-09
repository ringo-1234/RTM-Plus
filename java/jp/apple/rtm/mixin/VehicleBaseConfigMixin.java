package jp.apple.rtm.mixin;
import jp.ngt.rtm.modelpack.cfg.VehicleBaseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(value = VehicleBaseConfig.class, remap = false)
public abstract class VehicleBaseConfigMixin {
    @Unique
    public String rollsignTexture2;
    @Unique
    public String[] rollsignNames2;
    @Unique
    public VehicleBaseConfig.Rollsign[] rollsigns2;
    /** 交互切替周期(ms)*/
    @Unique
    public int rollsignAlternateCycle = 3000;
}