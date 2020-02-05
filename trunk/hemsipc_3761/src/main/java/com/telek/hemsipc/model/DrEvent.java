package com.telek.hemsipc.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

//@Entity
//@Table(name = "dr_event")
public class DrEvent {
    @Id
    @Column(name = "event_id")
    private String eventId;
    
    /** 事件状态 none :无,far :准备,near : 就绪,"active : 激活,completed :完成,"cancelled :取消 */
    @Column(name = "status")
    private String status;
    
    @Column(name = "priority")
    private int priority;
    /** 注释 */
    @Column(name = "comment")
    private String comment;
    
    /** 开始时间   格式是“yyyy-MM-dd HH:mm:ss”*/
    @Column(name = "dtstart")
    private String dtstart;
    
    /** 持续时间(单位:分钟) */
    @Column(name = "duration")
    private int duration;
    
    /** 降低同时率 */
    @Column(name = "tolerance")
    private int tolerance;
    
    /** 提前通知时间(单位:分钟) */
    @Column(name = "notification")
    private int notification;
    
    /** 从开始执行到满足要求所需时间(单位:分钟) */
    @Column(name = "rampup")
    private int rampup;
    
    /** 恢复时间(单位:分钟) */
    @Column(name = "recovery")
    private int recovery;
    
    @Column(name = "value")
    private float value;
    
    /*** 基线 */
    @Column(name = "base_line")
    private float baseLine;
    
    @Transient
    private List<String> resourceIds = new ArrayList<String>();

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDtstart() {
        return dtstart;
    }

    public void setDtstart(String dtstart) {
        this.dtstart = dtstart;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTolerance() {
        return tolerance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public int getRampup() {
        return rampup;
    }

    public void setRampup(int rampup) {
        this.rampup = rampup;
    }

    public int getRecovery() {
        return recovery;
    }

    public void setRecovery(int recovery) {
        this.recovery = recovery;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getBaseLine() {
        return baseLine;
    }

    public void setBaseLine(float baseLine) {
        this.baseLine = baseLine;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DrEvent) {
            return ((DrEvent) obj).getEventId().equals(eventId);
        }
        return false;
    }
}
