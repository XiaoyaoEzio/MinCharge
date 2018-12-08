package com.min.charge.web.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.min.charge.beans.Price;
import com.min.charge.beans.User;
import com.min.charge.buffer.WebLoginBuffer;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.PriceMapper;
import com.min.charge.web.service.PriceService;

@Service
public class PriceServiceImpl implements PriceService{

	@Autowired
	PriceMapper priceDao;
	
	@Override
	public JsonResult save(String webToken, int priceValue) {
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		Price price = new Price();
		price.setCommonPrice(priceValue);
		price.setCreatedDateTime(new Date());
		price.setHasInvaild(false);
		priceDao.save(price);
		
		return new JsonResult();
	}

	@Override
	public JsonResult current(String webToken) {
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		return JsonResult.data(priceDao.getByNow());
	}

	@Override
	public JsonResult update(String webToken, int priceValue) {
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		Price oldPrice = priceDao.getByNow();
		oldPrice.setHasInvaild(true);
		priceDao.update(oldPrice);
		
		Price price = new Price();
		price.setCommonPrice(priceValue);
		price.setCreatedDateTime(new Date());
		price.setHasInvaild(false);
		priceDao.save(price);
		return JsonResult.data(price);
	}

}
