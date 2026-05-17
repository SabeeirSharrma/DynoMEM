package com.sabcancode.dynomem.ducks;

/**
 * An object that can be acquired and released similar to the vanilla ThreadingDetector/LockHelper.
 * Methods must only be called while synchronized on this object.
 */
public interface SmallThreadDetectable {
    byte UNLOCKED = 0;
    byte LOCKED = 1;
    byte CRASHING = 2;

    byte dynomem$getState();

    void dynomem$setState(byte newState);
}
