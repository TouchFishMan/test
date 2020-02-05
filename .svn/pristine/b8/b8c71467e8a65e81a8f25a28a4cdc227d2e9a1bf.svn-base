package com.telek.hemsipc.sdmp;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * The <code>Integer32</code> represents 32bit signed integer values for SNMP.
 * 
 * @author Frank Fock
 * @version 1.8
 */
public class SignedInteger64 extends AbstractVariable {
    private static final long serialVersionUID = 5046132399890132417L;
    private long value = 0L;

    /**
     * Creates an <code>Integer32</code> with a zero value.
     */
    public SignedInteger64() {
    }

    public SignedInteger64(int value){
        setValue(value);
    }
    /**
     * Creates an <code>Integer32</code> variable with the supplied value.
     * 
     * @param value an integer value.
     */
    public SignedInteger64(Long value) {
        setValue(value);
    }

    @Override
	public void encodeBER(ByteBuf buffer) throws java.io.IOException {
        BER.encodeSingedInteger64(buffer, BER.SIGNED_INTEGER64, value);  
    }

    @Override
	public void decodeBER(ByteBuf buffer) throws java.io.IOException {  
        BER.MutableByte type = new BER.MutableByte();
        long newValue = BER.decodeSingedInteger64(buffer, type);
        if (type.getValue() != BER.SIGNED_INTEGER64) {
            throw new IOException("Wrong type encountered when decoding Counter: " + type.getValue());
        }
        setValue(newValue);
    }

    @Override
	public int getSyntax() {
        return BER.SIGNED_INTEGER64;
    }

    @Override
	public int hashCode() {
        return (int)value; // TODO 不合适
    }

    @Override
    public int getBERLength() {
        long integer = value;
        long mask;
        int intsize = 8;
        mask = 0x1FFL << ((8 * 7) - 1);
        // mask is 0xFF800000 on a big-endian machine
        while ((((integer & mask) == 0) || ((integer & mask) == mask)) && intsize > 1) {
            intsize--;
            integer <<= 8;
        }
        return intsize + 2;
    }
    
    @Override
	public boolean equals(Object o) {
        return (o instanceof SignedInteger64) && (((SignedInteger64) o).value == value);
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
        return new SignedInteger64(value);
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
