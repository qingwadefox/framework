package com.framework.plugin.web.system.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

public class ActInfoBean implements Serializable {

    private static final long serialVersionUID = 4327557876929487923L;

    private String nickname;
    private String gender;
    private String birthday;
    private String headimg;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
