package com.sabcancode.dynomem.ducks;

import com.sabcancode.dynomem.fastmap.FastMap;

public interface FastMapStateHolder<S> {
    void dynomem$setStateMap(FastMap<S> stateMap, int tableIndex);
}
