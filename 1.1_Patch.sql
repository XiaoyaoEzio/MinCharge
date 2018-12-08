Delimiter ;
Drop Function If Exists `fnTableExists`;
Delimiter ;;
Create Function `fnTableExists`(
    p_TableName Varchar(50)
) Returns Bit
Begin 
	If Exists(Select * From information_schema.`TABLES` Where TABLE_SCHEMA = Database() And Lower(Table_Name) = Lower(p_TableName)) Then 
		Return 1;
	Else 
		Return 0;
  End If;
End ;;


Delimiter ;
Drop Function If Exists `fnColumnExists`;
Delimiter ;;
Create Function `fnColumnExists` (
	p_TableName Varchar(50), 
	p_ColumnName Varchar(50)
) Returns Bit
Begin
	If Exists(Select * From information_schema.`COLUMNS` Where TABLE_SCHEMA = Database() And Lower(TABLE_NAME) = Lower(p_TableName) And Lower(COLUMN_NAME) = Lower(p_ColumnName)) Then 
		Return 1;
	Else
		Return 0;
	End If;
End ;;

Delimiter;
Drop Procedure If Exists `spUpdate`;
Delimiter;;
Create Procedure `spUpdate`()
Begin
	If fnTableExists('tClient') = 0 Then
		Create Table `tClient`(
			`Id` Int Unsigned Not Null Auto_Increment,
			`UserName` VarChar(20) Not Null Comment'用户名',
			`Password` Varchar(50) Not Null Comment'登陆密码',
			`PhoneNum` Varchar(50) Null Comment'手机号',
			`Balance` Int Not Null Default 0 Comment'余额',
			`SessionId` Varchar(50) Null Comment'登陆的token',
			`Gender` Int UnSigned Null Default 1 Comment'性别',
			`Signature` VarChar(150) Null Comment'个性签名',
			`PublicOpenId` VarChar(50) Null Comment '微信公众号OpenId', 
			Primary Key(`Id`)
		)Comment'用户表';
	End If;
	
	If fnTableExists('tPrice') = 0 Then
		Create Table `tPrice`(
			`Id` Int Unsigned Not Null Auto_Increment,
			`CommonPrice` Int Unsigned Not Null Comment'公共价格',
			`CreatedDateTime` DateTime Not Null Comment'创建时间',
			`HasInvaild` Bit Not Null Comment '是否过期',
			Primary Key(`Id`)
		)Comment'价格表';
	End If;
	
	If fnTableExists('tBillRecords') = 0 Then
		Create Table `tBillRecords`(
			`Id` bigint(20) Unsigned Not Null Auto_Increment,
			`CreatedDateTime` DateTime Not Null Comment'创建时间',
			`TradeType` Int Unsigned Not Null Comment'交易类型，1：代表消费，2：代表充值',
			`TradeStatusEnum`	Int UnSigned Not Null Comment'交易状态',
			`ClientId` Int Unsigned Null Comment'关联用户' ,
			`PriceId` Int UnSigned Null Comment'关联价格表',
			`TradingSn` VarChar(50) Not Null Comment'订单号',
			`DeviceId` Int UnSigned Null Comment'设备的Id',
			`TotalFee` Int Not Null Comment'金额',
			Primary Key(`Id`)
		)Comment '账单记录表';
	End If;
	
	If fnTableExists('tOrderRecord') = 0 Then
		Create Table `tOrderRecord`(
			`Id` bigint(20) Unsigned Not Null Auto_Increment,
			`CreatedDateTime` DateTime Not Null Comment'创建时间',
			`StartTime` DateTime Not Null Comment'创建时间',
			`StopTime`	DateTime  Null Comment'结束时间',
			`DeviceId` Int UnSigned Null Comment'设备的Id',
			`ClientId` Int Unsigned Null Comment'关联用户' ,
			`PriceId` Int UnSigned Null Comment'关联价格表',
			`TradingSn` VarChar(50) Not Null Comment'订单号',
			`OrderStatusEnum`	Int UnSigned Not Null Comment'订单状态状态',
			`LastPauseTime`	DateTime  Null Comment'最后一次暂停时间时间',
			`TotalPauseTime` Int default 0 Not Null Comment'累计停止时长，单位：毫秒',
			 `Path` VarChar(10) Not Null Comment'通道',
			Primary Key(`Id`)
		)Comment'订单记录';
	End If;
	
	If fnTableExists('tDevice') = 0 Then 
		Create Table `tDevice`(
		`Id` Int Unsigned Not Null Auto_Increment,
		`CreatedDateTime` DateTime Not Null Comment'创建时间',
		`DeviceSn` VarChar(50) Not Null Comment'设备的序列号',
		`DeviceName` VarChar(50) Not Null Comment'设备的名称',
		`LastOperatorTime` DateTime Not Null Comment'最后更新时间',
		`DeviceStatus` Int  Not Null Comment'设备状态',
		`StationId` Int Not Null Comment '站点Id',
		`HasDeleted` Bit  Not Null Default b'0' Comment'是否删除',
		Primary Key(`Id`)
		)Comment '设备列表';
	End If;
	If fnTableExists('tSysParam') = 0 Then
		Create Table `tSysParam`(
			`Id` Int Unsigned Not Null Auto_Increment,
			`ParamKey` VarChar(20) Not Null Comment'参数名称',
			`ParamValue` VarChar(200) Not Null Comment'参数值',
			`ParamDesc` VarChar(200) Null Comment'参数描述',
			Primary Key(`Id`)
		)Comment'系统参数表';
	End If;
	If fnTableExists('tMicroPay') = 0 Then
		CREATE TABLE `tMicroPay` (
		  `Id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
		  `TradingLogId` bigint(20) unsigned NOT NULL COMMENT '业务Id',
		  `TradingSn` VarChar(50)  NOT NULL COMMENT '业务流水号',
		  `CreatedDateTime` datetime NOT NULL COMMENT '创建时间',
		  `ISChecked1` bit(1) DEFAULT b'0' COMMENT '异步通知验签成功',
		  `ISChecked` bit(1) DEFAULT b'0' COMMENT '是否经过查单验证',
		  `AppId` varchar(32) NOT NULL COMMENT 'APPID',
		  `MchId` varchar(32) NOT NULL COMMENT '商户号',
		  `DeviceInfo` varchar(32) DEFAULT NULL COMMENT '设备号',
		  `NonceStr` varchar(32) NOT NULL COMMENT '随机字符串',
		  `Sign` varchar(32) NOT NULL COMMENT '异步通知签名',
		  `Sign1` varchar(32) NOT NULL COMMENT '查询订单通知签名',
		  `ResultCode1` varchar(16) NOT NULL COMMENT '异步通知业务结果',
		  `ErrCode1` varchar(32) DEFAULT NULL COMMENT '异步通知错误代码',
		  `ErrCodeDes1` varchar(128) DEFAULT NULL COMMENT '异步通知错误代码描述',
		  `ResultCode2` varchar(16) NOT NULL COMMENT '查询订单业务结果',
		  `ErrCode2` varchar(32) DEFAULT NULL COMMENT '查询订单错误代码',
		  `ErrCodeDes2` varchar(128) DEFAULT NULL COMMENT '查询订单错误代码描述',
		  `OpenId` varchar(128) NOT NULL COMMENT '用户标识',
		  `IsSubscribe` varchar(5) DEFAULT NULL COMMENT '是否关注公众账号，Y——关注，N——未关注',
		  `TradeType` varchar(16) NOT NULL COMMENT '交易类型',
		  `BankType` varchar(16) NOT NULL COMMENT '付款银行',
		  `TotalFee` int(11) DEFAULT NULL COMMENT '交易总金额，以分为单位',
		  `FeeType` varchar(8) DEFAULT NULL COMMENT '货币类型',
		  `CashFee` int(11) NOT NULL COMMENT '现金支付金额，以分为单位',
		  `CashFeeType` varchar(8) DEFAULT NULL COMMENT '现金支付货币类型',
		  `CouponFee` int(11) DEFAULT NULL COMMENT '代金卷或立减优惠金额',
		  `CouponCount` int(11) DEFAULT NULL COMMENT '代金卷或立减优惠使用数量',
		  `TransactionId` varchar(32) NOT NULL COMMENT '微信支付订单号',
		  `Attach` varchar(128) DEFAULT NULL COMMENT '商家数据包',
		  `TimeEnd` datetime NOT NULL COMMENT '支付完成时间',
		  `TradeState` varchar(32) NOT NULL COMMENT '交易状态，SUCCESS—支付成功，REFUND—转入退款，NOTPAY—未支付，CLOSED—已关闭，REVOKED—已撤销（刷卡支付），USERPAYING——用户支付中，PAYERROR——支付失败(其他原因，如银行返回失败)',
		  `TradeStateDes` varchar(50) DEFAULT NULL COMMENT '交易状态描述',
		  PRIMARY KEY (`Id`)
		)Comment'微信订单记录';
	End If;	
	
	If fnTableExists('tUser') = 0 Then
		Create Table `tUser`(
			`Id` Int Unsigned Not Null Auto_Increment,
			`UserName` VarChar(20) Not Null Comment'用户名',
			`Password` Varchar(50) Not Null Comment'登陆密码',
			`PhoneNum` Varchar(50) Null Comment'手机号',
			Primary Key(`Id`)
		)Comment'管理用户表';
	End If;
	
	If fnTableExists('tStation') = 0 Then 
		Create Table `tStation`(
		`Id` Int Unsigned Not Null Auto_Increment,
		`CreatedDateTime` DateTime Not Null Comment'创建时间',
		`StationName` VarChar(50) Not Null Comment'站点名称',
		`Latitude` Float Not Null Comment'纬度',
		`Longitude` Float Not Null Comment'经度',
		`ProvinceName` VarChar(10)  Not Null Comment'省名',
		`ProvinceCode` Varchar(10)  Null Comment '省编码',
		`CityName` VarChar(10)  Not Null  Comment '市名',
		`CityCode` VarChar(10) Null Comment '市编码',
		`StreetName` VarChar(50) Null Comment '街道信息',
		`HasDeleted` Bit Not Null Default b'0' Comment'是否删除',
		`UpdateDateTime` DateTime Not Null Comment '更新时间',
		Primary Key(`Id`)
		)Comment '设备列表';
	End If;
	
End;;

Call `spUpdate`();;
Drop Procedure If Exists `spUpdate`;;

Delimiter;
Drop Function If Exists `fnUpdateClientBalance`;
Delimiter;;
CREATE  FUNCTION `fnUpdateClientBalance`(
	p_ClientId Int Unsigned, -- 客户Id
	p_Quantity Int Signed -- 要更改的数量，如果是扣费，则值为负数，如果为充值，则值为正数
) RETURNS int(10) unsigned
Begin 
	Declare v_Balance Int Unsigned;
	Update `tClient` Set `Balance` = `Balance` + p_Quantity 
	Where 
		`Id` = p_ClientId;
	
	Select `Balance` Into v_Balance From `tClient` Where `Id` = p_ClientId;
	Return v_Balance;
End;;