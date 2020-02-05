package com.telek.hemsipc.model;

/**
 * 某个计量点的数据值及采集时间
 * 
 * @Class Name：PointData
 * @Class Description：
 * @Creater：kds
 * @Create Time：2018年11月21日上午9:34:08
 * @Modifier：kds
 * @Modification Time：2018年11月21日上午9:34:08
 * @Remarks：
 */
public class PointData {
    private long time; // 数据采集的时间
    private Object value = "0"; //值

    public PointData(){
        
    }
    
    public PointData(long time, Object value) {
        this.time = time;
        this.value = value;
    }
    
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Object getValue() {
        return value;
    }

    public float getFloatValue() {
    	try {
            float v = Float.parseFloat(value.toString());
            return v;
    	}catch(Exception e) {
    		return 0;
    	}
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
}
