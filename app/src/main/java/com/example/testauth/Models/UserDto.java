package com.example.testauth.Models;

public class UserDto {
    int id ;
    String email ;
    String password ;
    String FullName;
    String imgStr ;

    public UserDto(int id, String email, String password, String FullName, String lastName, String imgStr) {
    }
    public UserDto(String name , String email, String password) {
            this.FullName = name;
            this.email = email;
            this.password = password;
    }
    public UserDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        this.FullName = fullName;
    }

    public String getImgStr() {
        return imgStr;
    }

    public void setImgStr(String imgStr) {
        this.imgStr = imgStr;
    }
}
