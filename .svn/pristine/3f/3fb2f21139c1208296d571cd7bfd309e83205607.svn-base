package com.telek.hemsipc.protocal3761.controller;

import com.google.gson.reflect.TypeToken;
import com.telek.hemsipc.http.MethodName;
import com.telek.hemsipc.model.Concentrator;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F10Dto;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F33Dto;
import com.telek.hemsipc.protocal3761.dto.CommandAfn4F3Dto;
import com.telek.hemsipc.protocal3761.service.ProtocolCommandService;
import com.telek.hemsipc.service.IDeviceService;
import com.telek.hemsipc.util.JsonResult;
import com.telek.hemsipc.util.StringUtil;
import com.telek.smarthome.cloudserver.hemstools.control.service.CommandSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangxb
 * @date 20-1-8 下午2:45
 */
@Component
public class ConcentratorController extends BaseController {

    @Autowired
    IDeviceService deviceService;

    public JsonResult queryDeviceList(Object parameter) throws Exception {
        Map<String, Object> params = resolveParams(parameter, Map.class);
        String name = null;
        if (params != null && params.containsKey("name")) {
            name = StringUtil.toString(params.get("name"));
        }
        return new JsonResult(true, 0, deviceService.queryDeviceList(name));
    }

    public JsonResult createConcentrator(Object parameter) throws Exception {
        Concentrator concentrator = resolveParams(parameter, Concentrator.class);
        return deviceService.createConcentrator(concentrator);
    }

    public JsonResult updateConcentrator(Object parameter) throws Exception {
        Concentrator concentrator = resolveParams(parameter, Concentrator.class);
        return deviceService.updateConcentrator(concentrator);
    }

    public JsonResult deleteConcentrator(Object parameter) throws Exception {
        Map<String, Object> params = resolveParams(parameter, Map.class);
        if (params == null || !params.containsKey("idList")) {
            return new JsonResult(false, 10001, "参数错误");
        }
        TypeToken<ArrayList<Integer>> typeToken = new TypeToken<ArrayList<Integer>>() {
        };
        List<Integer> idList = StringUtil.strToBeans(
                StringUtil.toString(params.get("idList")), typeToken);
        return deviceService.deleteConcentrator(idList);
    }

    public JsonResult replaceConcentrator(Object parameter) throws Exception {
        Concentrator concentrator = resolveParams(parameter, Concentrator.class);
        return deviceService.replaceConcentrator(concentrator);
    }

    public JsonResult createElecMeter(Object parameter) throws Exception {
        Device elecMeter = resolveParams(parameter, Device.class);
        return deviceService.createElecMeter(elecMeter);
    }

    public JsonResult updateElecMeter(Object parameter) throws Exception {
        Device elecMeter = resolveParams(parameter, Device.class);
        return deviceService.updateElecMeter(elecMeter);
    }

    public JsonResult queryTaskIds(Object parameter) throws Exception {
        Map<String, Object> params = resolveParams(parameter, Map.class);
        if (params == null || !params.containsKey("id")) {
            return new JsonResult(false, 10001, "参数错误");
        }
        return deviceService.queryTaskIds(((Double) (params.get("id"))).intValue());
    }

    public JsonResult getFnPnMap(Object parameter) throws Exception {
        Map<String, Object> params = resolveParams(parameter, Map.class);
        if (params == null || !params.containsKey("terminalId")) {
            return new JsonResult(false, 10001, "参数错误");
        }
        return deviceService.getFnPnMapList(((Double) (params.get("terminalId"))).intValue());
    }
}
