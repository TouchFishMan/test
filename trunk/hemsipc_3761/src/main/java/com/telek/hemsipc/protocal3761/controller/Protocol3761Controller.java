package com.telek.hemsipc.protocal3761.controller;

import com.google.gson.reflect.TypeToken;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F10Dto;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F33Dto;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F3Dto;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F65Dto;
import com.telek.hemsipc.protocal3761.service.ProtocolCommandService;
import com.telek.hemsipc.util.JsonResult;
import com.telek.hemsipc.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangxb
 * @date 20-1-8 下午2:44
 */
@Component
public class Protocol3761Controller extends BaseController {
    @Autowired
    ProtocolCommandService commandService;

    //        @MethodName(value = "commandSetF3")
    public JsonResult commandSetF3(Object parameter, String terminalAddress, String channelId) throws Exception {
        CommandAfn4F3Dto afn4F3Dto = resolveParams(parameter, CommandAfn4F3Dto.class);
        String errorMsg = valid(afn4F3Dto);
        if (!StringUtil.isBlank(errorMsg)) {
            return new JsonResult(false, 10001, errorMsg);
        }
        commandService.sendAfn4F3Command(afn4F3Dto, terminalAddress, channelId);
        return null;
    }

    public JsonResult commandSetF10(Object parameter, String terminalAddress, String channelId) throws Exception {
        TypeToken<ArrayList<CommandAfn4F10Dto>> typeToken = new TypeToken<ArrayList<CommandAfn4F10Dto>>() {
        };
        List<CommandAfn4F10Dto> afn4F10DtoList = StringUtil.strToBeans(
                StringUtil.toString(parameter), typeToken);
        for (CommandAfn4F10Dto f10Dto : afn4F10DtoList) {
            String errorMsg = valid(f10Dto);
            if (!StringUtil.isBlank(errorMsg)) {
                return new JsonResult(false, 10001, errorMsg);
            }
        }
        return commandService.handleHttpF10Command(afn4F10DtoList, terminalAddress, channelId);
    }

    public JsonResult commandSetF33(Object parameter, String terminalAddress, String channelId) throws Exception {
        TypeToken<ArrayList<CommandAfn4F33Dto>> typeToken = new TypeToken<ArrayList<CommandAfn4F33Dto>>() {
        };
        List<CommandAfn4F33Dto> afn4F33DtoList = StringUtil.strToBeans(
                StringUtil.toString(parameter), typeToken);
        for (CommandAfn4F33Dto f33Dto : afn4F33DtoList) {
            String errorMsg = valid(f33Dto);
            if (!StringUtil.isBlank(errorMsg)) {
                return new JsonResult(false, 10001, errorMsg);
            }
        }
        return commandService.handleHttpF33Command(afn4F33DtoList, terminalAddress, channelId);
    }

    public JsonResult commandSetF65(Object parameter, String terminalAddress, String channelId) throws Exception {
        CommandAfn4F65Dto f65Dto = resolveParams(parameter, CommandAfn4F65Dto.class);
        return commandService.handleHttpF65Command(f65Dto, terminalAddress, channelId);
    }

    public JsonResult commandSetF67(Object parameter, String terminalAddress, String channelId) throws Exception {
        Map f67Dto = resolveParams(parameter, Map.class);
        if (f67Dto == null || !f67Dto.containsKey("taskOnOff") || !f67Dto.containsKey("taskId")) {
            return new JsonResult(false, 10001, "参数错误");
        }
        commandService.sendAfn4F67Command(((Double) f67Dto.get("taskId")).intValue(),
                (Boolean) f67Dto.get("taskOnOff"), terminalAddress, channelId);
        return null;
    }

    public JsonResult commandGetF3(Object parameter, String terminalAddress, String channelId) throws Exception {
        return commandService.handleSendGetHttpF3Command(terminalAddress, channelId);
    }

    public JsonResult commandGetF10(Object parameter, String terminalAddress, String channelId) throws Exception {
        return commandService.handleSendGetHttpF10Command(terminalAddress, channelId);
    }

    public JsonResult commandGetF33(Object parameter, String terminalAddress, String channelId) throws Exception {
        return commandService.handleSendGetHttpF33Command(terminalAddress, channelId);
    }

    public JsonResult commandGetF65(Object parameter, String terminalAddress, String channelId) throws Exception {
        Map<String, Object> params = resolveParams(parameter, Map.class);
        if (params == null || !params.containsKey("taskId")) {
            return new JsonResult(false, 10001, "参数错误");
        }
        return commandService.handleSendGetHttpF65Command(terminalAddress, channelId, ((Double) params.get("taskId")).intValue());
    }

    public JsonResult commandGetF67(Object parameter, String terminalAddress, String channelId) throws Exception {
        Map<String, Object> params = resolveParams(parameter, Map.class);
        if (params == null || !params.containsKey("taskId")) {
            return new JsonResult(false, 10001, "参数错误");
        }
        return commandService.handleSendGetF67Command(terminalAddress, channelId, ((Double) params.get("taskId")).intValue());
    }

    public JsonResult commandGetF25(Object parameter, String terminalAddress, String channelId) throws Exception {
        Map<String, Object> params = resolveParams(parameter, Map.class);
        if (params == null || !params.containsKey("pn")) {
            return new JsonResult(false, 10001, "参数错误");
        }
        commandService.sendGetF25Command(terminalAddress, channelId, ((Double) (params.get("pn"))).intValue());
        return null;
    }

    public JsonResult commandGetF129(Object parameter, String terminalAddress, String channelId) throws Exception {
        Map<String, Object> params = resolveParams(parameter, Map.class);
        if (params == null || !params.containsKey("pn")) {
            return new JsonResult(false, 10001, "参数错误");
        }
        commandService.sendGetF129Command(terminalAddress, channelId, ((Double) (params.get("pn"))).intValue());
        return null;
    }
}