package com.telek.hemsipc.protocal3761.service;

import com.google.gson.reflect.TypeToken;
import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.model.Concentrator;
import com.telek.hemsipc.model.Device;
import com.telek.hemsipc.protocal3761.Protocol3761Cache;
import com.telek.hemsipc.protocal3761.dto.*;
import com.telek.hemsipc.protocal3761.protocal.constant.CommandAfn;
import com.telek.hemsipc.protocal3761.protocal.constant.ProcotolTypeConst;
import com.telek.hemsipc.protocal3761.protocal.constant.SpeedConst;
import com.telek.hemsipc.protocal3761.service.request.*;
import com.telek.hemsipc.repository.IDeviceDao;
import com.telek.hemsipc.service.IDeviceService;
import com.telek.hemsipc.util.CommandJsonResult;
import com.telek.hemsipc.util.DateUtil;
import com.telek.hemsipc.util.JsonResult;
import com.telek.hemsipc.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangxb
 * @date 20-1-8 下午3:15
 */
@Service
public class ProtocolCommandService {

    @Autowired
    IDeviceDao deviceDao;

    @Autowired
    IDeviceService deviceService;

    public void sendAfn4F3Command(CommandAfn4F3Dto afn4F3Dto, String terminalAddress, String channelId) throws Exception {
        removeCommandCacheData(terminalAddress, CommandAfn.GET_MAIN_STATION_IP);
        SetAfn4F3MainStationIp afn4F3MainStationIp = new SetAfn4F3MainStationIp(true, afn4F3Dto);
        byte[] sendData = afn4F3MainStationIp.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    /**
     * 处理发送 AFN04F10 帧，设置F10电表参数
     *
     * @param afn4F10DtoList
     * @param terminalAddress
     * @param channelId
     * @return
     * @throws Exception
     */
    public JsonResult handleHttpF10Command(List<CommandAfn4F10Dto> afn4F10DtoList, String terminalAddress, String channelId) throws Exception {
        if (afn4F10DtoList == null || afn4F10DtoList.size() == 0) {
            return new JsonResult(false, 10002, "至少保留一条配置");
        }
        Set<Integer> measuringPointSet = new HashSet();
        for (CommandAfn4F10Dto f10Dto : afn4F10DtoList) {
            if (measuringPointSet.contains(f10Dto.getMeasuringPoint())) {
                return new JsonResult(false, 10003, "设置的测量点号不能重复");
            }
            measuringPointSet.add(f10Dto.getMeasuringPoint());
        }
        removeCommandCacheData(terminalAddress, CommandAfn.GET_RESIDUAL_CURRENT_PROTECTOR_CONFIG);
        sendAfn4F10Command(afn4F10DtoList, terminalAddress, channelId);
        deviceService.bindElecMeter(terminalAddress, afn4F10DtoList);
        return null;
    }

    public void sendAfn4F10Command(List<CommandAfn4F10Dto> afn4F10DtoList, String terminalAddress, String channelId) throws Exception {
        SetAfn4F10Params afn4F10Params = new SetAfn4F10Params(true, afn4F10DtoList);
        byte[] sendData = afn4F10Params.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    /**
     * 处理发送 AFN04F33 帧，设置F33读表运行参数
     *
     * @param afn4F33DtoList
     * @param terminalAddress
     * @param channelId
     * @return
     * @throws Exception
     */
    public JsonResult handleHttpF33Command(List<CommandAfn4F33Dto> afn4F33DtoList, String terminalAddress, String channelId) throws Exception {
        if (afn4F33DtoList == null || afn4F33DtoList.size() == 0) {
            return new JsonResult(false, 10002, "至少保留一条配置");
        }
        Set<Integer> portSet = new HashSet<>();
        for (CommandAfn4F33Dto f33Dto : afn4F33DtoList) {
            if (portSet.contains(f33Dto.getTerminalPort())) {
                return new JsonResult(false, 10003, "设置的端口号不能重复");
            }
            portSet.add(f33Dto.getTerminalPort());
        }
        removeCommandCacheData(terminalAddress, CommandAfn.GET_READ_RUNNING_PARAMS);
        sendAfn4F33Command(afn4F33DtoList, terminalAddress, channelId);
        List<Integer> portList = new ArrayList<>();
        for (CommandAfn4F33Dto f33Dto : afn4F33DtoList) {
            portList.add(f33Dto.getTerminalPort());
        }
        deviceService.updateConcentratorBindPorts(terminalAddress, portList);
        return null;
    }

    public void sendAfn4F33Command(List<CommandAfn4F33Dto> afn4F33DtoList, String terminalAddress, String channelId) throws Exception {
        SetAfn4F33ReadParams afn4F33ReadParams = new SetAfn4F33ReadParams(true, afn4F33DtoList);
        byte[] sendData = afn4F33ReadParams.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    /**
     * 处理发送 AFN04F65 帧，设置F65任务参数
     *
     * @param afn4F65Dto
     * @param terminalAddress
     * @param channelId
     * @throws Exception
     */
    public JsonResult handleHttpF65Command(CommandAfn4F65Dto afn4F65Dto, String terminalAddress, String channelId) throws Exception {
        removeCommandCacheData(terminalAddress + Constant.SPLIT_SIGN_COLON +
                CommandAfn.GET_READ_TASK_PARAMS.getCommand() + Constant.SPLIT_SIGN_COLON + (afn4F65Dto.getTaskId()));
        sendAfn4F65Command(afn4F65Dto, terminalAddress, channelId);
        deviceService.updateConcentratorTaskId(terminalAddress, afn4F65Dto.getTaskId());
        return null;
    }

    public void sendAfn4F65Command(CommandAfn4F65Dto afn4F65Dto, String terminalAddress, String channelId) throws Exception {
        //转成A19格式时间 19/12/31 12:10:04 2
        Date date = DateUtil.dateParse(afn4F65Dto.getBaseTime(), DateUtil.timeformat);
        String formatBaseTime = DateUtil.dateFormat(date, DateUtil.timeformat2);
        int week = DateUtil.getDayOfWeek(date.getTime());
        String baseTimeWithWeek = formatBaseTime.substring(2, formatBaseTime.length() - 1) + " " + week;
        afn4F65Dto.setBaseTime(baseTimeWithWeek);
        SetAfn4F65ReadTaskParams afn4F65ReadTaskParams = new SetAfn4F65ReadTaskParams(true, afn4F65Dto);
        byte[] sendData = afn4F65ReadTaskParams.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    public void sendAfn4F67Command(int taskId, boolean onOff, String terminalAddress, String channelId) throws Exception {
        SetAfn4F67ReadTaskOnOff f67ReadTaskOnOff = new SetAfn4F67ReadTaskOnOff(true, taskId, onOff);
        byte[] sendData = f67ReadTaskOnOff.dealSendToTerminal(terminalAddress, null);
        deviceService.updateConcentratorTaskId(terminalAddress, taskId);
        updateChannelCache(terminalAddress, sendData, channelId);
        removeCommandCacheData(terminalAddress + Constant.SPLIT_SIGN_COLON +
                CommandAfn.GET_TASK_ON_OFF.getCommand() + Constant.SPLIT_SIGN_COLON + taskId);
    }

    /**
     * 处理发送 AFN0AF3 帧，获取F3主站ip信息
     *
     * @param terminalAddress
     * @param channelId
     * @return
     * @throws Exception
     */
    public CommandJsonResult handleSendGetHttpF3Command(String terminalAddress, String channelId) throws Exception {
        CommandJsonResult result = getCommandCacheData(terminalAddress, CommandAfn.GET_MAIN_STATION_IP);
        if (result != null) {
            return result;
        }
        sendGetF3Command(terminalAddress, channelId);
        return null;
    }

    public void sendGetF3Command(String terminalAddress, String channelId) throws Exception {
        GetAfc4F3MainStationIp getAfc4F3MainStationIp = new GetAfc4F3MainStationIp(true);
        byte[] sendData = getAfc4F3MainStationIp.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    /**
     * 处理发送 AFN0AF10 帧，获取F10电表参数
     *
     * @param terminalAddress
     * @param channelId
     * @return
     * @throws Exception
     */
    public JsonResult handleSendGetHttpF10Command(String terminalAddress, String channelId) throws Exception {
        CommandJsonResult result = getCommandCacheData(terminalAddress, CommandAfn.GET_RESIDUAL_CURRENT_PROTECTOR_CONFIG);
        if (result != null) {
            return result;
        }
        List<Device> elecMeterList = deviceDao.queryElecMeterBySlaveAdd(terminalAddress);
        if (elecMeterList == null || elecMeterList.size() == 0) {
            Map<String, Object> resultMap = getProtocolTypeMapAndSpeedMapForF10();
            resultMap.put("params", new ArrayList());
            return new JsonResult(true, 0, resultMap);
        }
        List<Integer> indexList = new ArrayList<>();
        for (Device elecMeter : elecMeterList) {
            indexList.add(elecMeter.getSlaveMachineNum());
        }
        sendGetF10Command(terminalAddress, channelId, indexList);
        return null;
    }

    public void sendGetF10Command(String terminalAddress, String channelId, List<Integer> indexList) throws Exception {
        GetAfc4F10Params getAfc4F10Params = new GetAfc4F10Params(true, indexList);
        byte[] sendData = getAfc4F10Params.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    /**
     * 处理发送 AFN0AF33 帧，获取F33读表运行参数
     *
     * @param terminalAddress
     * @param channelId
     * @return
     * @throws Exception
     */
    public JsonResult handleSendGetHttpF33Command(String terminalAddress, String channelId) throws Exception {
        CommandJsonResult result = getCommandCacheData(terminalAddress, CommandAfn.GET_READ_RUNNING_PARAMS);
        if (result != null) {
            return result;
        }
        List<Concentrator> concentratorList = deviceDao.queryConcentratorByParams(null, terminalAddress);
        List<Integer> portList = new ArrayList<>();
        if (concentratorList != null && concentratorList.size() > 0) {
            if (!StringUtil.isBlank(concentratorList.get(0).getBindPorts())) {
                String[] indexArr = concentratorList.get(0).getBindPorts().split(Constant.SPLIT_SIGN_COMMA);
                if (indexArr.length > 0) {
                    for (int i = 0; i < indexArr.length; i++) {
                        portList.add(StringUtil.parseInt(indexArr[i]));
                    }
                }
            }
        }
        if (portList.size() == 0) {
            return new JsonResult(true, 0, new ArrayList());
        }
        sendGetF33Command(terminalAddress, channelId, portList);
        return null;
    }

    public void sendGetF33Command(String terminalAddress, String channelId, List<Integer> portList) throws Exception {
        GetAfc4F33ReadParams getAfc4F33ReadParams = new GetAfc4F33ReadParams(true, portList);
        byte[] sendData = getAfc4F33ReadParams.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    /**
     * 处理发送 AFN0AF65 帧，获取F65任务运行参数
     *
     * @param terminalAddress
     * @param channelId
     * @param taskId          任务id，一次获取一个任务的参数
     * @return
     * @throws Exception
     */
    public JsonResult handleSendGetHttpF65Command(String terminalAddress, String channelId, int taskId) throws Exception {
        CommandJsonResult result = getCommandCacheData(terminalAddress + Constant.SPLIT_SIGN_COLON
                + CommandAfn.GET_READ_TASK_PARAMS.getCommand() + Constant.SPLIT_SIGN_COLON + taskId);
        if (result != null) {
            return result;
        }
        if (!deviceService.checkConcentratorTaskId(terminalAddress, taskId)) {
            return new JsonResult(false, 10002, "该集中器未配置当前任务");
        }
        sendGetF65Command(terminalAddress, channelId, taskId);
        return null;
    }

    public void sendGetF65Command(String terminalAddress, String channelId, int pn) throws Exception {
        GetAfc4F65ReadTaskParams getAfc4F65ReadTaskParams = new GetAfc4F65ReadTaskParams(true, pn);
        byte[] sendData = getAfc4F65ReadTaskParams.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    /**
     * 处理发送 AFN0AF67 帧，获取F67任务开关
     *
     * @param terminalAddress
     * @param channelId
     * @param taskId          任务id，一次获取一个任务的开关状态
     * @return
     * @throws Exception
     */
    public JsonResult handleSendGetF67Command(String terminalAddress, String channelId, int taskId) throws Exception {
        CommandJsonResult result = getCommandCacheData(terminalAddress + Constant.SPLIT_SIGN_COLON
                + CommandAfn.GET_TASK_ON_OFF.getCommand() + Constant.SPLIT_SIGN_COLON + taskId);
        if (result != null) {
            return result;
        }
        if (!deviceService.checkConcentratorTaskId(terminalAddress, taskId)) {
            return new JsonResult(false, 10002, "该集中器未配置当前任务");
        }
        sendGetF67Command(terminalAddress, channelId, taskId);
        return null;
    }

    public void sendGetF67Command(String terminalAddress, String channelId, int pn) throws Exception {
        GetAfc4F67TaskOnOff getAfc4F67TaskOnOff = new GetAfc4F67TaskOnOff(true, pn);
        byte[] sendData = getAfc4F67TaskOnOff.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    public void sendGetF25Command(String terminalAddress, String channelId, int pn) throws Exception {
        GetAfc0CF25RealTimeData getAfc0CF25RealTimeData = new GetAfc0CF25RealTimeData(true, pn);
        byte[] sendData = getAfc0CF25RealTimeData.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    public void sendGetF129Command(String terminalAddress, String channelId, int pn) throws Exception {
        GetAfc0CF129ElecData afc0CF129ElecData = new GetAfc0CF129ElecData(true, pn);
        byte[] sendData = afc0CF129ElecData.dealSendToTerminal(terminalAddress, null);
        updateChannelCache(terminalAddress, sendData, channelId);
    }

    /**
     * 封装得到的主站ip查询帧，返回dto
     *
     * @param data
     * @return
     * @throws Exception
     */
    public CommandAfn4F3Dto getAfn0AF3CommandDto(Map<String, Object> data) throws Exception {
        String main = StringUtil.toString(data.get("mainIp"));
        String sub = StringUtil.toString(data.get("subIp"));
        String apn = StringUtil.toString(data.get("APN"));
        CommandAfn4F3Dto afn4F3Dto = new CommandAfn4F3Dto();
        if (!StringUtil.isBlank(main)) {
            String[] mainArr = main.split(Constant.SPLIT_SIGN_COLON);
            afn4F3Dto.setMainIp(mainArr[0]);
            afn4F3Dto.setMainPort(StringUtil.parseInt(mainArr[1]));
        }
        if (!StringUtil.isBlank(sub)) {
            String[] subArr = sub.split(Constant.SPLIT_SIGN_COLON);
            afn4F3Dto.setSubIp(subArr[0]);
            afn4F3Dto.setSubPort(StringUtil.parseInt(subArr[1]));
        }
        afn4F3Dto.setAPN(apn);
        return afn4F3Dto;
    }

    /**
     * 封装接收的终端电表参数，返回dto
     *
     * @param data
     * @return
     * @throws Exception
     */
    public List<CommandAfn4F10Dto> getAfn0AF10CommandDto(Map<String, Object> data) throws Exception {
        String str = StringUtil.toString(data.get("list"));
        if (StringUtil.isBlank(str)) {
            return null;
        }
        TypeToken<ArrayList<CommandAfn4F10Dto>> typeToken = new TypeToken<ArrayList<CommandAfn4F10Dto>>() {
        };
        return StringUtil.strToBeans(StringUtil.toString(str), typeToken);
    }

    public Map<String, Object> getAfn0AF10CommandResponse(Map<String, Object> data, String terminalAddress) throws Exception {
        List<CommandAfn4F10Dto> dtoList = getAfn0AF10CommandDto(data);
        List<Device> elecMeterList = deviceDao.queryElecMeterBySlaveAdd(terminalAddress);
        //塞电表其它信息，电表数据库没存电表地址，通过集中器地址+测量点号判断
        for (CommandAfn4F10Dto f10Dto : dtoList) {
            for (Device elecMeter : elecMeterList) {
                if (f10Dto.getMeasuringPoint().equals(elecMeter.getSlaveMachineNum())) {
                    f10Dto.setName(elecMeter.getDeviceName());
                    f10Dto.setLocation(elecMeter.getDetail());
                    f10Dto.setDeviceId(elecMeter.getDeviceId());
                }
            }
        }
        Map<String, Object> resultMap = getProtocolTypeMapAndSpeedMapForF10();
        resultMap.put("params", dtoList);
        return resultMap;
    }

    public List<CommandAfn4F33Dto> getAfn0AF33CommandDto(Map<String, Object> data) throws Exception {
        String str = StringUtil.toString(data.get("list"));
        if (StringUtil.isBlank(str)) {
            return null;
        }
        String listStr = handleTimeFormatString(StringUtil.toString(str));
        TypeToken<ArrayList<CommandAfn4F33Dto>> typeToken = new TypeToken<ArrayList<CommandAfn4F33Dto>>() {
        };
        List<CommandAfn4F33Dto> f33DtoList = StringUtil.strToBeans(listStr, typeToken);
        TypeToken<ArrayList<F33ReadSettingParams>> typeToken2 = new TypeToken<ArrayList<F33ReadSettingParams>>() {
        };
        List<F33ReadSettingParams> readSettingParams = StringUtil.strToBeans(listStr, typeToken2);
        for (int i = 0; i < f33DtoList.size(); i++) {
            f33DtoList.get(i).setReadSetting(readSettingParams.get(i));
        }
        return f33DtoList;
        //912311   720001
    }

    /**
     * F33返回帧类似[{terminalPort=1, notAllowAutoRead=0, readImportantMeterOnly=0, broadcast=0, checkTimeRegular=1, updateNewMeter=0, readMeterStatus=1, readDate=7fffffff, readTime=12:0, intervalTime=1, checkTime=0 12:0, allowReadTimeNum=1, list=[{allowReadTimeStart=0:0, allowReadTimeEnd=24:0}]}]";
     * 把12:0变成'12:0'，不然转不出来
     *
     * @param str
     * @return
     */
    private String handleTimeFormatString(String str) {
        String regEx = "=[\\d\\s]{0,5}:[\\d]{0,2}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        StringBuffer result = new StringBuffer(str);
        while (m.find()) {
            String oldStr = m.group();
            int i = result.indexOf(oldStr);
            result.replace(i + 1, i + oldStr.length(), "'" + oldStr.substring(1) + "'");
        }
        return result.toString();
    }

    public CommandAfn4F65Dto getAfn0AF65CommandDto(Map<String, Object> data) throws Exception {
        CommandAfn4F65Dto f65Dto = StringUtil.mapToObject(data, CommandAfn4F65Dto.class);
        List taskList = (List) data.get("list");
        f65Dto.setTaskList(taskList);
        String baseTime = f65Dto.getBaseTime();
        //接收到的baseTime：19/12/28 1:5:0 6 转换为 2019-12-28 01:05:00
        if (!StringUtil.isBlank(baseTime) && baseTime.length() > 2) {
            baseTime = baseTime.substring(0, baseTime.length() - 2);
            baseTime = "20" + baseTime;
            Date date = DateUtil.dateParse(baseTime, DateUtil.timeformat2);
            baseTime = DateUtil.dateFormat(date, DateUtil.timeformat);
            f65Dto.setBaseTime(baseTime);
        }
        return f65Dto;
    }

    public Map<String, Boolean> getAfn0AF67CommandDto(Map<String, Object> data) throws Exception {
        int onOff = (int) data.get("taskOnOff");
        Map<String, Boolean> map = new HashMap<>();
        map.put("taskOnOff", onOff == 85);
        return map;
    }

    public CommandAfn0CF25Dto getAfn0CF25CommandDto(Map<String, Object> data) throws Exception {
        CommandAfn0CF25Dto f25Dto = StringUtil.mapToObject(data, CommandAfn0CF25Dto.class);
        String terminalTime = f25Dto.getTerminalTime();
        //接收到的terminalTime：19/12/28 1:5 转换为 2019-12-28 01:05:00
        f25Dto.setTerminalTime(DateUtil.formatA15DateTime(terminalTime));
        return f25Dto;
    }

    public CommandAfn0CF129Dto getAfn0CF129CommandDto(Map<String, Object> data) throws Exception {
        CommandAfn0CF129Dto f129Dto = StringUtil.mapToObject(data, CommandAfn0CF129Dto.class);
        String terminalTime = f129Dto.getTerminalTime();
        //接收到的terminalTime：19/12/28 1:5 转换为 2019-12-28 01:05:00
        f129Dto.setTerminalTime(DateUtil.formatA15DateTime(terminalTime));
        return f129Dto;
    }

    /**
     * 获取指令帧的序号
     *
     * @param commandData
     * @return
     */
    private int getSeqByCommand(byte[] commandData) {
        if (commandData == null || commandData.length < 14) {
            return -1;
        } else {
            return commandData[13] & 0x0f;
        }
    }

    private void updateChannelCache(String terminalAddress, byte[] data, String channelId) {
        int seq = getSeqByCommand(data);
        StringBuffer key = new StringBuffer();
        key.append(terminalAddress).append(Constant.SPLIT_SIGN_COLON).append(seq);
        //http通道加入缓存
        Protocol3761Cache.HTTP_COMMAND_CHANNEL_ID_MAP.put(key.toString(), channelId);
        //发送的帧加入缓存
        Protocol3761Cache.TERMINAL_SEQ_SEND_DATA.put(key.toString(), StringUtil.byteArrToString(data));
    }

    /**
     * 获取查询指令F3、F10等接口时从缓存拿
     *
     * @param terminalAddress
     * @param command
     * @return
     */
    private CommandJsonResult getCommandCacheData(String terminalAddress, CommandAfn command) {
        String key = terminalAddress + Constant.SPLIT_SIGN_COLON + command.getCommand();
        return getCommandCacheData(key);
    }

    private CommandJsonResult getCommandCacheData(String key) {
        Map<String, Object> map = Protocol3761Cache.TERMINAL_GET_COMMAND_PACKET.get(key);
        if (map != null && map.size() > 0) {
            return new CommandJsonResult(true, 0, map.get("data"),
                    StringUtil.toString(map.get("sendCommand")), StringUtil.toString(map.get("receiveCommand")));
        }
        return null;
    }

    /**
     * 移除获取查询指令F3、F10等的缓存信息
     *
     * @param terminalAddress
     * @param command
     */
    private void removeCommandCacheData(String terminalAddress, CommandAfn command) {
        String key = terminalAddress + Constant.SPLIT_SIGN_COLON + command.getCommand();
        removeCommandCacheData(key);
    }

    private void removeCommandCacheData(String key) {
        if (Protocol3761Cache.TERMINAL_GET_COMMAND_PACKET.containsKey(key)) {
            Protocol3761Cache.TERMINAL_GET_COMMAND_PACKET.remove(key);
        }
    }

    private Map<String, Object> getProtocolTypeMapAndSpeedMapForF10() {
        Map<Integer, String> protocolTypeMap = new HashMap<>();
        for (ProcotolTypeConst protocol : ProcotolTypeConst.values()) {
            protocolTypeMap.put(protocol.getCode(), protocol.getMsg());
        }
        Map<Integer, Integer> speedMap = new HashMap<>();
        for (SpeedConst speed : SpeedConst.values()) {
            speedMap.put(speed.getCode(), speed.getSpeed());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("protocolTypeMap", protocolTypeMap);
        map.put("speedMap", speedMap);
        return map;
    }
}
