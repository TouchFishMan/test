package com.telek.hemsipc.sdmp;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * The <code>OctetString</code> class represents the SMI type OCTET STRING.
 * 
 * @author Frank Fock
 * @version 1.8
 * @since 1.0
 */
public class OctetString extends AbstractVariable {
    private static final long serialVersionUID = 4125661211046256289L;
    private static final char DEFAULT_HEX_DELIMITER = ':';
    private byte[] value = new byte[0];

    /**
     * Creates a zero length octet string.
     */
    public OctetString() {
    }

    /**
     * Creates an octet string from an byte array.
     * 
     * @param rawValue an array of bytes.
     */
    public OctetString(byte[] rawValue) {
        this(rawValue, 0, rawValue.length);
    }

    /**
     * Creates an octet string from an byte array.
     * 
     * @param rawValue an array of bytes.
     * @param offset the position (zero based) of the first byte to be copied
     *        from <code>rawValue</code>into the new <code>OctetSring</code>.
     * @param length the number of bytes to be copied.
     */
    public OctetString(byte[] rawValue, int offset, int length) {
        value = new byte[length];
        System.arraycopy(rawValue, offset, value, 0, length);
    }

    /**
     * Creates an octet string from a java string.
     * 
     * @param stringValue a Java string.
     */
    public OctetString(String stringValue) {
        this.value = stringValue.getBytes();
    }

    /**
     * Sets the value of the octet string to a zero length string.
     */
    public void clear() {
        value = new byte[0];
    }

    @Override
    public void encodeBER(ByteBuf buffer) throws IOException {
        BER.encodeString(buffer, BER.OCTETSTRING, getValue());
    }

    @Override
    public void decodeBER(ByteBuf buffer) throws IOException {
        BER.MutableByte type = new BER.MutableByte();
        byte[] v = BER.decodeString(buffer, type);
        if (type.getValue() != BER.OCTETSTRING) {
            throw new IOException("Wrong type encountered when decoding OctetString: " + type.getValue());
        }
        setValue(v);
    }

    @Override
    public int getBERLength() {
        return value.length + BER.getBERLengthOfLength(value.length) + 1;
    }

    @Override
    public int getSyntax() {
        return BER.ASN_OCTET_STR;
    }

    /**
     * Gets the byte at the specified index.
     * 
     * @param index a zero-based index into the octet string.
     * @return the byte value at the specified index.
     * @throws ArrayIndexOutOfBoundsException if <code>index</code> &lt; 0 or
     *         &gt; {@link #length()}.
     */
    public final byte get(int index) {
        return value[index];
    }

    /**
     * Sets the byte value at the specified index.
     * 
     * @param index an index value greater or equal 0 and less than
     *        {@link #length()}.
     * @param b the byte value to set.
     * @since v1.2
     */
    public final void set(int index, byte b) {
        value[index] = b;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < value.length; i++) {
            hash += value[i] * 31 ^ ((value.length - 1) - i);
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OctetString) {
            OctetString other = (OctetString) o;
            return Arrays.equals(value, other.value);
        }
        return false;
    }

    /**
     * Determines whether this octet string contains non ISO control characters
     * only.
     * 
     * @return <code>false</code> if this octet string contains any ISO control
     *         characters as defined by
     *         <code>Character.isISOControl(char)</code> except if these ISO
     *         control characters are all whitespace characters as defined by
     *         <code>Character.isWhitespace(char)</code> and not
     *         <code>'&#92;u001C'</code>-<code>'&#92;u001F'</code>.
     */
    public boolean isPrintable() {
        for (byte aValue : value) {
            char c = (char) aValue;
            if ((Character.isISOControl(c) || ((c & 0xFF) >= 0x80))
                    && ((!Character.isWhitespace(c)) || (((c & 0xFF) >= 0x1C)) && ((c & 0xFF) <= 0x1F))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (isPrintable()) {
            return new String(value);
        }
        return toHexString();
    }

    public String toHexString() {
        return toHexString(DEFAULT_HEX_DELIMITER);
    }

    private String toHexString(char separator) {
        return toString(separator, 16);
    }

    public static OctetString fromString(String string, char delimiter, int radix) {
        String delim = "";
        delim += delimiter;
        StringTokenizer st = new StringTokenizer(string, delim);
        byte[] value = new byte[st.countTokens()];
        for (int n = 0; st.hasMoreTokens(); n++) {
            String s = st.nextToken();
            value[n] = (byte) Integer.parseInt(s, radix);
        }
        return new OctetString(value);
    }

    public String toString(char separator, int radix) {
        int digits = (int) (Math.round((float) Math.log(256) / Math.log(radix)));
        StringBuffer buf = new StringBuffer(value.length * (digits + 1));
        for (int i = 0; i < value.length; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            int v = (value[i] & 0xFF);
            String val = Integer.toString(v, radix);
            for (int j = 0; j < digits - val.length(); j++) {
                buf.append('0');
            }
            buf.append(val);
        }
        return buf.toString();
    }

    /**
     * Formats the content into a ASCII string. Non-printable characters are
     * replaced by the supplied placeholder character.
     * 
     * @param placeholder a placeholder character, for example '.'.
     * @return the contents of this octet string as ASCII formatted string.
     * @since 1.6
     */
    public String toASCII(char placeholder) {
        StringBuffer buf = new StringBuffer(value.length);
        for (byte aValue : value) {
            if ((Character.isISOControl((char) aValue)) || ((aValue & 0xFF) >= 0x80)) {
                buf.append(placeholder);
            } else {
                buf.append((char) aValue);
            }
        }
        return buf.toString();
    }

    public void setValue(String value) {
        setValue(value.getBytes());
    }

    public void setValue(byte[] value) {
        if (value == null) {
            throw new IllegalArgumentException("OctetString must not be assigned a null value");
        }
        this.value = Arrays.copyOf(value, value.length);
    }

    public byte[] getValue() {
        return value;
    }

    /**
     * Gets the length of the byte string.
     * 
     * @return an integer >= 0.
     */
    public final int length() {
        return value.length;
    }

    @Override
    public Object clone() {
        return new OctetString(value);
    }

    /**
     * Returns the length of the payload of this <code>BERSerializable</code>
     * object in bytes when encoded according to the Basic Encoding Rules (BER).
     * 
     * @return the BER encoded length of this variable.
     */
    @Override
    public int getBERPayloadLength() {
        return value.length;
    }

    @Override
    public int toInt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long toLong() {
        throw new UnsupportedOperationException();
    }

    public String toStringIgnoreControlChar() {
        if (isPrintable()) {
            return new String(value);
        }
        return toHexStringIgnoreControlChar();
    }

    public String toHexStringIgnoreControlChar() {
        int digits = (int) (Math.round((float) Math.log(256) / Math.log(16)));
        StringBuffer buf = new StringBuffer(value.length * (digits + 1));
        for (int i = 0; i < value.length; i++) {
            int v = (value[i] & 0xFF);
            String val = Integer.toString(v, 16);
            for (int j = 0; j < digits - val.length(); j++) {
                buf.append('0');
            }
            buf.append(val);
        }
        return buf.toString();
    }
}
