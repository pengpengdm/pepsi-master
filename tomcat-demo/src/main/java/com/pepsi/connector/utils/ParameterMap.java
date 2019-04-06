package com.pepsi.connector.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/22
 * describe:
 */
public class ParameterMap extends HashMap{

    private static final long serialVersionUID = 1762003831820295706L;

    public ParameterMap() {
        super();
    }

    public ParameterMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ParameterMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ParameterMap(Map map) {
        super(map);
    }

    /**
     * The current lock state of this parameter map.
     */
    private boolean locked = false;

    /**
     * Return the locked state of this parameter map.
     */
    public boolean isLocked() {
        return (this.locked);
    }


    /**
     * Set the locked state of this parameter map.
     *
     * @param locked The new locked state
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void clear() {

        if (locked)
            throw new IllegalStateException("parameterMap.locked");
        super.clear();

    }

    public Object put(Object key, Object value) {

        if (locked)
            throw new IllegalStateException("parameterMap.locked");
        return (super.put(key, value));

    }

    public void putAll(Map map) {

        if (locked)
            throw new IllegalStateException("parameterMap.locked");
        super.putAll(map);

    }

    public Object remove(Object key) {

        if (locked)
            throw new IllegalStateException("parameterMap.locked");
        return (super.remove(key));

    }
}
