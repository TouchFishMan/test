package com.telek.hemsipc.util;


import lombok.Data;

/**
 * @author wangxb
 * @date 20-1-6 上午9:42
 */
@Data
public class CommandJsonResult extends JsonResult {

    private static final long serialVersionUID = -1L;

    /**
     * 发送的指令帧
     */
    private String sendCommand;

    /**
     * 接收的指令帧
     */
    private String receiveCommand;


    public CommandJsonResult(boolean _success, int _code, Object _data,String _sendCommand,String _receiveCommand){
        super(_success,_code,_data);
        this.sendCommand = _sendCommand;
        this.receiveCommand = _receiveCommand;
    }

    public CommandJsonResult(boolean _success,String _sendCommand,String _receiveCommand){
        super(_success,0,null);
        this.sendCommand = _sendCommand;
        this.receiveCommand = _receiveCommand;
    }

    public CommandJsonResult(boolean _success,int _code,String _sendCommand,String _receiveCommand){
        super(_success,_code,null);
        this.sendCommand = _sendCommand;
        this.receiveCommand = _receiveCommand;
    }
}
