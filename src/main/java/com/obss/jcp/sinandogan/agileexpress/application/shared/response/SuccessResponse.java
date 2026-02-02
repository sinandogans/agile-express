package com.obss.jcp.sinandogan.agileexpress.application.shared.response;


public class SuccessResponse extends Response{
    public SuccessResponse(String message){
        super(true,message);
    }
}
