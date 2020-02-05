package com.telek.hemsipc.service;  

/**
 * 数据查询接口类
 * @Class Name：IDataQueryService    
 * @Class Description：    
 * @Creater：telek    
 * @Create Time：2019年5月7日下午4:27:03    
 * @Modifier：telek    
 * @Modification Time：2019年5月7日下午4:27:03    
 * @Remarks：
 */
public interface IDataQueryService {
    
	/**
	 * 查询所有设备当前功率
	 * @Author:kds 
	 * @Date：2019年5月7日下午5:35:33 
	 * @Describe:queryCurrentSumPower  
	 * @throws    
	 * @return
	 */
	public float queryCurrentSumPower();
	
	/**
	 * 查询某个设备当前功率
	 * @Author:kds 
	 * @Date：2019年5月7日下午5:35:37 
	 * @Describe:queryCurrentPower  
	 * @throws    
	 * @param deviceID
	 * @return
	 */
	public Float queryCurrentPowerByDevice(String deviceID);
	
	/**
	 * 查询某个设备当前频率
	 * @Author:kds 
	 * @Date：2019年5月8日上午9:02:43 
	 * @Describe:queryCurrentFrequenceByDevice  
	 * @throws    
	 * @param deviceID
	 * @return
	 */
	public Integer queryCurrentFrequenceByDevice(String deviceID);
	
	/**
	 * 查询某个设备当前开关状态
	 * @Author:kds 
	 * @Date：2019年5月8日上午9:08:59 
	 * @Describe:queryCurrentOnOffStatusByDevice  
	 * @throws    
	 * @param deviceID
	 * @return
	 */
	public Integer queryCurrentOnOffStatusByDevice(String deviceID);
}
  
