package com.telek.hemsipc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统配置表
 * 
 * @Class Name：SysConfig
 * @Class Description：
 * @Creater：kds
 * @Create Time：2018年11月14日下午5:27:55
 * @Modifier：kds
 * @Modification Time：2018年11月14日下午5:27:55
 * @Remarks：
 */
@Entity
@Table(name = "sys_config")
public class SysConfig {
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
