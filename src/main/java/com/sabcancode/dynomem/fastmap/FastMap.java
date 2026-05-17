package com.sabcancode.dynomem.fastmap;

import com.google.common.collect.ImmutableList;
import net.minecraft.state.property.Property;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Maps a Property->Value assignment to a value, while allowing fast access to "neighbor" states.
 * Adapted for MC 1.20.1 which doesn't have Property.getInternalIndex().
 */
public class FastMap<Value> {
    private final List<Property<?>> properties;
    private final List<FastMapKey> keys;
    private final List<Value> valueMatrix;
    // In 1.20.1, Property doesn't have getInternalIndex(), so we build our own index maps
    private final Map<Property<?>, List<? extends Comparable<?>>> propertyValues;
    private final Map<Property<?>, Integer> propertyIndices;

    public FastMap(Collection<Property<?>> properties, boolean compact) {
        this.properties = ImmutableList.copyOf(properties);
        this.propertyValues = new LinkedHashMap<>();
        this.propertyIndices = new LinkedHashMap<>();

        int idx = 0;
        for (Property<?> prop : this.properties) {
            List<? extends Comparable<?>> values = ImmutableList.copyOf(prop.getValues());
            this.propertyValues.put(prop, values);
            this.propertyIndices.put(prop, idx++);
        }

        List<FastMapKey> keyList = new ArrayList<>(this.properties.size());
        int factorUpTo = 1;
        for (Property<?> prop : this.properties) {
            int numValues = this.propertyValues.get(prop).size();
            FastMapKey nextKey;
            if (compact) {
                nextKey = new CompactFastMapKey(factorUpTo, numValues);
            } else {
                nextKey = BinaryFastMapKey.create(factorUpTo, numValues);
            }
            keyList.add(nextKey);
            factorUpTo *= nextKey.getFactorToNext();
        }
        this.keys = ImmutableList.copyOf(keyList);

        List<Value> valuesList = new ArrayList<>(factorUpTo);
        for (int i = 0; i < factorUpTo; ++i) {
            valuesList.add(null);
        }
        this.valueMatrix = valuesList;
    }

    /**
     * Get the internal index of a value within its property's possible values list.
     */
    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> int getValueIndex(Property<T> property, T value) {
        List<? extends Comparable<?>> values = propertyValues.get(property);
        if (values == null) return -1;
        return values.indexOf(value);
    }

    /**
     * Get the property index (position in our property list).
     */
    public int getPropertyIndex(Property<?> property) {
        Integer idx = propertyIndices.get(property);
        return idx != null ? idx : -1;
    }

    /**
     * Computes the value for a neighbor state
     */
    @Nullable
    public Value with(int oldIndex, int propertyIndex, int valueIndex) {
        int newIndex = keys.get(propertyIndex).replaceIn(oldIndex, valueIndex);
        if (newIndex < 0 || valueMatrix.get(newIndex) == null) {
            return null;
        }
        return valueMatrix.get(newIndex);
    }

    public <V> int getIndexOf(Value state, PropertyValueGetter<Value> getValue) {
        int id = 0;
        for (int i = 0; i < this.properties.size(); ++i) {
            Property<?> prop = this.properties.get(i);
            Comparable<?> val = getValue.getValue(state, prop);
            int valueIndex = propertyValues.get(prop).indexOf(val);
            id += keys.get(i).toPartialMapIndex(valueIndex);
        }
        return id;
    }

    public int insertAtIndex(Value state, PropertyValueGetter<Value> getValue) {
        final int index = getIndexOf(state, getValue);
        if (this.valueMatrix.get(index) != null) {
            throw new IllegalStateException("Duplicate state at index " + index);
        }
        this.valueMatrix.set(index, state);
        return index;
    }

    public int getValueIndexFromState(int stateIndex, int propertyIndex) {
        return this.keys.get(propertyIndex).getIndexIn(stateIndex);
    }

    public List<Property<?>> getProperties() {
        return properties;
    }

    public Comparable<?> getValueAt(int stateIndex, int propertyIndex) {
        int internalIndex = getValueIndexFromState(stateIndex, propertyIndex);
        Property<?> prop = properties.get(propertyIndex);
        return propertyValues.get(prop).get(internalIndex);
    }

    public interface PropertyValueGetter<Owner> {
        Comparable<?> getValue(Owner state, Property<?> property);
    }
}
