package com.example.backend.entity;

public class Message {

    public int type;//消息类型 0表示操作成功 >0表示失败消息(1 账号或密码错误;2 对象在数据库中已存在;3 修改失败)

    public String description;

    public Message(int type,String content){
        this.type = type;
        this.description = content;
    }
    public Message(int type) {
        this.type = type;
        switch (type){
            case 0:
                this.description = "Success";
                break;
            case 1:
                this.description = "The ID card number or password is incorrect";
                break;
            case 2:
                this.description = "Add failure";
                break;
            case 3:
                this.description = "Delete failure";
                break;
            case 4:
                this.description = "Modify failure";
                break;
            case 5:
                this.description = "Query failure";
                break;
            case 6:
                this.description = "Parameters exception";
                break;
            case 7:
                this.description = "Email send failed";
        }
    }
}
