package com.telek.hemsipc.protocal3761.protocal.internal.packetSegment;

import lombok.Data;

/**
 * Created by PETER on 2015/3/25.
 */
@Data
public class Auxiliary extends Segment implements Cloneable {
    private String password;
    private int ec1;
    private int ec2;
    private int pfc;
    private long sendTime;
    private int timeout;

    @Override
    public void reset() {
        password="";
        ec1=0;
        ec2=0;
        pfc=0;
        sendTime=0;
        timeout=0;
        getBuffer().clear();
    }

    @Override
    public Auxiliary clone() {
        Auxiliary auxiliary = new Auxiliary();
        auxiliary.setTimeout(timeout);
        auxiliary.setPassword(password);
        auxiliary.setEc1(ec1);
        auxiliary.setEc2(ec2);
        auxiliary.setPfc(pfc);
        auxiliary.setSendTime(sendTime);
        return auxiliary;
    }
}
