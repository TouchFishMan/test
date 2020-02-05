package com.telek.hemsipc.protocal3761.protocal.internal.packetSegment;

import lombok.Data;

/**
 * Created by PETER on 2015/3/25.
 */
@Data
public class Tail extends Segment implements Cloneable {
    private int checkSum;

    @Override
    public void reset() {
        checkSum=0;
        getBuffer().clear();
    }

    @Override
    public Tail clone() {
        Tail tail = new Tail();
        tail.setCheckSum(checkSum);
        return tail;
    }
}
