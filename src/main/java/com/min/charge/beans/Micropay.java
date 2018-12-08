package com.min.charge.beans;
import java.util.Date;



public class Micropay {
	
		private Long id;
		/**
		 * 交易账单ID
		 */
		private Long tradingLogId;
		/**
		 * 订单号
		 */
		private String tradingSn;
		/**
		 * 创建时间
		 */
		private Date createdDateTime;
		
		/**
		 * 查询订单通知验签结果
		 */
		private  boolean isChecked;
		/**
		 * APPID
		 */
		private  String  appId;
		
		/**
		 * 商户号
		 */
		private String mchId;
		
		/**
		 * 设备号
		 */
		private String deviceInfo;
		
		/**
		 * 随机字符串
		 */
		private String nonceStr;

		/**
		 * 异步通知签名
		 */
		private String sign;
		
		/**
		 * 异步通知业务结果
		 */
		private String resultCode1;
		
		/**
		 * 异步通知错误代码
		 */
		private String errCode1;
		
		/**
		 * 异步通知错误代码描述
		 */
		private String errCodeDes1;
		
		/**
		 * 查询订单业务结果
		 */
		private String resultCode2;
		
		/**
		 * 查询订单错误代码
		 */
		private String errCode2;
		/**
		 * 查询订单错误代码描述
		 */
		private String errCodeDes2;
		/**
		 * 用户标识
		 */
		private String  openId;
		/**
		 * 是否关注公众号
		 */
		private String isSubscribe;
		/**
		 * 交易类型
		 */
		private String tradeType;
		/**
		 * 付款银行
		 */
		private String  bankType;
		/**
		 * 交易总金额
		 */
		private Integer  totalFee;
		/**
		 * 货币类型
		 */
		private String   feeType;
		/**
		 * 现金支付金额
		 */
		private Integer  cashFee;
		/**
		 * 现金支付货币类型
		 */
		private String   cashFeeType;
		/**
		 * 代金卷或立减优惠金额
		 */
		private Integer  couponFee;
		/**
		 * 代金卷或立减优惠使用数量
		 */
		private Integer  couponCount;
		/**
		 * 微信支付订单号
		 */
		private String   transactionId;

		/**
		 * 商家数据包
		 */
		private String   attach;
		
		/**
		 * 支付完成时间
		 */
		private Date timeEnd;
		
		/**
		 * 交易状态
		 */
		private String   tradeState;
		
		/**
		 * 交易状态描述
		 */
		private String   tradeStateDes;
		
		/**
		 * 查询订单通知签名
		 */
		private String sign1;
		
		/**
		 * 异步通知验签结果
		 */
		private  boolean isChecked1;
		
		
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		/**
		 * 获取记录创建时间
		 * @return
		 */
		public Date getCreatedDateTime() {
			return createdDateTime;
		}
		/**
		 * 设置记录创建时间
		 * @param createdDateTime
		 */
		public void setCreatedDateTime(Date createdDateTime) {
			this.createdDateTime = createdDateTime;
		}
		
		/**
		 * 获取充值记录单
		 * @return
		 */
		public Long getTradingLogId() {
			return tradingLogId;
		}
		/**
		 * 设置充值记录单
		 * @param tradingLogId
		 */
		public void setTradingLogId(Long tradingLogId) {
			this.tradingLogId = tradingLogId;
		}
		
		
		public String getTradingSn() {
			return tradingSn;
		}
		
		public void setTradingSn(String tradingSn) {
			this.tradingSn = tradingSn;
		}
		
