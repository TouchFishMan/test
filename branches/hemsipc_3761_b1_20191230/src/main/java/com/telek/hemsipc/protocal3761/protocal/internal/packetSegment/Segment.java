package com.telek.hemsipc.protocal3761.protocal.internal.packetSegment;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by PETER on 2015/3/25.
 */
@Getter
@Setter
public abstract class Segment {
    private List<byte[]> buffer=new ArrayList<>();

    public abstract void reset();
}
