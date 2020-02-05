package com.telek.hemsipc.sdmp;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Description: 提供BER编码和解码的实用方法 BER.java Create on 2012-10-11 下午3:57:19
 * 
 * @author jnb
 * @version 1.0 Copyright (c) 2012 telek. All Rights Reserved.
 */
public class BER {
    public static final byte ASN_BOOLEAN = 0x01;
    public static final byte ASN_INTEGER = 0x02;
    public static final byte ASN_BIT_STR = 0x03;
    public static final byte ASN_OCTET_STR = 0x04;
    public static final byte ASN_NULL = 0x05;
    public static final byte ASN_OBJECT_ID = 0x06;
    public static final byte ASN_SEQUENCE = 0x10;
    public static final byte ASN_SET = 0x11;
    public static final byte ASN_UNIVERSAL = 0x00;
    public static final byte ASN_APPLICATION = 0x40;
    public static final byte ASN_CONTEXT = (byte) 0x80;
    public static final byte ASN_PRIVATE = (byte) 0xC0;
    public static final byte ASN_PRIMITIVE = (byte) 0x00;
    public static final byte ASN_CONSTRUCTOR = (byte) 0x20;
    public static final byte ASN_LONG_LEN = (byte) 0x80;
    public static final byte ASN_EXTENSION_ID = (byte) 0x1F;
    public static final byte ASN_BIT8 = (byte) 0x80;
    public static final byte INTEGER = ASN_UNIVERSAL | 0x02;
    public static final byte INTEGER32 = ASN_UNIVERSAL | 0x02;
    public static final byte BITSTRING = ASN_UNIVERSAL | 0x03;
    public static final byte OCTETSTRING = ASN_UNIVERSAL | 0x04;
    public static final byte NULL = ASN_UNIVERSAL | 0x05;
    public static final byte SIGNED_INTEGER64 = ASN_UNIVERSAL | 0x07;
    
    public static final byte SEQUENCE = ASN_CONSTRUCTOR | 0x10;
    public static final byte IPADDRESS = ASN_APPLICATION | 0x00;
    public static final byte COUNTER = ASN_APPLICATION | 0x01;
    public static final byte COUNTER32 = ASN_APPLICATION | 0x01;
    public static final byte GAUGE = ASN_APPLICATION | 0x02;
    public static final byte GAUGE32 = ASN_APPLICATION | 0x02;
    public static final byte TIMETICKS = ASN_APPLICATION | 0x03;
    public static final byte OPAQUE = ASN_APPLICATION | 0x04;
    public static final byte COUNTER64 = ASN_APPLICATION | 0x06;
    public static final int NOSUCHOBJECT = 0x80;
    public static final int NOSUCHINSTANCE = 0x81;
    public static final int ENDOFMIBVIEW = 0x82;
    public static final int MAX_OID_LENGTH = 127;
    private static boolean checkSequenceLength = true;
    private static boolean checkValueLength = true;

    /**
     * The <code>MutableByte</code> class serves for exchanging type information
     * from the various decode* methods.
     * 
     * @author Frank Fock
     * @version 1.0
     */
    public static class MutableByte {
        private byte value = 0;

        public MutableByte() {
        }

        public MutableByte(byte value) {
            setValue(value);
        }

