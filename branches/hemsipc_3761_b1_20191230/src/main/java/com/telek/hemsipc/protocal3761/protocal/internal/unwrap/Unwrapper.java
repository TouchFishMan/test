package com.telek.hemsipc.protocal3761.protocal.internal.unwrap;

import java.nio.ByteBuffer;

import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalTemplate;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;


/**
 * Created by PETER on 2015/3/25.
 */
public abstract class Unwrapper {
    protected Unwrapper next;
    abstract  void decode(ByteBuffer in, PacketSegmentContext packetSegmentContext,
                          ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception;

    void setNext(Unwrapper unwrapper){
        next=unwrapper;
    }

}
