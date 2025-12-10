package jp.apple.rtm;

import jp.ngt.rtm.modelpack.modelset.ModelSetVehicleBase;
import net.minecraft.util.ResourceLocation;

import java.util.WeakHashMap;

public final class RollsignStore {
    private static final WeakHashMap<ModelSetVehicleBase<?>, ResourceLocation> MAP = new WeakHashMap<>();
    private RollsignStore() {}
    public static void put(ModelSetVehicleBase<?> self, ResourceLocation tex) { MAP.put(self, tex); }
    public static ResourceLocation get(ModelSetVehicleBase<?> self) { return MAP.get(self); }
}