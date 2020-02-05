package com.telek.hemsipc.sdmp;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.Serializable;

/**
 * The <code>Variable</code> abstract class is the base class for all SNMP
 * variables.
 * <p>
 * All derived classes need to be registered with their SMI BER type in the
 * <code>smisyntaxes.properties</code>so that the
 * {@link #createFromBER(BERInputStream inputStream)} method is able to decode a
 * variable from a BER encoded stream.
 * <p>
 * To register additional syntaxes, set the system property
 * {@link #SMISYNTAXES_PROPERTIES} before decoding a Variable for the first
 * time. The path of the property file must be accessible from the classpath and
 * it has to be specified relative to the <code>Variable</code> class.
 * 
 * @author Jochen Katz & Frank Fock
 * @version 1.8
 * @since 1.8
 */
public abstract class AbstractVariable implements Variable, Serializable {
    private static final long serialVersionUID = 1395840752909725320L;

    /**
     * The abstract <code>Variable</code> class serves as the base class for all
     * specific SNMP syntax types.
     */
    public AbstractVariable() {
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    /**
     * Returns the length of this <code>Variable</code> in bytes when encoded
     * according to the Basic Encoding Rules (BER).
     * 
     * @return the BER encoded length of this variable.
     */
    @Override
    public abstract int getBERLength();

    @Override
    public int getBERPayloadLength() {
        return getBERLength();
    }

    /**
     * Decodes a <code>Variable</code> from an <code>InputStream</code>.
     * 
     * @param buffer an <code>InputStream</code> containing a BER encoded
     *        byte stream.
     * @throws IOException if the stream could not be decoded by using BER
     *         rules.
     */
    @Override
    public abstract void decodeBER(ByteBuf buffer) throws IOException;

    /**
     * Encodes a <code>Variable</code> to an <code>OutputStream</code>.
     * 
     * @param buffer an <code>OutputStream</code>.
     * @throws IOException if an error occurs while writing to the stream.
     */
    @Override
    public abstract void encodeBER(ByteBuf buffer) throws IOException;

    /**
     * Creates a <code>Variable</code> from a BER encoded
     * <code>InputStream</code>. Subclasses of <code>Variable</code> are
     * registered using the properties file <code>smisyntaxes.properties</code>
     * in this package. The properties are read when this method is called
     * first.
     * 
     * @param buffer an <code>BERInputStream</code> containing a BER
     *        encoded byte stream.
     * @return an instance of a subclass of <code>Variable</code>.
     * @throws IOException if the <code>inputStream</code> is not properly BER
     *         encoded.
     */
    public static Variable createFromBER(ByteBuf buffer) throws IOException {
        buffer.markReaderIndex();
        int type = buffer.readByte() & 0xFF;
        Variable variable;
        variable = createVariable(type);
        // buffer.reset();
        buffer.resetReaderIndex();
        variable.decodeBER(buffer);
        return variable;
    }

    private static Variable createVariable(int smiSyntax) {
        switch (smiSyntax) {
        case BER.ASN_INTEGER: {
            return new Integer32();
        }
        case BER.ASN_OCTET_STR: {
            return new OctetString();
        }
        case BER.SIGNED_INTEGER64: {
            return new SignedInteger64();
        }
        default: {
            throw new IllegalArgumentException("Unsupported variable syntax: " + smiSyntax);
        }
        }
    }

    /**
     * Gets the ASN.1 syntax identifier value of this SNMP variable.
     * 
     * @return an integer value < 128 for regular SMI objects and a value >= 128
     *         for exception values like noSuchObject, noSuchInstance, and
     *         endOfMibView.
     */
    @Override
    public abstract int getSyntax();

    /**
     * Gets a string representation of the variable.
     * 
     * @return a string representation of the variable's value.
     */
    @Override
    public abstract String toString();

    /**
     * Returns an integer representation of this variable if such a
     * representation exists.
     * 
     * @return an integer value (if the native representation of this variable
     *         would be a long, then the long value will be casted to int).
     * @throws UnsupportedOperationException if an integer representation does
     *         not exists for this Variable.
     * @since 1.7
     */
    @Override
    public abstract int toInt();

    /**
     * Returns a long representation of this variable if such a representation
     * exists.
     * 
     * @return a long value.
     * @throws UnsupportedOperationException if a long representation does not
     *         exists for this Variable.
     * @since 1.7
     */
    @Override
    public abstract long toLong();

    @Override
    public abstract Object clone();

    /**
     * Indicates whether this variable is dynamic, which means that it might
     * change its value while it is being (BER) serialized. If a variable is
     * dynamic, it will be cloned on-the-fly when it is added to a {@link PDU}
     * with {@link PDU#add(VariableBinding)}. By cloning the value, it is
     * ensured that there are no inconsistent changes between determining the
     * length with {@link #getBERLength()} for encoding enclosing SEQUENCES and
     * the actual encoding of the Variable itself with {@link #encodeBER}.
     * 
     * @return <code>false</code> by default. Derived classes may override this
     *         if implementing dynamic {@link Variable} instances.
     * @since 1.8
     */
    @Override
    public boolean isDynamic() {
        return false;
    }

    /**
     * Tests if two variables have the same value.
     * 
     * @param a a variable.
     * @param b another variable.
     * @return <code>true</code> if
     *         <code>a == null) ?  (b == null) : a.equals(b)</code>.
     * @since 2.0
     */
    public static boolean equal(AbstractVariable a, AbstractVariable b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
