package com.sdk.rh.networking;

public class ReferredBy {

    private String id;
    private String name;
    private String email;
    private String code;
    private Integer peopleReferred;
    private Integer points;

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

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code != null ? code : "";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPeopleReferred() {
        return peopleReferred != null ? peopleReferred : 0;
    }

    public void setPeopleReferred(Integer peopleReferred) {
        this.peopleReferred = peopleReferred;
    }

    public Integer getPoints() {
        return points != null ? points : 0;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
