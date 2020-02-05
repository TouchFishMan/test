package com.telek.hemsipc.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 集中器，无设备id，不作为设备
 * @author wangxb
 * @date 20-1-3 下午4:45
 */
@Entity
@Table(name = "concentrator")
@Data
public class Concentrator {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    /**
     * 集中器地址
     */
    @Column(name = "address")
    private String address;

    /**
     * F33已绑定有设置的通信端口号，读F33时需要传
     */
    @Column(name = "bind_ports")
    private String bindPorts;

    /**
     * 已设置的任务号 1,2,3...
     */
    @Column(name = "task_ids")
    private String taskIds;

    /**
     * 品牌
     */
    @Column(name = "brand")
    private String brand;

    @Column(name = "enable")
    private Integer enable;

    /**
     * 是否删除，0未删除，-1已删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    @Column(name = "created_at")
    private Date createdAt;

}
