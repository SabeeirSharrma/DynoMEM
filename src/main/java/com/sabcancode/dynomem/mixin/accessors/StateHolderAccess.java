package com.sabcancode.dynomem.mixin.accessors;

import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.common.collect.ImmutableMap;

@Mixin(State.class)
public interface StateHolderAccess {
    @Accessor("entries")
    ImmutableMap<Property<?>, Comparable<?>> getEntries();
}
