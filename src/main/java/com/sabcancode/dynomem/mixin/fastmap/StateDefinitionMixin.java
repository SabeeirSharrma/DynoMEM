package com.sabcancode.dynomem.mixin.fastmap;

import com.sabcancode.dynomem.impl.FastMapStateHolderImpl;
import net.minecraft.state.State;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

/**
 * Hooks into StateManager to initialize FastMap after states are created.
 * In 1.20.1, StateManager builds all states in its constructor and then calls
 * State.createWithTable on each state.
 */
@Mixin(StateManager.class)
public class StateDefinitionMixin<O, S extends State<O, S>> {

    /**
     * After the StateManager constructor finishes building all states and their neighbor tables,
     * we initialize our FastMap which replaces the neighbor table lookup.
     */
    @Inject(
            method = "<init>(Ljava/util/function/Function;Ljava/lang/Object;Lnet/minecraft/state/StateManager$Factory;Ljava/util/Map;)V",
            at = @At("TAIL")
    )
    private void initFastMapAfterConstruction(
            java.util.function.Function<O, S> defaultStateGetter, O owner,
            StateManager.Factory<O, S> factory,
            Map<String, Property<?>> propertiesByName,
            CallbackInfo ci
    ) {
        // Access the states field from the StateManager
        StateManager<O, S> self = (StateManager<O, S>) (Object) this;
        FastMapStateHolderImpl.initializeFastMap(self.getStates());
    }
}
