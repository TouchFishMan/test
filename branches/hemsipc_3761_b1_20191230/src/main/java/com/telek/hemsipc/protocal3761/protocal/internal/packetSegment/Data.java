package com.telek.hemsipc.protocal3761.protocal.internal.packetSegment;

import java.util.Map;

/**
 * Created by PETER on 2015/3/25.
 */
@lombok.Data
public class Data extends Segment implements Cloneable {

    /**
     * 信息点pn
     */
    private int pn;
    /**
     * 信息类fn
     */
    private int fn;
    /**
     * 数据
     */
    private Map<String,Object> data;

    private String command;

    @Override
    public void reset() {
        pn=0;
        fn=0;
        command=null;
        data=null;
        getBuffer().clear();
    }

    @Override
    public Data clone() {
        Data data = new Data();
        data.setData(this.data);
        data.setCommand(command);
        data.setPn(pn);
        data.setFn(fn);
        return data;
    }
}
