package com.sabcancode.dynomem.mixin.fastmap;

import com.google.common.collect.ImmutableMap;
import com.sabcancode.dynomem.ducks.FastMapStateHolder;
import com.sabcancode.dynomem.fastmap.FastMap;
import com.sabcancode.dynomem.common.config.DynoMEMConfig;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Replaces the neighbor lookup Table with a compact FastMap, saving ~600MB of memory.
 * Adapted for MC 1.20.1 which uses Table-based neighbor lookup in State.with().
 */
@Mixin(value = State.class, priority = 900)
public abstract class FastMapStateHolderMixin<O, S> implements FastMapStateHolder<S> {
    @Shadow
    @Final
    private ImmutableMap<Property<?>, Comparable<?>> entries;

    @Unique
    private int dynomem$globalTableIndex;
    @Unique
    private FastMap<S> dynomem$globalTable;

    /**
     * Replace the `with` method to use FastMap instead of neighbor Table lookup.
     * In 1.20.1, State.with() looks up neighbors in a Table<Property, Comparable, S>.
     * We replace that with our compact FastMap.
     */
    @SuppressWarnings("unchecked")
    @Inject(method = "with", at = @At("HEAD"), cancellable = true)
    private <T extends Comparable<T>, V extends T> void redirectWith(
            Property<T> property, V value, CallbackInfoReturnable<Object> cir
    ) {
        if (dynomem$globalTable == null) return; // Not initialized yet

        Comparable<?> current = this.entries.get(property);
        if (current == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this);
        }
        if (current.equals(value)) {
            cir.setReturnValue(this);
            return;
        }

        int propertyIndex = dynomem$globalTable.getPropertyIndex(property);
        if (propertyIndex < 0) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in FastMap");
        }
        int valueIndex = dynomem$globalTable.getValueIndex(property, value);
        if (valueIndex < 0) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on " + this + ", it is not an allowed value");
        }

        S result = dynomem$globalTable.with(dynomem$globalTableIndex, propertyIndex, valueIndex);
        if (result == null) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on " + this);
        }
        cir.setReturnValue(result);
    }

    @Override
    public void dynomem$setStateMap(FastMap<S> stateMap, int tableIndex) {
        dynomem$globalTable = stateMap;
        dynomem$globalTableIndex = tableIndex;
    }
}
