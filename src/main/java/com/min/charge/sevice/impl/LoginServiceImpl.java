package com.min.charge.sevice.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.min.charge.beans.Client;
import com.min.charge.beans.User;
import com.min.charge.buffer.LoginBuffer;
import com.min.charge.buffer.SessionBuffer;
import com.min.charge.buffer.WebLoginBuffer;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.ClientMapper;
import com.min.charge.mapping.UserMapper;
import com.min.charge.security.MD5;
import com.min.charge.sevice.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
	
	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);
	
	@Autowired
	ClientMapper clientMapper;
	
	@Autowired
	UserMapper userMapper;
	
	@Override
	public JsonResult dologin(HttpServletRequest request, String username,
			String image, String password, String openId) {
		Client client = clientMapper.getByOpenId(openId);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}

//		if (!MD5.encrypt(password).equals(client.getPassword())) {
//			return JsonResult.code(ErrorCodeEnum.PASSWORD_ERROR);
//		}
		SessionBuffer.add(request.getSession());
		LoginBuffer.bindUser(request.getSession(), client.getId());
		Client client2 = LoginBuffer.getClient(request.getSession().getId());
		logger.debug(request.getSession().getId()+"  " +client2.getPhoneNum());
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.outTime = 60*24*7;
		loginInfo.token = request.getSession().getId();
		return JsonResult.data(loginInfo);
	}

	@Override
	public Client getClietnId(int id){
		return clientMapper.getById(id);
	}

	@Override
	public JsonResult register(HttpServletRequest request, String username,
			String openId, String password, int gender) {
		
		Client  client = null;
		client= clientMapper.getByOpenId(openId);
		if (client == null) {
			client = new Client();
			client.setBalance(0);
			if (gender == 0) 
				gender = 1;
			client.setGender(gender);
//		client.setPassword(MD5.encrypt(password));

			client.setUserName(username);
			client.setPhoneNum("");
			client.setSessionId(request.getSession().getId());
			client.setPublicOpenId(openId);
			int id = clientMapper.save(client);
			if (id<1) {
				return JsonResult.code(ErrorCodeEnum.REGISTER_ERERROR);
			}
			System.err.println("saveId" + client.getId());
		}
		
		SessionBuffer.add(request.getSession());
		LoginBuffer.bindUser(request.getSession(), client.getId());
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.outTime = 60*24*7;
		loginInfo.token = request.getSession().getId();
		return JsonResult.data(loginInfo);
	}

	@Override
	public JsonResult get(HttpServletRequest request, String token) {
		
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.balance = client.getBalance();
		userInfo.userName = client.getUserName();
		userInfo.phoneNum = client.getPhoneNum();
		userInfo.signature = client.getSignature();
		userInfo.gender = client.getGender();
		userInfo.openId = client.getPublicOpenId();
		System.err.println(client.getId());
		
		return JsonResult.data(userInfo);
	}
	
	@Override
	public JsonResult edit(HttpServletRequest request, String token, String phoneNum,
			String signature, int gender) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		if (phoneNum != null) 
			client.setPhoneNum(phoneNum);
		if(signature != null)
			client.setSignature(signature);
		if (gender != 0) 
			client.setGender(gender);
		System.err.println("s"+ client.getSignature());
		int code = clientMapper.update(client);
		System.err.println("code:"+code);
		if (code >1) {
			return JsonResult.code(ErrorCodeEnum.UPDATE_FAILED);
		}
		return new JsonResult();
	}
	
	@Override
	public JsonResult doWebLogin(HttpServletRequest request, String username,
			String image, String password) {
		User client = userMapper.getByName(username);
		if (!MD5.encrypt(password).equals(client.getPassword())) {
			return JsonResult.code(ErrorCodeEnum.PASSWORD_ERROR);
		}
		SessionBuffer.add(request.getSession());
		WebLoginBuffer.bindUser(request.getSession(), client.getId());
		User client2 = WebLoginBuffer.getClient(request.getSession().getId());
		logger.debug(request.getSession().getId()+"  " +client2.getPhoneNum());
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.outTime = 60*24*7;
		loginInfo.token = request.getSession().getId();
		return JsonResult.data(loginInfo);
	}
	
	class UserInfo {
		public String userName;
		
		public String phoneNum;
		
		public int gender;
		
		public String signature;
		
		public int balance;
		
		public String openId;
		
	}

	class LoginInfo  {
		
		public String token;
		
		public int outTime;
	}
}
