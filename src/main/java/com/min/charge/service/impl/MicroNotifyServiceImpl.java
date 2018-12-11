package com.min.charge.service.impl;

import com.min.charge.beans.BillRecords;
import com.min.charge.beans.Micropay;
import com.min.charge.config.Config;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.enums.TradeStatusEnum;
import com.min.charge.mapping.BillRecordsMapper;
import com.min.charge.mapping.ClientMapper;
import com.min.charge.mapping.MicroPayMapper;
import com.min.charge.security.MicropayCore;
import com.min.charge.security.XMLUtil;
import com.min.charge.service.MicroNotifyService;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MicroNotifyServiceImpl implements MicroNotifyService{

	private static final Logger logger = Logger
			.getLogger(MicroNotifyServiceImpl.class);

	private static final Map<String, String> map = new HashMap<String, String>();

	/**
	 * 添加同步锁缓存
	 * 
	 * @param tradeSn
	 * @return
	 */
	private synchronized String addLock(String tradeSn) {
		String result = map.get(tradeSn);
		if (result == null) {
			result = tradeSn;
			map.put(tradeSn, result);
		}
		return result;
	}

	/**
	 * 移除同步锁缓存
	 * 
	 * @param tradeSn
	 */
	private synchronized void removeLock(String tradeSn) {
		map.remove(tradeSn);
	}
	//微信支付异步通知处理
	@Override
	public void notifyPay(HttpServletRequest request, HttpServletResponse response){

		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		InputStream is = null;
		try {
			out = response.getWriter();
			is = request.getInputStream();
		} catch (IOException e1) {
			logger.error(e1.getMessage(), e1);

		}
		if (is == null) {
			out.println(MicropayCore.fail());
			out.flush();
			out.close();
			return;
		}
		SortedMap<String, String> params = new TreeMap<String, String>();
		params = XMLUtil.getXML(is);

		logger.debug("微信通知接收成功");
		// 获取商户订单号
		String out_trade_no = params.get("out_trade_no");
		logger.debug("微信通知账单号"+out_trade_no);
		String lock = this.addLock(out_trade_no);
		SqlSession session = MybaitsConfig.getCurrent();
		
		BillRecordsMapper billRecordsMapper = session.getMapper(BillRecordsMapper.class);
		MicroPayMapper microPayMapper = session.getMapper(MicroPayMapper.class);
		ClientMapper clientMapper = session.getMapper(ClientMapper.class);
		synchronized (lock) {
			try {

				// 异步通知的通信判断
				if ("SUCCESS".equals(params.get("return_code"))) {
					
					// 获取交易订单信息
					BillRecords tradingLog = billRecordsMapper.getBySn(out_trade_no);
					// 设置时间格式
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					// 填写微信支付记录表
					Micropay micropay = microPayMapper.getTradingSn(out_trade_no);

					if (micropay == null) {
						micropay = new Micropay();
						micropay.setCreatedDateTime(new Date());
					}
					// 异步通知参数进行本地签名用来验签
					String sign = XMLUtil.createSign(params, Config.Instance.micro_ApiKey);
					
					logger.debug("哈哈这是Openid:"+params.get("openid"));
					micropay.setAppId(params.get("appid"));
					if (params.get("attach") != null) {
						micropay.setAttach(params.get("attach"));
					}
					micropay.setBankType(params.get("bank_type"));
					micropay.setCashFee(Integer.parseInt(params
							.get("cash_fee")));
					if (params.get("cash_fee_type") != null) {
						micropay.setCashFeeType(params.get("cash_fee_type"));
					}
					if (params.get("coupon_count") != null) {

						micropay.setCouponCount(Integer.parseInt(params
								.get("coupon_count")));
					}
					if (params.get("coupon_fee") != null) {

						micropay.setCouponFee(Integer.parseInt(params
								.get("coupon_fee")));
					}
					micropay.setDeviceInfo(params.get("device_info"));
					if (params.get("err_code") != null) {

						micropay.setErrCode1(params.get("err_code"));

					}
					if (params.get("err_code_des") != null) {
						micropay.setErrCodeDes1(params.get("err_code_des"));

					}
					if (params.get("fee_type") != null) {
						micropay.setFeeType(params.get("fee_type"));
					}
					if (params.get("is_subscribe") != null) {
						micropay.setIsSubscribe(params.get("is_subscribe"));
					}

					micropay.setMchId(params.get("mch_id"));
					micropay.setNonceStr(params.get("nonce_str"));
					micropay.setOpenId(params.get("openid"));
					micropay.setResultCode1(params.get("result_code"));
					if (params.get("sign") != null) {

						micropay.setSign(params.get("sign"));
						logger.debug("\n" + "异步通知的签名：" + params.get("sign"));
					}
					micropay.setTimeEnd(formatter.parse(params
							.get("time_end")));
					// 交易金额
					int total_fee = Integer.parseInt(params
							.get("total_fee"));
					micropay.setTotalFee(total_fee);
					micropay.setTradeType(params.get("trade_type"));
					micropay.setTradingLogId(tradingLog.getId());
					micropay.setTradingSn(out_trade_no);
					// 微信支付订单号
					String transaction_id = params.get("transaction_id");
					micropay.setTransactionId(transaction_id);
					
					//异步通知的验签
					if (sign.equals(params.get("sign"))) {
						logger.debug("异步通知验签成功");
						micropay.setIsChecked1(true);
						if ("SUCCESS".equals(micropay.getTradeState())) {
							// 如果已经交易成功则直接返回成功的信息给腾讯
							out.println(MicropayCore.success());
							out.flush();
							out.close();
							return;
						}
						// 异步通知业务结果判断
						if ("SUCCESS".equals(params.get("result_code"))) {


							// 生成查询订单请求参数
							String postData = MicropayCore
									.requestCheck(transaction_id,out_trade_no,Config.Instance.micro_ApiKey);
							SortedMap<String, String> checkParams = new TreeMap<String, String>();
							// 获取查询订单的返回结果
							checkParams =XMLUtil.postAndRecive(postData
													,Config.Instance.micro_Qurey_URL);

				
							// 判断查询订单通信接收是否正常
							if ("SUCCESS"
									.equals(checkParams.get("return_code"))) {
								// 查询订单返回的参数进行本地签名用来验签
								String sign1 = XMLUtil.createSign(checkParams, Config.Instance.micro_ApiKey);
								logger.debug("\n" + "查询订单通知的签名："
										+ checkParams.get("sign"));
								//查询订单的验签
								if (sign1.equals(checkParams.get("sign"))) {
									logger.debug("查询订单通知验签成功");

									micropay.setSign1(checkParams.get("sign"));
									if (checkParams.get("err_code") != null) {
										micropay.setErrCode2(checkParams
												.get("err_code"));
									}
									if (checkParams.get("err_code_des") != null) {
										micropay.setErrCodeDes2(checkParams
												.get("err_code_des"));
									}
									micropay.setResultCode2(checkParams
											.get("result_code"));
									// 判断查询订单的业务结果是否成功
									if ("SUCCESS".equals(checkParams
											.get("result_code"))) {

										micropay.setTradeState(checkParams
												.get("trade_state"));
										if (checkParams.get("trade_state_des") != null) {
											micropay.setTradeStateDes(checkParams
													.get("trade_state_des"));
										}
										micropay.setIsChecked(true);
										// 判断查询订单返回的参数：“trade_state”（交易状态）是否成功
										if ("SUCCESS".equals(checkParams
												.get("trade_state"))) {
											//判断该订单是否已经处理过
											if (tradingLog.getTradeStatusEnum()
													.compareTo(
															TradeStatusEnum.Finished) != 0) {
												tradingLog
														.setTotalFee(total_fee
																);
												tradingLog
														.setTradeStatusEnum(TradeStatusEnum.Finished);
												billRecordsMapper.update(tradingLog);
												MybaitsConfig.commitCurrent();
												
												// 金额写进钱包
										
												clientMapper.updateBalance(tradingLog.getClientId(), total_fee);
												MybaitsConfig.commitCurrent();
												
												// 移除缓存
												this.removeLock(out_trade_no);

												
											}
										} else if ("NOTPAY".equals(checkParams
												.get("trade_state"))) {
											// 查询订单交易结果为未支付

										}
										
										// 保存微信支付记录表
										microPayMapper.save(micropay);
										MybaitsConfig.commitCurrent();
									} else {
										// 查询订单的result_code=fail，重新请求
										logger.error("\n"+"查询订单业务结果result_code="+checkParams.get("result_code")
										+"\n"+"返回的错误码err_code="+checkParams.get("err_code")
										+"\n"+"返回的错误码信息err_code_des="+checkParams.get("err_code_des"));
										out.println(MicropayCore.fail());
										out.flush();
										out.close();
										return;
									}
								} else {
									//查询订单通知验签失败
//									micropay.setIsChecked(false);
									logger.error("\n"+"查询订单通知验签失败"
									+"\n"+"查询订单本地签名：sign1="+sign1
									+"\n"+"查询订单返回签名sign="+checkParams.get("sign"));
									out.println(MicropayCore.fail());
									out.flush();
									out.close();
									return;
								}
							} else {
								// 查询订单return_code=fail
								logger.error("\n"+"查询订单return_code="+checkParams.get("return_code")
										+"\n"+"return_msg="+checkParams.get("return_msg")
										+"\n"+"发送的请求信息"+postData);
								out.println(MicropayCore.fail());
								out.flush();
								out.close();
								return;
							}
						} else {
							// 交易失败
							logger.error("\n"+"异步通知:result_code="+params.get("result_code")
									+"\n"+"返回的错误码err_code="+params.get("err_code")
									+"\n"+"返回的错误码描述err_code_des="+params.get("err_code_des"));
							out.println(MicropayCore.success());
							out.flush();
							out.close();
							return;

						}
					} else {
						//异步通知验签失败
						
						logger.error("\n"+"异步通知验签失败"
								+"\n"+"异步本地签名：sign="+sign
								+"\n"+"异步返回签名sign="+params.get("sign"));
						out.println(MicropayCore.fail());
						out.flush();
						out.close();
						return;
					}
					// 成功接收通知发送的信息
					out.println(MicropayCore.success());
					out.flush();
					out.close();

				} else {

					// 通知接收失败发送的信息
					logger.error("异步通知return_code=fail");
					out.println(MicropayCore.fail());
					out.flush();
					out.close();
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				
				MybaitsConfig.closeCurrent();
				
			}
		}
	}

}
