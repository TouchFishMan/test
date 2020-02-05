package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 不同项目个性化配置表
 * @Class Name：CustomizedConfig    
 * @Class Description：    
 * @Creater：kds    
 * @Create Time：2019年10月23日上午9:36:24    
 * @Modifier：kds    
 * @Modification Time：2019年10月23日上午9:36:24    
 * @Remarks：
 */
//@Entity
//@Table(name = "customized_config")
public class CustomizedConfig {
    @Id
    @Column(name = "config_name")
    private String configName;
    
    @Column(name = "config_value")
    private String configValue;
    
    @Column(name = "config_desc")
    private String configDesc;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }
}
