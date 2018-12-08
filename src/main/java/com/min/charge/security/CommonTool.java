package com.min.charge.security;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 通用方法
 * 
 * @author
 *
 */
public class CommonTool {

	/**
	 * 得到长度为20的随机数字字符串
	 * 
	 * @return
	 */
	public static String getRandomDigitalString() {
		return getRandomDigitalString(20);
	}

	/**
	 * 得到指定长度的随机数字字符串
	 * 
	 * @param length
	 *            要生成的字符串的长度
	 * @return 生成的随机字符串
	 */
	public static String getRandomDigitalString(int length) {
		StringBuilder builder = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			builder.append((char) (ThreadLocalRandom.current().nextInt(48, 57)));
		}
		return builder.toString();
	}

	/**
	 * 获取字符串的单双字节
	 * 
	 * @param str
	 * @param flag
	 *            true:双字节，false:单字节
	 * @return
	 */
	public static byte[] getStringByOddOrEven(String str, boolean flag) {
		byte[] chars = hex2Bytes(str);
		byte[] sb = new byte[chars.length / 2];
		int j = 0;
		for (int i = (flag == true ? 1 : 0); i < chars.length; i += 2) {
			sb[j] = chars[i];
			j++;
		}
		return sb;
	}

	/**
	 * 将十六进制字符串转换为字节数组
	 * 
	 * @param strhex
	 * @return
	 */
	public static byte[] hex2Bytes(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
					16);
		}
		return b;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2Hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * 将字节数组转换为十六进制字符串，每个字节之间用空格分隔
	 * 
	 * @param b
	 * @return
	 */
	public static String bytes2String(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x ", b & 0xff));
		return sb.toString();
	}

	// /**
	// * BCD码转int
	// */
	// public static int bcd2int(byte[] inputdata) {
	//
	// int data = 0;
	// for (int i = inputdata.length-1; i >-1; i--) {
	// int tempdate = inputdata[i];
	// if(tempdate < 0){
	// tempdate = 0 - tempdate;
	// }
	//
	// if(tempdate>0x99){
	// tempdate = 0;
	// }
	// int x = tempdate /10;
	// int y = tempdate % 10;
	// tempdate = x*16 + y ;
	// data = tempdate + data*100;
	//
	// }
	// return data;
	// }
	/**
	 * BCD码转int
	 */
	public static long bcd2int(byte[] inputdata) {

		long data = 0;
		for (int i = inputdata.length - 1; i > -1; i--) {
			int tempdate = inputdata[i];
			int x = tempdate & 0x0f;
			int y = (tempdate >> 4) & 0x0f;
			tempdate = x + y * 10;
			data = tempdate + data * 100;
		}
		return data;
	}

	/**
	 * BCD码转String 用于交易流水号，终端机器编码等情况
	 */
	public static String bcd2String(byte[] inputdata) {

		String value = "";
		value = String.valueOf(bcd2int(inputdata)); // 直接把int转String
		return value;
	}

	/**
	 * bin码转int
	 */
	public static int bin2int(byte[] inputdata) {
		// int data = 0;
		// for (int i = inputdata.length-1; i > -1; i--) {
		// data= (inputdata[i] & 0xff) +data*256; // 把bin码转int
		// }
		// return data;
		int data = 0;
		for (int i = 0; i < inputdata.length; i++) {
			data = (inputdata[i] & 0xff) + data * 256; // 把bin码转int
		}
		return data;
	}

	/**
	 * bin码转double
	 * 
	 * @throws IOException
	 */
	public static double bin2double(byte[] inputdata) throws IOException {
		double data = 0.0;
		// 构建输入流
		ByteArrayInputStream bis = new ByteArrayInputStream(inputdata);
		DataInputStream dis = new DataInputStream(bis);
		try {
			for (int i = 0; i < bis.available(); i++) {
				data = dis.readDouble();
			}
		} catch (Exception ex) {
		}

		return data;
	}

	/**
	 * 把bin数据直接转换成String字符串
	 * 
	 * @param inputdata
	 * @return
	 */
	public static String bin2string(byte[] inputdata) {
		String data = "";
		for (int k = 0; k < inputdata.length; k++)
			data = data + String.valueOf(inputdata[k] / 16)
					+ String.valueOf(inputdata[k] % 16);
		return data;
	}

	/**
	 * ASII码转int;
	 */
	public static int Ascii2int(byte[] inputdata) {
		int data = 0;
		byte[] bytedata = new byte[2];
		for (int i = 0; i < inputdata.length; i = i + 2) {
			bytedata[0] = inputdata[i];
			bytedata[1] = inputdata[i + 1];
			data = bin2int(bytedata) - 48 + data * 10;
		}
		return data;
	}

	/**
	 * ASCII码转String
	 */
	public static String ASCII2String(byte[] inputdata) {
		String data = "";
		char getdata = 0;

		for (int i = 0; i < inputdata.length; i++) {
			getdata = (char) (inputdata[i] & 0xff);
			data = data + String.valueOf(getdata);
		}
		return data;
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * String转Data
	 * 
	 * @param strTime
	 * @return
	 * @throws ParseException
	 */
	public static Date StringToDate(String strTime) throws ParseException {
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	public static String DateToString(Date value) {
		return formatter.format(value);
	}

	
	public static String GetTradingSn(Date date) {

		long time = date.getTime();
		String timeString = String.valueOf(time);
		String sn = formatter.format(date)
				+ timeString.substring(timeString.length() - 3,
						timeString.length())+getRandomDigitalString(3);
		return sn;
	}

	/**
	 * Description DES加密
	 * 
	 * @param data
	 *            MD5编码
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		// 获取双字节数据
		StringBuilder db = new StringBuilder();
		for (int i = 1; i < data.length(); i = i + 2) {
			db.append(data.charAt(i));
		}
		String dbString = db.toString();

		// 生成一个可信任的随机数源
		SecureRandom random = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
		// 加密结果
		byte[] bt = cipher.doFinal(dbString.getBytes("UTF-8"));

		// 将字节转换为16进制字符串
		String result = CommonTool.byte2Hex(bt);
		return result;
	}
}
