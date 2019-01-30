package org.qingfox.framework.microservice.enums;

public enum RespCode {
    SUCCESS("MICROSERVICE/SUCCESS"), FAILE("MICROSERVICE/FAILE"), UNFINDMETHOD("MICROSERVICE/UNFINDMETHOD");
    private final String name;

    private RespCode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
