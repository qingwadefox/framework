package com.framework.plugin.web.system.bean;

import java.io.Serializable;
import java.util.List;

public class AccountBean implements Serializable {

    private static final long serialVersionUID = 3015447865078452793L;
    private String id;
    private String name;
    private String status;
    private List<String> pmsids;
    private List<String> menuids;
    private ActInfoBean actInfo;
    private String wssId;
    private String tokenId;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPmsids() {
        return pmsids;
    }

    public void setPmsids(List<String> pmsids) {
        this.pmsids = pmsids;
    }

    public List<String> getMenuids() {
        return menuids;
    }

    public void setMenuids(List<String> menuids) {
        this.menuids = menuids;
    }

    public ActInfoBean getActInfo() {
        return actInfo;
    }

    public void setActInfo(ActInfoBean actInfo) {
        this.actInfo = actInfo;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getWssId() {
        return wssId;
    }

    public void setWssId(String wssId) {
        this.wssId = wssId;
    }

}
