package com.min.charge.service.impl;

import com.min.charge.beans.Client;
import com.min.charge.buffer.LoginBuffer;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.operator.*;
import com.min.charge.service.CommandService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CommandServiceImpl implements CommandService{
	private static final Logger logger = Logger.getLogger(CommandServiceImpl.class);

	@Override
	public JsonResult connect(HttpServletRequest request, String token,
			String deviceSn) {
		
		return null;
	}

	class ConnectInfo{
		
		public String deviceId;
		
		public int state;
		
		
	}

	@Override
	public JsonResult start(HttpServletRequest request, String token,
							String deviceSn, String path, String chargeRank) {
		logger.debug("chargeRank: " + chargeRank);
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult = new OperatorStart().start(client, deviceSn, path);
		return jsonResult;
	}

	@Override
	public JsonResult pause(HttpServletRequest request, String token,
			String deviceSn, String path) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult = new OperatorPause().pause(client, deviceSn, path);
		return jsonResult;
	}

	@Override
	public JsonResult regain(HttpServletRequest request, String token,
			String deviceSn, String path) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult = new OperatorRegain().regain(client, deviceSn, path);
		return jsonResult;
	}

	@Override
	public JsonResult state(HttpServletRequest request, String token,
			String deviceSn, String path) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult = new OperatorState().state(client, deviceSn, path);
		return jsonResult;
	}

	@Override
	public JsonResult stop(HttpServletRequest request, String token,
			String deviceSn, String path) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult = new OperatorStop().stop(client, deviceSn, path);
		return jsonResult;
	}
}
