package com.oriensolutions.newtech.model;

public class User {

    private String fname;
    private String lname;
    private String email;
    private String bday;
    private String gender;
    private String profile_image_id;
    private String name;
    private String line1;
    private String line2;
    private String province;
    private String district;
    private String phone;

    public User() {
    }

    public User(String fname, String lname, String email, String bday, String gender, String profile_image_id, String name, String line1, String line2, String province, String district, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.bday = bday;
        this.gender = gender;
        this.profile_image_id = profile_image_id;
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.province = province;
        this.district = district;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image_id() {
        return profile_image_id;
    }

    public void setProfile_image_id(String profile_image_id) {
        this.profile_image_id = profile_image_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
