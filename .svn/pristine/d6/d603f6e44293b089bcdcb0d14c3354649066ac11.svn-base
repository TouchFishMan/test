package com.telek.hemsipc.util;

import com.telek.hemsipc.contant.Constant;
import com.telek.hemsipc.context.SysContext;

public class CommonUtil {

	/**
	 * 验证数据是否有效
	 * @Author:kds 
	 * @Date：2019年5月7日下午5:49:30 
	 * @Describe:validateData  
	 * @throws    
	 * @param time
	 * @return
	 */
	public static boolean validateData(long time) {
		if(System.currentTimeMillis() - time < StringUtil.parseLong(SysContext.getSysConfig(Constant.OFF_LINE_TIME).getConfigValue()) * 1000) {
        	return true;
        }
		return false;
	}
	
	 /**
     * 根据事件信号获取削减的百分数
     * 
     * @Modifier:kds
     * @Date：2018年5月23日上午8:31:03
     * @Describe:getCutPersent
     * @param value
     * @return
     */
    public static float getCutPersent(float value) {
        float persent = 0;
        switch ((int) value) {
   
        case 0:
            persent = StringUtil.parseInt(SysContext.getSysConfig(Constant.SIMPLE_SINGAL_0));
            break;
        case 1:
            persent = StringUtil.parseInt(SysContext.getSysConfig(Constant.SIMPLE_SINGAL_1));
            break;
        case 2:
            persent = StringUtil.parseInt(SysContext.getSysConfig(Constant.SIMPLE_SINGAL_2));
            break;
        case 3:
            persent = StringUtil.parseInt(SysContext.getSysConfig(Constant.SIMPLE_SINGAL_3));
            break;
        default:
            persent = StringUtil.parseInt(SysContext.getSysConfig(Constant.SIMPLE_SINGAL_3));
            break;
        }
        return persent;
    }
}
  
