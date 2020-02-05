package com.telek.hemsipc.context;

import java.util.HashMap;
import java.util.Map;

public class IoSession {
	private Map<Object, Object> sessionAttributeMap = new HashMap<Object, Object>();
	
	/**
     * Returns the value of the user-defined attribute of this session.
     *
     * @param key the key of the attribute
     * @return <tt>null</tt> if there is no attribute with the specified key
     */
    public Object getAttribute(Object key) {
    	return this.sessionAttributeMap.get(key);
    }

    /**
     * Returns the value of user defined attribute associated with the
     * specified key.  If there's no such attribute, the specified default
     * value is associated with the specified key, and the default value is
     * returned.  This method is same with the following code except that the
     * operation is performed atomically.
     * <pre>
     * if (containsAttribute(key)) {
     *     return getAttribute(key);
     * } else {
     *     setAttribute(key, defaultValue);
     *     return defaultValue;
     * }
     * </pre>
     */
    public Object getAttribute(Object key, Object defaultValue) {
    	Object obj = this.sessionAttributeMap.get(key);
    	if (obj == null) {
    		this.setAttribute(key, defaultValue);
    		return defaultValue;
    	} else {
    		return obj;
    	}
    }

    /**
     * Sets a user-defined attribute.
     *
     * @param key   the key of the attribute
     * @param value the value of the attribute
     * @return The old value of the attribute.  <tt>null</tt> if it is new.
     */
    public Object setAttribute(Object key, Object value) {
    	return this.sessionAttributeMap.put(key, value);
    }
}
