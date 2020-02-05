package com.telek.hemsipc.protocal3761.protocal.internal.wrap;

import java.util.ArrayList;
import java.util.List;

import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalTemplate;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;


/**
 * Created by PETER on 2015/3/24.
 */
public class WrapperChain {
    List<Wrapper> wrappers=new ArrayList<>();

    public void add(Wrapper wrapper){
        if(wrappers.size()>0){
            Wrapper pre=wrappers.get(wrappers.size()-1);
            pre.setNext(wrapper);
        }
        wrappers.add(wrapper);
    }

    public void encode(Packet in, PacketSegmentContext packetSegmentContext,
                       ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception{
        if(wrappers.size()>0){
            wrappers.get(0).encode(in,packetSegmentContext,protocalTemplate,codecConfig);
        }
    }
}
