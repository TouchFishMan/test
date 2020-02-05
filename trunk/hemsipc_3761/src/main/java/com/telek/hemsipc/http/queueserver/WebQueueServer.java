/**
 *
 */
package com.telek.hemsipc.http.queueserver;

import com.telek.hemsipc.context.HemsipcSpringContext;
import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.controller.ConcentratorController;
import com.telek.hemsipc.protocal3761.controller.Protocol3761Controller;
import com.telek.hemsipc.http.HttpServerStart;
import com.telek.hemsipc.protocal3761.service.ProtocolCommandService;
import com.telek.hemsipc.service.IDeviceService;
import com.telek.hemsipc.service.impl.DeviceServiceImpl;
import com.telek.hemsipc.util.JsonResult;
import com.telek.hemsipc.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangxb
 * @date 20-1-2 下午5:10
 */
public class WebQueueServer implements Runnable {
    private static final Log LOG = LogFactory.getLog(WebQueueServer.class);
    private Map<String, String> map = null;

    private IDeviceService deviceService = HemsipcSpringContext.getBean(DeviceServiceImpl.class);

    private ProtocolCommandService commandService = HemsipcSpringContext.getBean(ProtocolCommandService.class);

    private ConcentratorController concentratorController = HemsipcSpringContext.getBean(ConcentratorController.class);

    private Protocol3761Controller protocol3761Controller = HemsipcSpringContext.getBean(Protocol3761Controller.class);

    public WebQueueServer() {
    }

    public WebQueueServer(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public void run() {
        try {
            process(map);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void process(Map<String, String> webMap) throws Exception {
        Object object = null;
        try {
            object = doService(webMap);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        //object为null时暂不做响应，等待集中器通信指令完成再回复
        if (object == null) {
            return;
        }
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("parameter", object);
        responseMap.put("channelId", webMap.get("channelId"));
        HttpServerStart.httpResponseThreadPool.execute(new HttpResponseQueueServer(responseMap));
    }

    private Object doService(Map webMap) throws Exception {
        LOG.info("接收到http请求：" + StringUtil.toString(webMap));
        try {
            String methodName = StringUtil.toString(webMap.get("methodName"));
            Object parameter = webMap.get("parameter");
            if (methodName.isEmpty()) {
                return new JsonResult(false, 10001, "缺少方法名参数");
            }
            if (methodName.indexOf("command") == 0) {
                String terminalAddress = StringUtil.toString(webMap.get("terminalAddress"));
                return dealCommandRequest(methodName, parameter, terminalAddress, webMap.get("channelId").toString());
            } else {
                return dealNormalRequest(methodName, parameter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(false, 10000, "系统繁忙");
        }
    }

    private Object dealCommandRequest(String methodName, Object parameter, String terminalAddress, String channelId) {
        try {
            if (StringUtil.isBlank(terminalAddress) || !Protocol3761Cache.TERMINAL_ADDRESS_LOGIN.containsKey(terminalAddress)) {
                return new JsonResult(false, 10001, "集中器未连接线或地址错误");
            }
            switch (methodName) {
                case "commandSetF3":
                    return protocol3761Controller.commandSetF3(parameter, terminalAddress, channelId);
                case "commandSetF7":
                    return null;
                case "commandSetF10":
                    return protocol3761Controller.commandSetF10(parameter, terminalAddress, channelId);
                case "commandSetF33":
                    return protocol3761Controller.commandSetF33(parameter, terminalAddress, channelId);
                case "commandSetF65":
                    return protocol3761Controller.commandSetF65(parameter,terminalAddress,channelId);
                case "commandSetF67":
                    return protocol3761Controller.commandSetF67(parameter,terminalAddress,channelId);
                case "commandGetF3":
                    return protocol3761Controller.commandGetF3(parameter,terminalAddress,channelId);
                case "commandGetF7":
                    return null;
                case "commandGetF10":
                    return protocol3761Controller.commandGetF10(parameter,terminalAddress,channelId);
                case "commandGetF33":
                    return protocol3761Controller.commandGetF33(parameter,terminalAddress,channelId);
                case "commandGetF65":
                    return protocol3761Controller.commandGetF65(parameter,terminalAddress,channelId);
                case "commandGetF67":
                    return protocol3761Controller.commandGetF67(parameter,terminalAddress,channelId);
                case "commandGetF25":
                    return protocol3761Controller.commandGetF25(parameter,terminalAddress,channelId);
                case "commandGetF129":
                   return protocol3761Controller.commandGetF129(parameter,terminalAddress,channelId);
                default:
                    return new JsonResult(false, 10001, "无法解析的methodName");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult(false, 10000, "系统繁忙");
        }
    }

    private Object dealNormalRequest(String methodName, Object parameter) {
        try {
            switch (methodName) {
                case "queryDeviceList": {
                   return concentratorController.queryDeviceList(parameter);
                }
                case "createConcentrator": {
                    return concentratorController.createConcentrator(parameter);
                }
                case "updateConcentrator": {
                    return concentratorController.updateConcentrator(parameter);
                }
                case "deleteConcentrator": {
                    return concentratorController.deleteConcentrator(parameter);
                }
                case "replaceConcentrator": {
                    return concentratorController.replaceConcentrator(parameter);
                }
                case "createElecMeter": {
                    return concentratorController.createElecMeter(parameter);
                }
                case "updateElecMeter": {
                    return concentratorController.updateElecMeter(parameter);
                }
//                case "deleteElecMeter": {
//                    Map<String, Object> concentrator = resolveParams(parameter, Map.class);
//                    return deviceService.deleteElecMeter(StringUtil.toString(concentrator.get("deviceId")));
//                }
                case "queryTaskIds": {
                   return concentratorController.queryTaskIds(parameter);
                }
                case "getFnPnMap": {
                    return concentratorController.getFnPnMap(parameter);
                }
                default:
                    return new JsonResult(false, 10001, "无法解析的methodName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult(false, 10000, "系统繁忙");
    }
}
