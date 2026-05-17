package com.sabcancode.dynomem.impl;

import com.sabcancode.dynomem.ducks.FastMapStateHolder;
import com.sabcancode.dynomem.fastmap.FastMap;
import com.sabcancode.dynomem.common.config.DynoMEMConfig;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;

import java.util.Collection;

public class FastMapStateHolderImpl {
    @SuppressWarnings("unchecked")
    public static <O, S extends State<O, S>>
    void initializeFastMap(Collection<S> states) {
        S someState = states.iterator().next();
        Collection<Property<?>> properties = someState.getProperties();
        FastMap<S> mainMap = new FastMap<>(properties, DynoMEMConfig.COMPACT_FAST_MAP.isEnabled());
        for (var entry : states) {
            final int index = mainMap.insertAtIndex(entry, (state, prop) -> state.get(prop));
            ((FastMapStateHolder<S>) entry).dynomem$setStateMap(mainMap, index);
        }
    }

    public static Comparable<?> getPropertyValue(
            FastMap<?> fastMap, int fastMapIndex, int propertyIndex
    ) {
        return fastMap.getValueAt(fastMapIndex, propertyIndex);
    }
}
