package com.telek.hemsipc.protocal3761.service.response;


import com.alibaba.fastjson.JSONObject;
import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.HemsipcSpringContext;
import com.telek.hemsipc.http.HttpServerStart;
import com.telek.hemsipc.http.queueserver.HttpResponseQueueServer;
import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F10Dto;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F3Dto;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F65Dto;
import com.telek.hemsipc.protocal3761.netty.NettyStarter;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;

import com.telek.hemsipc.protocal3761.service.ProtocolCommandService;
import com.telek.hemsipc.util.CommandJsonResult;
import com.telek.hemsipc.util.JsonResult;
import com.telek.hemsipc.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class ResponseServer implements Runnable {

    private Packet decodedPacket;

    private ProtocolCommandService commandService = HemsipcSpringContext.getBean(ProtocolCommandService.class);


    public ResponseServer(Packet decodedPacket) {
        this.decodedPacket = decodedPacket;
    }

    @Override
    public void run() {
        String command = decodedPacket.getCommand();
        if (command == null) {
            log.error("command is null");
        }
        IResponse response = null;
        switch (CommandAfn.get(command)) {
            case LOGIN:
            case HEARTBEAT:
            case LOGOUT:
                response = new LinkResponse();
                break;
            case GET_TERMINAL_TIME:
                log.info("终端时间为:" + decodedPacket.getData());
                break;
            case GET_REAL_TIME_DATA:
                dealHttpService(decodedPacket);
                response = new Afn0cF25RealTimeData();
                break;
            case GET_ELEC_REAL_DATA:
                response = new Afn0cF129ElecData();
                dealHttpService(decodedPacket);
                break;
            case CONFIRM:
                dealHttpService(decodedPacket);
                break;
            default:
                log.info("解码packet:" + decodedPacket);
                dealHttpService(decodedPacket);
                break;
        }
        if (response != null) {
            response.dealReceiveFromTerminal(decodedPacket);
        }
    }

    private void dealHttpService(Packet decodedPacket) {
        if (decodedPacket == null) {
            return;
        }
        StringBuffer keyBuf = new StringBuffer();
        keyBuf.append(decodedPacket.getTerminalAddress()).append(Constant.SPLIT_SIGN_COLON)
                .append(decodedPacket.getControl().getSeq());
        String key = keyBuf.toString();
        if (Protocol3761Cache.HTTP_COMMAND_CHANNEL_ID_MAP.containsKey(key)) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("parameter", getHttpResponse(decodedPacket));
            responseMap.put("channelId", Protocol3761Cache.HTTP_COMMAND_CHANNEL_ID_MAP.get(key));
            HttpServerStart.httpResponseThreadPool.execute(new HttpResponseQueueServer(responseMap));
            //移除http通道缓存
            Protocol3761Cache.HTTP_COMMAND_CHANNEL_ID_MAP.remove(key);
        }
    }

    private CommandJsonResult getHttpResponse(Packet decodedPacket) {
        StringBuffer keyBuf = new StringBuffer();
        keyBuf.append(decodedPacket.getTerminalAddress()).append(Constant.SPLIT_SIGN_COLON).append(decodedPacket.getControl().getSeq());
        String key = keyBuf.toString();
        String sendCommand = Protocol3761Cache.TERMINAL_SEQ_SEND_DATA.get(key);
        String receiveCommand = Protocol3761Cache.TERMINAL_SEQ_RECEIVE_DATA.get(key);
        CommandJsonResult jsonResult = new CommandJsonResult(true, sendCommand, receiveCommand);
        if ("Deny".equals(decodedPacket.getCommand())) {
            jsonResult.setSuccess(false);
            jsonResult.setData("指令被拒绝执行");
            return jsonResult;
        }
        if ("Confirm".equals(decodedPacket.getCommand())) {
            return jsonResult;
        }
        try {
            jsonResult.setData(getResponseData(decodedPacket, sendCommand, receiveCommand));
        } catch (Exception e) {
            e.printStackTrace();
            return new CommandJsonResult(false, 10004, "集中器返回指令无法被解析", sendCommand, receiveCommand);
        }
        return jsonResult;
    }

    private Object getResponseData(Packet decodedPacket, String sendCommand, String receiveCommand) throws Exception {
        Map<String, Object> data = decodedPacket.getData();
        String address = decodedPacket.getTerminalAddress();
        if (data != null) {
            switch (CommandAfn.get(decodedPacket.getCommand())) {
                case GET_MAIN_STATION_IP:
                    CommandAfn4F3Dto f3Dto = commandService.getAfn0AF3CommandDto(data);
                    putCommandCacheData(address + Constant.SPLIT_SIGN_COLON + decodedPacket.getCommand(), f3Dto, sendCommand, receiveCommand);
                    return f3Dto;
                case GET_RESIDUAL_CURRENT_PROTECTOR_CONFIG:
                    Map f10Map = commandService.getAfn0AF10CommandResponse(data, address);
                    putCommandCacheData(address + Constant.SPLIT_SIGN_COLON + decodedPacket.getCommand(), f10Map, sendCommand, receiveCommand);
                    return f10Map;
                case GET_READ_RUNNING_PARAMS:
                    List f33List = commandService.getAfn0AF33CommandDto(data);
                    putCommandCacheData(address + Constant.SPLIT_SIGN_COLON + decodedPacket.getCommand(), f33List, sendCommand, receiveCommand);
                    return f33List;
                case GET_READ_TASK_PARAMS:
                    CommandAfn4F65Dto f65Dto = commandService.getAfn0AF65CommandDto(data);
                    putCommandCacheData(address + Constant.SPLIT_SIGN_COLON + decodedPacket.getCommand() + Constant.SPLIT_SIGN_COLON + decodedPacket.getLine(), f65Dto, sendCommand, receiveCommand);
                    return f65Dto;
                case GET_TASK_ON_OFF:
                    Map f67Map = commandService.getAfn0AF67CommandDto(data);
                    putCommandCacheData(address + Constant.SPLIT_SIGN_COLON + decodedPacket.getCommand() + Constant.SPLIT_SIGN_COLON + decodedPacket.getLine(), f67Map, sendCommand, receiveCommand);
                    return f67Map;
                case GET_REAL_TIME_DATA:
                    return commandService.getAfn0CF25CommandDto(data);
                case GET_ELEC_REAL_DATA:
                    return commandService.getAfn0CF129CommandDto(data);
                default:
                    return data;
            }
        }
        return null;
    }

    /**
     * 查询帧的内容放入缓存，前端接口缓存
     *
     * @param key
     * @param data
     * @param sendCommand
     * @param receiveCommand
     */
    private void putCommandCacheData(String key, Object data, String sendCommand, String receiveCommand) {
        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put("data", data);
        cacheMap.put("sendCommand", sendCommand);
        cacheMap.put("receiveCommand", receiveCommand);
        Protocol3761Cache.TERMINAL_GET_COMMAND_PACKET.put(key, cacheMap);
    }

    private void testSendStr() {

        String hex = "68 42 00 42 00 68 7B 01 01 BA 07 02 0C 68 00 00 04 01 00 00 10 01 CA 16  ";

        NettyStarter.sendTest(hex);
    }

    private void testSendStr2() {

        String[] ss = {

                "68 76 00 76 00 68 5A 01 01 BA 07 02 04 61 00 00 80 02 01 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F7 16",
                "68 A2 00 A2 00 68 7A 01 01 BA 07 02 04 62 00 00 10 03 31 32 33 34 35 36 00 00 00 00 00 00 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DD 16",
                "68 BE 00 BE 00 68 5A 01 01 BA 07 02 04 63 00 00 01 04 01 01 3F 00 FF FF FF FF 00 00 01 00 00 00 01 00 00 00 23 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 DD 16",
                "68 86 00 86 00 68 7A 01 01 BA 07 02 04 64 00 00 20 04 01 01 01 01 FF 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 BE 16",
                "68 E6 00 E6 00 68 5A 01 01 BA 07 02 04 65 00 00 02 01 01 00 01 00 01 00 21 00 66 55 44 33 22 11 22 22 22 11 11 11 00 05 66 66 66 33 22 11 31 67 89 00 00 00 00 00 00 00 00 00 00 00 00 00 00 6B 16"
        };
        for (String s : ss) {
            NettyStarter.sendTest(s);
        }
    }
}
