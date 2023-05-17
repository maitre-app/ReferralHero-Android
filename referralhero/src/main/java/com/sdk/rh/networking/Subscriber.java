package com.sdk.rh.networking;

public class Subscriber {
    String id;
    String name;
    String code;
    String referrallink;

    public String getId() {
        return id != null ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code != null ? code : "";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReferrallink() {
        return referrallink != null ? referrallink : "";
    }

    public void setReferrallink(String referrallink) {
        this.referrallink = referrallink;
    }
}
