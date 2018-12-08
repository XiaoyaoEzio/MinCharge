package com.min.charge.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.min.charge.enums.ErrorCodeEnum;

public class JsonResult {

	public int status ;
	
	public String msg ;
	
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	public Object data;
	
	public JsonResult(int status, String msg, Object data){
		this.status = status;
		this.msg = msg;
	}
	
	public JsonResult(){
		this.status = ErrorCodeEnum.OK.getStatus();
		this.msg =  ErrorCodeEnum.OK.getMsg();
	}
	
	public static JsonResult code(ErrorCodeEnum errorCode){
		JsonResult jResult = new JsonResult();
		jResult.status = errorCode.getStatus();
		jResult.msg = errorCode.getMsg();
		return jResult;
	}
	
	public static JsonResult data(Object data){
		JsonResult jResult = new JsonResult();
		jResult.setData(data);
		return jResult;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
