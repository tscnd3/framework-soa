package com.xinyue.framework.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @ClassName: HttpUtil
 * @Description: HTTP请求工具类
 * @author buyuer
 * @date 2015年6月4日 下午2:48:24
 * 
 */
@SuppressWarnings("deprecation")
public class HttpUtil {

	private static Map<String, String> headers = new HashMap<String, String>();
	static {
		headers.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
		headers.put("Accept-Language", "zh-cn,zh;q=0.5");
		headers.put("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		headers.put("Accept",
				" image/gif, image/x-xbitmap, image/jpeg, "
						+ "image/pjpeg, application/x-silverlight, application/vnd.ms-excel, "
						+ "application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Accept-Encoding", "gzip, deflate");
	}

	/**
	 * 
	 * @author buyuer
	 * @Title: HttpPost
	 * @Description: post请求
	 * @param url
	 *            请求地址
	 * @param header
	 *            请求头
	 * @param body
	 *            请求消息体
	 * @return 响应数据
	 */
	public String HttpPost(String url, Map<String, String> header, String body) {
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			// set url
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			// set header
			for (String key : header.keySet()) {
				connection.setRequestProperty(key, header.get(key));
			}
			// set reaquest body
			connection.setDoOutput(true);
			connection.setDoInput(true);
			out = new PrintWriter(connection.getOutputStream());
			// save body
			out.print(body);
			// send body
			out.flush();
			// get response body
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		}

		return result;
	}

	/**
	 * 异常或者没拿到返回结果的情况下,result为""
	 * 
	 * @param url
	 * @param param
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String httpPost(String url, Map<String, Object> param) {
		DefaultHttpClient httpclient = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		String result = "";
		StringBuffer suf = new StringBuffer();
		try {
			httpclient = new DefaultHttpClient();
			// 设置cookie的兼容性---考虑是否需要
			httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
			httpPost = new HttpPost(url);
			// 设置各种头信息
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// 传入各种参数
			if (null != param) {
				for (Entry<String, Object> set : param.entrySet()) {
					String key = set.getKey();
					String value = set.getValue() == null ? "" : set.getValue().toString();
					nvps.add(new BasicNameValuePair(key, value));
					suf.append(" [" + key + "-" + value + "] ");
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			// 设置连接超时时间
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 1000);
			// 设置读数据超时时间
			HttpConnectionParams.setSoTimeout(httpPost.getParams(), 5000);
			response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return "";
			} else {
				entity = response.getEntity();
				if (null != entity) {
					byte[] bytes = EntityUtils.toByteArray(entity);
					result = new String(bytes, "UTF-8");
				} else {
				}
				return result;
			}
		} catch (Exception e) {
			return "";
		} finally {
			if (null != httpclient) {
				httpclient.getConnectionManager().shutdown();
			}
		}
	}

	/**
	 * 
	 * @author buyuer
	 * @Title: HttpGet
	 * @Description: get请求
	 * @param url
	 *            请求url
	 * @param header
	 *            请求头
	 * @return 请求结果
	 */
	public static String HttpGet(String url, Map<String, String> header) {
		String result = "";
		BufferedReader in = null;
		try {
			// set url
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			// set header
			if (null != header && !header.isEmpty()) {
				for (String key : header.keySet()) {
					connection.setRequestProperty(key, header.get(key));
				}
			}
			// get response body
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
