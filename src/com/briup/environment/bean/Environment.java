package com.briup.environment.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Environment implements Serializable{
    /*
    环境存储实体类，包括环境种类（温度，湿度，二氧化碳，光照）
    发送端id,树莓派id,试验箱模块id,传感器地址，传感器个数
    指令标号，状态，环境值，采集时间
    * */
    //环境种类名称
    private String name;
    //发送端Id
    private String srcId;
    //树莓派系统Id
    private String desId;
    //试验箱上模块Id
    private String devId;
    //模块上传感器地址
    private String sersorAddress;
    //传感器个数
    private int count;
    //发送指令标号 3接收数据 16发送数据
    private String cmd;
    //状态 默认为1表示成功
    private int status;
    //环境值
    private float data;
    //采集时间
    private Timestamp gather_data;

    public Environment() {
    }

    public Environment(String name, String srcId, String desId, String devId, String sersorAddress, int count, String cmd, int status, float data, Timestamp gather_data) {
        this.name = name;
        this.srcId = srcId;
        this.desId = desId;
        this.devId = devId;
        this.sersorAddress = sersorAddress;
        this.count = count;
        this.cmd = cmd;
        this.status = status;
        this.data = data;
        this.gather_data = gather_data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getDesId() {
        return desId;
    }

    public void setDesId(String desId) {
        this.desId = desId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getSersorAddress() {
        return sersorAddress;
    }

    public void setSersorAddress(String sersorAddress) {
        this.sersorAddress = sersorAddress;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public Timestamp getGather_data() {
        return gather_data;
    }

    public void setGather_data(Timestamp gather_data) {
        this.gather_data = gather_data;
    }
}