		/**
		 * 获取总金额
		 * @return
		 */
		public Integer getTotalFee() {
			return totalFee;
		}
		/**
		 * 设置总金额
		 * @param totalFee
		 */
		public void setTotalFee(Integer totalFee) {
			this.totalFee = totalFee;
		}
		/**
		 * 获取是否经过查询订单检验
		 * @return
		 */
		public boolean getIsChecked() {
			return isChecked;
		}
		/**
		 * 设置是否经过查询订单检验
		 * @param isChecked
		 */
		public void setIsChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}
		/**
		 * 获取APPID
		 * @return
		 */
		public String getAppId() {
			return appId;
		}
		/**
		 * 设置APPID
		 * @param appId
		 */
		public void setAppId(String appId) {
			this.appId = appId;
		}
		/**
		 * 获取商户号
		 * @return
		 */
		public String getMchId() {
			return mchId;
		}
		/**
		 * 设置商户号
		 * @param mchId
		 */
		public void setMchId(String mchId) {
			this.mchId = mchId;
		}
		/**
		 * 获取设备号
		 * @return
		 */
		public String getDeviceInfo() {
			return deviceInfo;
		}
		/**
		 * 设置设备号
		 * @param deviceInfo
		 */
		public void setDeviceInfo(String deviceInfo) {
			this.deviceInfo = deviceInfo;
		}
		/**
		 * 获取随机字符串
		 * @return
		 */
		public String getNonceStr() {
			return nonceStr;
		}
		/**
		 * 设置随机字符串
		 * @param nonceStr
		 */
		public void setNonceStr(String nonceStr) {
			this.nonceStr = nonceStr;
		}
		/**
		 * 获取签名
		 * @return
		 */
		public String getSign() {
			return sign;
		}
		/**
		 * 设置签名
		 * @param sign
		 */
		public void setSign(String sign) {
			this.sign = sign;
		}
		/**
		 * 获取异步通知业务结果
		 * @return
		 */
		public String getResultCode1() {
			return resultCode1;
		}
		/**
		 * 设置异步通知业务结果
		 * @param resultCode1
		 */
		public void setResultCode1(String resultCode1) {
			this.resultCode1 = resultCode1;
		}
		/**
		 * 获取异步通知错误代码
		 * @return
		 */
		public String getErrCode1() {
			return errCode1;
		}
		/**
		 * 设置异步通知错误代码
		 * @param errCode1
		 */
		public void setErrCode1(String errCode1) {
			this.errCode1 = errCode1;
		}
		/**
		 * 获取异步通知错误代码描述
		 * @return
		 */
		public String getErrCodeDes1() {
			return errCodeDes1;
		}
		/**
		 * 设置异步通知错误代码描述
		 * @param errCodeDes1
		 */
		public void setErrCodeDes1(String errCodeDes1) {
			this.errCodeDes1 = errCodeDes1;
		}
		/**
		 * 获取查询订单业务结果
		 * @return
		 */
		public String getResultCode2() {
			return resultCode2;
		}
		/**
		 * 设置查询订单业务结果
		 * @param resultCode2
		 */
		public void setResultCode2(String resultCode2) {
			this.resultCode2 = resultCode2;
		}
		/**
		 * 获取查询订单的错误代码
		 * @return
		 */
		public String getErrCode2() {
			return errCode2;
		}
		/**
		 * 设置查询订单的错误代码
		 * @param errCode2
		 */
		public void setErrCode2(String errCode2) {
			this.errCode2 = errCode2;
		}
		/**
		 * 获取查询订单的错误代码描述
		 * @return
		 */
		public String getErrCodeDes2() {
			return errCodeDes2;
		}
		/**
		 * 设置查询订单的错误代码描述
		 * @param errCodeDes2
		 */
		public void setErrCodeDes2(String errCodeDes2) {
			this.errCodeDes2 = errCodeDes2;
		}
		/**
		 * 获取用户标识
		 * @return
		 */
		public String getOpenId() {
			return openId;
		}
		/**
		 * 设置用户标识
		 * @param openId
		 */
		public void setOpenId(String openId) {
			this.openId = openId;
		}
		/**
		 * 获取是否关注公众账号
		 * @return
		 */
		public String getIsSubscribe() {
			return isSubscribe;
		}
		
		/**
		 * 设置是否关注公众账号
		 * @param isSubscribe
		 */
		public void setIsSubscribe(String isSubscribe) {
			this.isSubscribe = isSubscribe;
		}
		/**
		 * 获取交易类型
		 * @return
		 */
		public String getTradeType() {
			return tradeType;
		}
		/**
		 * 设置交易类型
		 * @param tradeType
		 */
		public void setTradeType(String tradeType) {
			this.tradeType = tradeType;
		}
		/**
		 * 获取付款银行
		 * @return
		 */
		public String getBankType() {
			return bankType;
		}
		/**
		 * 设置付款银行
		 * @param bankType
		 */
		public void setBankType(String bankType) {
			this.bankType = bankType;
		}
		/**
		 * 获取货币类型
		 * @return
		 */
		public String getFeeType() {
			return feeType;
		}
		/**
		 * 设置货币类型
		 * @param feeType
		 */
		public void setFeeType(String feeType) {
			this.feeType = feeType;
		}
		/**
		 * 获取现金支付金额
		 * @return
		 */
		public Integer getCashFee() {
			return cashFee;
		}
		/**
		 * 设置现金支付金额
		 * @param cashFee
		 */
		public void setCashFee(Integer cashFee) {
			this.cashFee = cashFee;
		}
		/**
		 * 获取现金支付货币类型
		 * @return
		 */
		public String getCashFeeType() {
			return cashFeeType;
		}
		/**
		 * 设置现金支付货币类型
		 * @param cashFeeType
		 */
		public void setCashFeeType(String cashFeeType) {
			this.cashFeeType = cashFeeType;
		}
		/**
		 * 获取代金卷或立减优惠金额
		 * @return
		 */
		public Integer getCouponFee() {
			return couponFee;
		}
		/**
		 * 设置代金卷或立减优惠金额
		 * @param couponFee
		 */
		public void setCouponFee(Integer couponFee) {
			this.couponFee = couponFee;
		}
		/**
		 * 获取代金卷或立减优惠使用数量
		 * @return
		 */
		public Integer getCouponCount() {
			return couponCount;
		}
		/**
		 * 设置代金卷或立减优惠使用数量
		 * @param couponCount
		 */
		public void setCouponCount(Integer couponCount) {
			this.couponCount = couponCount;
		}
		
		/**
		 * 获取微信订单号
		 * @return
		 */
		public String getTransactionId() {
			return transactionId;
		}
		/**
		 * 设置微信订单号
		 * @param transactionId
		 */
		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}
		/**
		 * 获取商户数据
		 * @return
		 */
		public String getAttach() {
			return attach;
		}
		/**
		 * 设置商户数据
		 * @param attach
		 */
		public void setAttach(String attach) {
			this.attach = attach;
		}
		/**
		 * 获取支付完成时间
		 * @return
		 */
		public Date getTimeEnd() {
			return timeEnd;
		}
		/**
		 * 设置支付完成时间
		 * @param timeEnd
		 */
		public void setTimeEnd(Date timeEnd) {
			this.timeEnd = timeEnd;
		}
		/**
		 * 获取交易状态
		 * @return
		 */
		public String getTradeState() {
			return tradeState;
		}
		/**
		 * 设置交易状态
		 * @param tradeState
		 */
		public void setTradeState(String tradeState) {
			this.tradeState = tradeState;
		}
		/**
		 * 获取交易状态描述
		 * @return
		 */
		public String getTradeStateDes() {
			return tradeStateDes;
		}
		/**
		 * 设置交易状态描述
		 * @param tradeStateDes
		 */
		public void setTradeStateDes(String tradeStateDes) {
			this.tradeStateDes = tradeStateDes;
		}
		/**
		 * 获取查询订单签名
		 * @return
		 */
		public String getSign1() {
			return sign1;
		}
		/**
		 * 设置查询订单签名
		 * @param sign1
		 */
		public void setSign1(String sign1) {
			this.sign1 = sign1;
		}
		/**
		 * 获取异步通知验签结果
		 * @return
		 */
		public boolean getIsChecked1() {
			return isChecked1;
		}
		/**
		 * 设置异步通知验签结果
		 * @param isChecked1
		 */
		public void setIsChecked1(boolean isChecked1) {
			this.isChecked1 = isChecked1;
		}

		


		
		
		
	}


