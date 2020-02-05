package com.telek.hemsipc.protocal3761.netty;

import java.nio.ByteBuffer;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.protocal3761.ProtocalFactory;
import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.protocal.IProtocal;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommConst;

import com.telek.hemsipc.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: wll
 * @Date: 2019/1/17 15:42
 * @Description:
 */
@Slf4j
public class HexDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        int i = in.readableBytes();
        byte[] bs = new byte[i];
        in.readBytes(bs);
        log.info("-----------------------------------------");
        log.info("接收帧:" + HexBin.encode(bs));
        ByteBuffer byteBuffer = ByteBuffer.wrap(bs);
        Packet packet = new Packet();
        try {
            Packet tempPacket = new Packet();
            int code = ProtocalFactory.getProtocal(null, null).decode(byteBuffer, tempPacket);
            int prm = tempPacket.getControl().getPrm();
            log.info("解码:" + tempPacket.getTerminalAddress() + " " + tempPacket.getDataInfo());
            String address = tempPacket.getTerminalAddress();
            // 解码, 报文来自终端
            boolean isStartStation = false;
            if (CommConst.PRM_SLAVE_STATION == prm) {
                isStartStation = true;
            }
            IProtocal protocal = ProtocalFactory.getProtocal(address, isStartStation);
            protocal.decode(byteBuffer, packet);
            //加入缓存，返回集中器配置页面
            if (packet.getControl() != null) {
                StringBuffer keyBuf = new StringBuffer();
                keyBuf.append(packet.getTerminalAddress()).append(Constant.SPLIT_SIGN_COLON).append(packet.getControl().getSeq());
                Protocol3761Cache.TERMINAL_SEQ_RECEIVE_DATA.put(keyBuf.toString(), StringUtil.byteArrToString(bs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解码异常...");
        } finally {
            in.clear();
            byteBuffer.clear();
        }
        return packet;
    }
}
