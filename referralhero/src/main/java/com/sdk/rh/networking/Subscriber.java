package com.sdk.rh.networking;

public class Subscriber {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String cryptoWalletAddress;
    private String cryptoWalletProvider;
    private String extraField;
    private String extraField2;
    private String optionField;
    private Integer conversionAmount;
    private String code;
    private Integer position;
    private Boolean referred;
    private ReferredBy referredBy;
    private Integer peopleReferred;
    private Boolean promoted;
    private Object promotedAt;
    private Boolean verified;
    private Long verifiedAt;
    private Integer points;
    private Integer riskLevel;
    private String host;
    private String referral_link;
    private Object source;
    private Object device;
    private Long createdAt;
    private Long lastUpdatedAt;
    private String response;

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

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber : "";
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCryptoWalletAddress() {
        return cryptoWalletAddress != null ? cryptoWalletAddress : "";
    }

    public void setCryptoWalletAddress(String cryptoWalletAddress) {
        this.cryptoWalletAddress = cryptoWalletAddress;
    }

    public String getCryptoWalletProvider() {
        return cryptoWalletProvider != null ? cryptoWalletProvider : "";
    }

    public void setCryptoWalletProvider(String cryptoWalletProvider) {
        this.cryptoWalletProvider = cryptoWalletProvider;
    }

    public String getExtraField() {
        return extraField != null ? extraField : "";
    }

    public void setExtraField(String extraField) {
        this.extraField = extraField;
    }

    public String getExtraField2() {
        return extraField2 != null ? extraField2 : "";
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public String getOptionField() {
        return optionField != null ? optionField : "";
    }

    public void setOptionField(String optionField) {
        this.optionField = optionField;
    }

    public Integer getConversionAmount() {
        return conversionAmount != null ? conversionAmount : 0;
    }

    public void setConversionAmount(Integer conversionAmount) {
        this.conversionAmount = conversionAmount;
    }

    public String getCode() {
        return code != null ? code : "";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPosition() {
        return position != null ? position : 0;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getReferred() {
        return referred != null ? referred : false;
    }

    public void setReferred(Boolean referred) {
        this.referred = referred;
    }

    public ReferredBy getReferredBy() {
        return referredBy != null ? referredBy : null;
    }

    public void setReferredBy(ReferredBy referredBy) {
        this.referredBy = referredBy;
    }

    public Integer getPeopleReferred() {
        return peopleReferred != null ? peopleReferred : 0;
    }

    public void setPeopleReferred(Integer peopleReferred) {
        this.peopleReferred = peopleReferred;
    }

    public Boolean getPromoted() {
        return promoted != null ? promoted : false;
    }

    public void setPromoted(Boolean promoted) {
        this.promoted = promoted;
    }

    public Object getPromotedAt() {
        return promotedAt != null ? promotedAt : "";
    }

    public void setPromotedAt(Object promotedAt) {
        this.promotedAt = promotedAt;
    }

    public Boolean getVerified() {
        return verified != null ? verified : false;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Long getVerifiedAt() {
        return verifiedAt != null ? verifiedAt : 0;
    }

    public void setVerifiedAt(Long verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Integer getPoints() {
        return points != null ? points : 0;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRiskLevel() {
        return riskLevel != null ? riskLevel : 0;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getHost() {
        return host != null ? host : "";
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getReferrallink() {
        return referral_link != null ? referral_link : "";
    }

    public void setReferrallink(String referrallink) {
        this.referral_link = referrallink;
    }

    public Object getSource() {
        return source != null ? source : "";
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public Object getDevice() {
        return device != null ? device : "";
    }

    public void setDevice(Object device) {
        this.device = device;
    }

    public Long getCreatedAt() {
        return createdAt != null ? createdAt : 0;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastUpdatedAt() {
        return lastUpdatedAt != null ? lastUpdatedAt : 0;
    }

    public void setLastUpdatedAt(Long lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getResponse() {
        return response != null ? response : "";
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
