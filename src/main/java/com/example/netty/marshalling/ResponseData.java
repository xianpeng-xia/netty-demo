package com.example.netty.marshalling;

import java.io.Serializable;

/**
 * @author xianpeng.xia
 * on 2022/5/17 21:38
 */
public class ResponseData implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String responseMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", responseMessage='" + responseMessage + '\'' +
            '}';
    }
}
