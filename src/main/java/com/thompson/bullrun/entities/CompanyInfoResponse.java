package com.thompson.bullrun.entities;

public class CompanyInfoResponse {
    private String profile;
    private String logo;

    // Constructor, getters, and setters
    // Constructor, getters, and setters
    public CompanyInfoResponse(String profile, String logo) {
        this.profile = profile;
        this.logo = logo;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
