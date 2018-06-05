package com.watom999.www.hoperun.entity;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class UserInfoEntity {
    private String user_ID;
    private String user_name;
    private String user_sex;
    private String job_number;
    private String login_account;
    private String login_password;
    private String page_id;//登录页面
    private String email;
    private String phone;
    private String preset_time01; //设置定时时间01
    private String preset_time02; //设置定时时间02

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public String getLogin_account() {
        return login_account;
    }

    public void setLogin_account(String login_account) {
        this.login_account = login_account;
    }

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPreset_time01() {
        return preset_time01;
    }

    public void setPreset_time01(String preset_time01) {
        this.preset_time01 = preset_time01;
    }

    public String getPreset_time02() {
        return preset_time02;
    }

    public void setPreset_time02(String preset_time02) {
        this.preset_time02 = preset_time02;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }
}
