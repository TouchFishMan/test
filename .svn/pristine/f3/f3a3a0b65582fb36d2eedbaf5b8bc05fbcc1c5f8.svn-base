package com.telek.hemsipc.sdmp;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * The <code>Integer32</code> represents 32bit signed integer values for SNMP.
 * 
 * @author Frank Fock
 * @version 1.8
 */
public class Integer32 extends AbstractVariable {
    private static final long serialVersionUID = 5046132399890132416L;
    private long value = 0L;

    /**
     * Creates an <code>Integer32</code> with a zero value.
     */
    public Integer32() {
    }

    public Integer32(int value){
        setValue(value);
    }
    /**
     * Creates an <code>Integer32</code> variable with the supplied value.
     * 
     * @param value an integer value.
     */
    public Integer32(Long value) {
        setValue(value);
    }

    @Override
        public void encodeBER(ByteBuf buffer) throws java.io.IOException {
        BER.encodeUnsignedInteger64(buffer, BER.INTEGER, value);
    }

    @Override
        public void decodeBER(ByteBuf buffer) throws java.io.IOException {
        BER.MutableByte type = new BER.MutableByte();
        long newValue = BER.decodeUnsignedInteger64(buffer, type);
        if (type.getValue() != BER.INTEGER) {
            throw new IOException("Wrong type encountered when decoding Counter: " + type.getValue());
        }
        setValue(newValue);
    }

    @Override
        public int getSyntax() {
        return BER.ASN_INTEGER;
    }

    @Override
        public int hashCode() {
        return (int)value; // TODO 不合适
    }

    @Override
        public int getBERLength() {
        if (value <= 0xFF) {
            return 3;
        } else if (value <= 0xFFFF) {
            return 4;
        } else if (value <= 0xFFFFFF) {
            return 5;
        }
        else if (value <= 0xFFFFFFFFL) {
            return 6;
        }
        else if (value <= 0xFFFFFFFFFFL) {
            return 7;
        }
        else if (value <= 0xFFFFFFFFFFFFL) {
            return 8;
        }
        else if (value <= 0xFFFFFFFFFFFFFFL) {
            return 9;
        }
        return 10;
    }

    @Override
        public boolean equals(Object o) {
        return (o instanceof Integer32) && (((Integer32) o).value == value);
    }

    @Override
        public String toString() {
        return Long.toString(value);
    }

    public final void setValue(String value) {
        this.value = Long.parseLong(value);
    }

    /**
     * Sets the value of this integer.
     * 
     * @param value an integer value.
     */
    public final void setValue(long value) {
        this.value = value;
    }

    /**
     * Gets the value.
     * 
     * @return an integer.
     */
    public final Long getValue() {
        return value;
    }

    @Override
        public Object clone() {
        return new Integer32(value);
    }

    @Override
        public final int toInt() {
        return getValue().intValue();
    }

    @Override
        public final long toLong() {
        return getValue();
    }
    
}