        public void setValue(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    /**
     * Encodes an ASN.1 header for an object with the ID and length specified.
     * 
     * @param os an <code>OutputStream</code> to which the header is encoded.
     * @param type the type of the ASN.1 object. Must be < 30, i.e. no extension
     *        octets.
     * @param length the length of the object. The maximum length is 0xFFFFFFFF;
     * @throws IOException
     */
    public static void encodeHeader(ByteBuf buffer, int type, int length) throws IOException {
        buffer.writeByte(type);
        encodeLength(buffer, length);
    }

    /**
     * Compute the space needed to encode the length.
     * 
     * @param length Length to encode
     * @return the count of bytes needed to encode the value <code>length</code>
     */
    public static int getBERLengthOfLength(int length) {
        if (length < 0) {
            return 5;
        } else if (length < 0x80) {
            return 1;
        } else if (length <= 0xFF) {
            return 2;
        } else if (length <= 0xFFFF) {
            return 3;
        } else if (length <= 0xFFFFFF) {
            return 4;
        }
        return 5;
    }

    /**
     * Encodes the length of an ASN.1 object.
     * 
     * @param os an <code>OutputStream</code> to which the length is encoded.
     * @param length the length of the object. The maximum length is 0xFFFFFFFF;
     * @throws IOException
     */
    public static void encodeLength(ByteBuf buffer, int length) throws IOException {
        if (length < 0) {
            buffer.writeByte(0x04 | ASN_LONG_LEN);
            buffer.writeByte((length >> 24) & 0xFF);
            buffer.writeByte((length >> 16) & 0xFF);
            buffer.writeByte((length >> 8) & 0xFF);
            buffer.writeByte(length & 0xFF);
        } else if (length < 0x80) {
            buffer.writeByte(length);
        } else if (length <= 0xFF) {
            buffer.writeByte((0x01 | ASN_LONG_LEN));
            buffer.writeByte(length);
        } else if (length <= 0xFFFF) {
            buffer.writeByte(0x02 | ASN_LONG_LEN);
            buffer.writeByte((length >> 8) & 0xFF);
            buffer.writeByte(length & 0xFF);
        } else if (length <= 0xFFFFFF) {
            buffer.writeByte(0x03 | ASN_LONG_LEN);
            buffer.writeByte((length >> 16) & 0xFF);
            buffer.writeByte((length >> 8) & 0xFF);
            buffer.writeByte(length & 0xFF);
        } else {
            buffer.writeByte(0x04 | ASN_LONG_LEN);
            buffer.writeByte((length >> 24) & 0xFF);
            buffer.writeByte((length >> 16) & 0xFF);
            buffer.writeByte((length >> 8) & 0xFF);
            buffer.writeByte(length & 0xFF);
        }
    }

    
    public static long decodeSingedInteger64(ByteBuf buffer, MutableByte type) throws IOException {
        int length;
        long value = 0;
        type.setValue((byte) (buffer.readByte() & 0xFF));
        if ((type.value != 0x02) && (type.value != 0x43) && (type.value != 0x41) && (type.value != BER.SIGNED_INTEGER64)) {
            throw new IOException("Wrong ASN.1 type. Not an integer64: " + type.value + getPositionMessage(buffer));
        }
        length = decodeLength(buffer);
        if (length > 8) {
            throw new IOException("Length greater than 64bit are not supported " + " for integers: " + getPositionMessage(buffer));
        }
        int b = buffer.readByte() & 0xFF & 0xFF;
        if ((b & 0x80) > 0) {
            value = -1;
        }
        while (length-- > 0) {
            value = (value << 8) | b;
            if (length > 0) {
                b = buffer.readByte() & 0xFF;
            }
        }
        return value;
    }

    /**
     * Encode a signed integer.
     * 
     * @param os an <code>OutputStream</code> to which the length is encoded.
     * @param type the tag type for the integer (typically 0x02)
     * @param value the integer value to encode.
     * @throws IOException
     */
    public static final void encodeSingedInteger64(ByteBuf buffer, byte type, long value) throws IOException {
        long integer = value;
        long mask;
        int intsize = 8;
        // * Truncate "unnecessary" bytes off of the most significant end of
        // this
        // * 2's complement integer. There should be no sequence of 9
        // consecutive
        // * 1's or 0's at the most significant end of the integer.
        mask = 0x1FFL << ((8 * 7) - 1);
        // mask is 0xFF800000 on a big-endian machine
        while ((((integer & mask) == 0) || ((integer & mask) == mask) ) && intsize > 1) {
            intsize--;
            integer <<= 8;
        }
        encodeHeader(buffer, type, intsize);
        mask = 0xFFL << (8 * 7);
        // mask is 0xFF000000 on a big-endian machine
        while ((intsize--) > 0) {
            buffer.writeByte((byte)((integer & mask) >> (8 * 7)) );
            integer <<= 8;
        }
    }

    
    public static final void encodeUnsignedInteger64(ByteBuf buffer, byte type, long value) throws IOException {
        //long mask;
        int intsize = 8;
        if (value <= 0xFF) {
            intsize = 1;
        } else if (value <= 0xFFFF) {
            intsize = 2;
        } else if (value <= 0xFFFFFF) {
            intsize = 3;
        }else if (value <= 0xFFFFFFFFL) {// 这个L必须加，否则临界值16777216 编码后是“0001 0000 0000 0000 0000 0000 0000”，会错误，计算成5字节
            intsize = 4;
        }
        else if (value <= 0xFFFFFFFFFFL) {
            intsize = 5;
        }
        else if (value <= 0xFFFFFFFFFFFFL) {
            intsize = 6;
        }
        else if (value <= 0xFFFFFFFFFFFFFFL) {
            intsize = 7;
        }
        
        encodeHeader(buffer, type, intsize);
    
        //mask = 0xFFL << (8 * 7);
        /* mask is 0xFF000000 on a big-endian machine */
        value <<= ((8-intsize) * 8);
        
        while ((intsize--) > 0) {
            //byte temp = (byte) ((value & mask) >> (8 * 7) & 0xFF);
            byte temp = (byte) (value >> (8 * 7) & 0xFF);
            value <<= 8;
            buffer.writeByte(temp);
        }
  
    }

   /* public static final void encodeIntegerUnsigned(ByteBuf buffer, byte type, int value) throws IOException {
        int mask;
        int intsize = 4;
        if (value <= 0xFF) {
            intsize = 1;
        } else if (value <= 0xFFFF) {
            intsize = 2;
        } else if (value <= 0xFFFFFF) {
            intsize = 3;
        }
        encodeHeader(buffer, type, intsize);
        // if (value == 0) {
        // byte temp = 0;
        // buffer.writeByte(temp);
        // return;
        // }
        boolean writed = false;
        mask = 0xFF << (8 * 3);
         mask is 0xFF000000 on a big-endian machine 
        while ((intsize--) > 0) {
            if (value == 0) {
                byte flag = 0;
                buffer.writeByte(flag);
                continue;
            }
            byte temp = (byte) ((value & mask) >> (8 * 3) & 0xFF);
            value <<= 8;
            if (temp == 0 && !writed) {
                intsize += 1;
                continue;
            }
            buffer.writeByte(temp);
            writed = true;
        }
    }*/

 
    /**
     * Encode an ASN.1 octet string filled with the supplied input string.
     * 
     * @param os an <code>OutputStream</code> to which the length is encoded.
     * @param type the tag type for the integer (typically 0x02)
     * @param string the <code>byte</code> array containing the octet string
     *        value.
     * @throws IOException
     */
    public static void encodeString(ByteBuf buffer, byte type, byte[] string) throws IOException {
        encodeHeader(buffer, type, string.length);
        // fixed
        buffer.writeBytes(string);
    }

    /**
     * Encode an ASN.1 header for a sequence with the ID and length specified.
     * This only works on data types < 30, i.e. no extension octets. The maximum
     * length is 0xFFFF;
     * 
     * @param os an <code>OutputStream</code> to which the length is encoded.
     * @param type the tag type for the integer (typically 0x02)
     * @param length the length of the sequence to encode.
     * @throws IOException
     */
    public static void encodeSequence(ByteBuf buffer, byte type, int length) throws IOException {
        buffer.writeByte(type);
        encodeLength(buffer, length);
    }

    /**
     * Decodes a ASN.1 length.
     * 
     * @param is an <code>InputStream</code>
     * @return the decoded length.
     * @throws IOException
     */
    public static int decodeLength(ByteBuf buffer) throws IOException {
        return decodeLength(buffer, true);
    }

    /**
     * Decodes a ASN.1 length.
     * 
     * @param is an <code>InputStream</code>
     * @param checkLength if <code>false</code> length check is always
     *        suppressed.
     * @return the decoded length.
     * @throws IOException
     */
    public static int decodeLength(ByteBuf buffer, boolean checkLength) throws IOException {
        int length = 0;
        int lengthbyte = buffer.readByte() & 0xFF; 
        if ((lengthbyte & ASN_LONG_LEN) > 0) {   // 如果第一位是1，则后面7位代表几个字节表示长度
            lengthbyte &= ~ASN_LONG_LEN;
            if (lengthbyte == 0) {
                throw new IOException("Indefinite lengths are not supported");
            }
            if (lengthbyte > 4) { 
                throw new IOException("Data length > 4 bytes are not supported!");
            }
            for (int i = 0; i < lengthbyte; i++) {
                int l = buffer.readByte() & 0xFF & 0xFF;
                length |= (l << (8 * ((lengthbyte - 1) - i)));
            }
            if (length < 0) {
                throw new IOException("SNMP does not support data lengths > 2^31");
            }
        } else {
            length = lengthbyte & 0xFF;
        }
        /**
         * If activated we do a length check here: length > is.available() ->
         * throw exception
         */
        if (checkLength) {
            checkLength(buffer, length);
        }
        return length;
    }

    /**
     * Decodes an ASN.1 header for an object with the ID and length specified.
     * On entry, datalength is input as the number of valid bytes following
     * "data". On exit, it is returned as the number of valid bytes in this
     * object following the id and length. This only works on data types < 30,
     * i.e. no extension octets. The maximum length is 0xFFFF;
     * 
     * @param is the BERInputStream to decode.
     * @param type returns the type of the object at the current position in the
     *        input stream.
     * @param checkLength if <code>false</code> length check is always
     *        suppressed.
     * @return the decoded length of the object.
     * @throws IOException
     */
    public static int decodeHeader(ByteBuf buffer, MutableByte type, boolean checkLength) throws IOException {
        /* this only works on data types < 30, i.e. no extension octets */
        byte t = (byte) (buffer.readByte() & 0xFF);
        if ((t & ASN_EXTENSION_ID) == ASN_EXTENSION_ID) {
            throw new IOException("Cannot process extension IDs" + getPositionMessage(buffer));
        }
        type.setValue(t);
        if (SEQUENCE != type.value) {
            throw new IOException(
                    "Wrong ASN.1 type. Not an Sequence: " + type.value + getPositionMessage(buffer));
        }
        return decodeLength(buffer, checkLength);
    }

    /**
     * Decodes an ASN.1 header for an object with the ID and length specified.
     * On entry, datalength is input as the number of valid bytes following
     * "data". On exit, it is returned as the number of valid bytes in this
     * object following the id and length. This only works on data types < 30,
     * i.e. no extension octets. The maximum length is 0xFFFF;
     * 
     * @param is the BERInputStream to decode.
     * @param type returns the type of the object at the current position in the
     *        input stream.
     * @return the decoded length of the object.
     * @throws IOException
     */
    public static int decodeHeader(ByteBuf buffer, MutableByte type) throws IOException {
        return decodeHeader(buffer, type, true);
    }

  
    public static long decodeUnsignedInteger64(ByteBuf buffer, MutableByte type) throws IOException {
        int length;
        long value = 0;
        type.setValue((byte) (buffer.readByte() & 0xFF));
        if ((type.value != 0x02) && (type.value != 0x43) && (type.value != 0x41)) {
            throw new IOException(
                    "Wrong ASN.1 type. Not an integer: " + type.value + getPositionMessage(buffer));
        }
        length = decodeLength(buffer);
        if (length > 8) {
            throw new IOException("Length greater than 32bit are not supported " + " for integers: "
                    + getPositionMessage(buffer));
        }
        int b = buffer.readByte() & 0xFF & 0xFF;
        while (length-- > 0) {
            value = (value << 8) | b;
            if (length > 0) {
                b = buffer.readByte() & 0xFF;
            }
        }
        return value;
    }
    
    
/*    public static int decodeIntegerUnsigned(ByteBuf buffer, MutableByte type) throws IOException {
        int length;
        int value = 0;
        type.setValue((byte) (buffer.readByte() & 0xFF));
        if ((type.value != 0x02) && (type.value != 0x43) && (type.value != 0x41)) {
            throw new IOException(
                    "Wrong ASN.1 type. Not an integer: " + type.value + getPositionMessage(buffer));
        }
        length = decodeLength(buffer);
        if (length > 4) {
            throw new IOException("Length greater than 32bit are not supported " + " for integers: "
                    + getPositionMessage(buffer));
        }
        int b = buffer.readByte() & 0xFF & 0xFF;
        while (length-- > 0) {
            value = (value << 8) | b;
            if (length > 0) {
                b = buffer.readByte() & 0xFF;
            }
        }
        return value;
    }*/

    private static String getPositionMessage(ByteBuf buffer) {
        return " at position " + buffer.readerIndex();
    }

    public static byte[] decodeString(ByteBuf buffer, MutableByte type) throws IOException {
        type.setValue((byte) (buffer.readByte() & 0xFF));
        if ((type.value != BER.OCTETSTRING) && (type.value != 0x24) && (type.value != BER.IPADDRESS)
                && (type.value != BER.OPAQUE) && (type.value != BER.BITSTRING) && (type.value != 0x45)) {
            throw new IOException(
                    "Wrong ASN.1 type. Not a string: " + type.value + getPositionMessage(buffer));
        }
        int length = decodeLength(buffer);
        byte[] value = new byte[length];
        int pos = 0;
        while ((pos < length) && (buffer.readableBytes() > 0)) {
            int read = 0;
            if (buffer.readableBytes() <= 0) {
                read = -1;
            }
            read = Math.min(buffer.readableBytes(), value.length);
            buffer.readBytes(value, 0, read);
            if (read > 0) {
                pos += read;
            } else if (read < 0) {
                throw new IOException("Wrong string length " + read + " < " + length);
            }
        }
        return value;
    }

    public static void decodeNull(ByteBuf buffer, MutableByte type) throws IOException {
        // get the type
        type.setValue((byte) (buffer.readByte() & 0xFF & 0xFF));
        if ((type.value != (byte) 0x05) && (type.value != (byte) 0x80) && (type.value != (byte) 0x81)
                && (type.value != (byte) 0x82)) {
            throw new IOException(
                    "Wrong ASN.1 type. Is not null: " + type.value + getPositionMessage(buffer));
        }
        int length = decodeLength(buffer);
        if (length != 0) {
            throw new IOException(
                    "Invalid Null encoding, length is not zero: " + length + getPositionMessage(buffer));
        }
    }

    /**
     * Gets the SEQUENCE length checking mode.
     * 
     * @return <code>true</code> if the length of a parsed SEQUENCE should be
     *         checked against the real length of the objects parsed.
     */
    public static boolean isCheckSequenceLength() {
        return checkSequenceLength;
    }

    /**
     * Sets the application wide SEQUENCE length checking mode.
     * 
     * @param checkSequenceLen specifies whether he length of a parsed SEQUENCE
     *        should be checked against the real length of the objects parsed.
     */
    public static void setCheckSequenceLength(boolean checkSequenceLen) {
        checkSequenceLength = checkSequenceLen;
    }

    public static void checkSequenceLength(int expectedLength, BERSerializable sequence) throws IOException {
        if ((isCheckSequenceLength()) && (expectedLength != sequence.getBERPayloadLength())) {
            throw new IOException("The actual length of the SEQUENCE object " + sequence.getClass().getName()
                    + " is " + sequence.getBERPayloadLength() + ", but " + expectedLength + " was expected");
        }
    }

    public static void checkSequenceLength(int expectedLength, int actualLength, BERSerializable sequence)
            throws IOException {
        if ((isCheckSequenceLength()) && (expectedLength != actualLength)) {
            throw new IOException("The actual length of the SEQUENCE object " + sequence.getClass().getName()
                    + " is " + actualLength + ", but " + expectedLength + " was expected");
        }
    }

    /**
     * Checks whether the length of that was encoded is also available from the
     * stream.
     * 
     * @param is InputStream
     * @param length int
     * @throws IOException if the bytes that are given in length cannot be read
     *         from the input stream (without blocking).
     */
    private static void checkLength(ByteBuf buffer, int length) throws IOException {
        if (!checkValueLength) {
            return;
        }
        if ((length < 0) || (length > buffer.capacity())) {
            throw new IOException(
                    "The encoded length " + length + " exceeds the number of bytes left in input"
                            + getPositionMessage(buffer) + " which actually is " + buffer.capacity());
        }
    }

    public boolean isCheckValueLength() {
        return checkValueLength;
    }

    public void setCheckValueLength(boolean checkValueLength) {
        BER.checkValueLength = checkValueLength;
    }

    public int read(ByteBuf buffer, byte[] b) throws IOException {
        if (buffer.readableBytes() <= 0) {
            return -1;
        }
        int read = Math.min(buffer.readableBytes(), b.length);
        buffer.readBytes(b, 0, read);
        return read;
    }
}
