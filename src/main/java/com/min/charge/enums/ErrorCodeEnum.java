package com.min.charge.enums;

public enum ErrorCodeEnum {

	OK(0,"Ok"),
	OFFLINE(-1,"离线"),
	ONLINE(1,"在线"),
	TOKEN_INVAILD(10000,"Toekn 失效"),
	PASSWORD_LENTH_ERROR(10001, "密码长度不对"),
	REGISTER_ERERROR(10002,"注册失败"),
	PASSWORD_ERROR(10003,"密码错误"),
	UPDATE_FAILED(10004,"更新失败"),
	PHONENUM_ILLEGAL(10005, "手机号不合法"),
	PHONENUM_LENTH_ERROR(10006,"手机号码为空"),
	SMS_SEND_FAILED(10007, "验证短信发送失败"),
	SMS_SIGNED_ERROR(10008,"签名校验失败"),
	DATA_NOT_FOUND(10009,"无记录"),
	TRADINGSN_HAD_DONE(10010,"订单已完成或失效"),
	SMS_VCODE_INVAILD(10011,"验证码无效,请重新获取"),
	SMS_VCODE_ERROR(10012,"验证码错误,请重新输入"),
	NOTENOUGH(10013,"余额不足"),
	COMMAND_UNKNOWN(20001,"未知的指令"),
	COMMAND_START_FAILD(20002,"启动失败"),
	COMMAND_STOP_FAILD(20003,"停止失败"),
	COMMAND_PAUSE_FAILD(20004,"暂停失败"),
	COMMAND_REGAIN_FAILD(20005,"复原失败"),
	CHARGING(20006,"已开启充电"),
	NO_CHARGING(20007,"未开启充电"),
	DEALING(20008,"正在处理中"),
	
	STATIONNAME_HAS_EXIST(30001,"站点名称已存在"),
	DEVICESN_HAS_EXIST(30002,"站点名称已存在"),
	;
	
	private int status;
	
	private String msg;
	
	private ErrorCodeEnum(int status, String msg){
		this.status = status;
		this.msg = msg;
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
	
	
}
