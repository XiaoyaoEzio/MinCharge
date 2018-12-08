package com.min.charge.security;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * xml工具类
 * @author miklchen
 *
 */
public class XMLUtil {
	private static final Logger logger=Logger.getLogger(XMLUtil.class);
	public static final MediaType XML
    = MediaType.parse("text/xml;charset=utf-8");
	
	public static SortedMap<String , String> getXML(InputStream is) {
		 
		String responseString = null;  
		try{ 
		BufferedInputStream bis = new BufferedInputStream(is);    
         byte[] bytes = new byte[1024];    
         ByteArrayOutputStream bos = new ByteArrayOutputStream();    
         int count = 0;    
         while((count = bis.read(bytes))!= -1){    
             bos.write(bytes, 0, count);    
         }    
         byte[] strByte = bos.toByteArray();    
         responseString = new String(strByte,0,strByte.length,"utf-8");    
         bos.close();    
         bis.close();    
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		SortedMap<String, String> map= new TreeMap<String,String>();
		try {
			map = XMLUtil.doXMLParse(responseString);
		} catch (JDOMException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return map;
		
	}
	/**
	 * 向微信发送XML信息，获取返回信息
	 * @param postData
	 * @return
	 */
	public static SortedMap<String,String> postAndRecive(String postData,String requestURL){

		OkHttpClient client = new OkHttpClient();
		RequestBody postBody = RequestBody.create(XML, postData);

		String responseString = null;
		try {
			Request request = new Request.Builder().url(requestURL)
					.post(postBody).build();
			Response response = client.newCall(request).execute();
			ResponseBody body = response.body();

			InputStream bufferStream = body.byteStream();
			byte[] buffer = new byte[1024];
			int hasRead = 0;
			while ((hasRead = bufferStream.read(buffer)) > 0) {
				byte[] rea = new String(buffer, 0, hasRead).getBytes("utf-8");
				responseString = new String(rea);

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		}
		SortedMap<String, String> map = new TreeMap<String, String>();
		try {
			map = XMLUtil.doXMLParse(responseString);
		} catch (JDOMException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return map;

	}
	/**
	 * 生成xml字符串
	 * @param Params
	 * @return
	 */
	public static String enXMLString( SortedMap<String,String> Params){
		String postData = "<xml>";
		Set<Entry<String,String>> es = Params.entrySet();
		Iterator<Entry<String,String>> it = es.iterator();
		while (it.hasNext()) {
			Entry<String,String> entry = it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			if (k != "appkey") {
				if (postData.length() > 1)
					postData += "\n";
				postData += "<"+k+">" + v +"</"+k+">";
			}
		} 	
		postData += "\n";
		postData += "</xml>";
		
		return postData;
	}
	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static SortedMap<String,String> doXMLParse(String strxml) throws JDOMException, IOException {
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		
		SortedMap<String,String> m = new TreeMap<String,String>();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = XMLUtil.getChildrenText(children);
			}
			
			m.put(k, v);
		}
		
		//关闭流
		in.close();
		
		return m;
	}
	
	/**
	 * 获取子结点的xml
	 * @param children
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(XMLUtil.getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 获取xml编码字符集
	 * @param strxml
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
//	public static String getXMLEncoding(String strxml) throws JDOMException, IOException {
//		InputStream in = HttpClientUtil.String2Inputstream(strxml);
//		SAXBuilder builder = new SAXBuilder();
//		Document doc = builder.build(in);
//		in.close();
//		return (String)doc.getProperty("encoding");
//	}
//	
	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	
	/**
	 * 生成MD5签名
	 * @param packageParams
	 * @param apikey
	 * @return
	 */
	public static String createSign(SortedMap<String, String> packageParams, String apikey) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = packageParams.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String k =  entry.getKey();
			String v =  entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + apikey);
//		System.out.println("md5 sb:" + sb);
		String sign = MD5.encrypt(sb.toString(),"UTF-8").toUpperCase();

		return sign;

	}
	
}
