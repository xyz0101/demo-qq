package com.jenkin.model;

public class Response<T> {
    private String resCode;
    private String resMsg;
    private T data;

    public Response(String resCode, String resMsg, T data) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.data = data;
    }

    public Response() {

    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public static Response successRes(Object data){
     return new Response<Object>("200","SUCCESS",data);
    }
    public static Response loginErrorRes(){
        return new Response<Object>("401","用户名或密码错误",null);
    }
    public static Response errorRes(String msg){
        return new Response<Object>("501",msg,null);
    }

}
