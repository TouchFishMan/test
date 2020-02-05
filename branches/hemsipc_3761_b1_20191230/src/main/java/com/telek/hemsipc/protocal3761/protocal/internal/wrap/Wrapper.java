package com.telek.hemsipc.protocal3761.protocal.internal.wrap;


import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalTemplate;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;

/**
 * Created by PETER on 2015/3/24.
 */
public abstract class Wrapper {
    protected Wrapper next;
    abstract void encode(Packet in, PacketSegmentContext packetSegmentContext,
                         ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception;

    public void setNext(Wrapper next) {
        this.next = next;
    }
}
