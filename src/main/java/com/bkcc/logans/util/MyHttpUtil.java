package com.bkcc.logans.util;

import com.bkcc.util.weixin.MyX509TrustManager;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
@Data
public class MyHttpUtil {

	private static MyHttpUtil myHttpUtil;
	
	private static HttpClient httpClient = null;

	private static String ContentType = "Content-Type";

	private static String Charset = "text/html; charset=utf-8";

	private static String PostContentType = "application/x-www-form-urlencoded; charset=UTF-8";

	private static String UTF8 = "utf-8";

	/**
	 * 请求超时时间,默认20000ms
	 */
	private int timeout = 20000;
	/**
	 * 等待异步JS执行时间,默认20000ms
	 */
	private int waitForBackgroundJavaScript = 10000;

	/**
	 * 获取实例
	 *
	 * @return
	 */
	public static MyHttpUtil getInstance() {
		if (myHttpUtil == null) {
            myHttpUtil = new MyHttpUtil();
        }
		return myHttpUtil;
	}

	public static String sendGet(String url) {
		HttpGet httpget = getHttpget(url);
		StringBuffer sbf = new StringBuffer();
		httpget.setHeader(ContentType, Charset);
		InputStream instream = null;
		try {
			HttpResponse response = getClient().execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream, UTF8));
				String strRead;
				while ((strRead = reader.readLine()) != null) {
					sbf.append(strRead);
				}
				reader.close();
				return sbf.toString();
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			closeGet(httpget);
			closeInstream(instream);
		}
		return "";
	}

	public static String sendPost(Map<String, String> params, String url) {
		HttpPost httppost = getHttppost(url);
		httppost.setHeader(ContentType, PostContentType);
		StringBuffer sbf = new StringBuffer();
		InputStream instream = null;
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> elem = iterator.next();
				nvps.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nvps, UTF8));
			HttpResponse response = getClient().execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream, UTF8));
				String strRead;
				while ((strRead = reader.readLine()) != null) {
					sbf.append(strRead);
				}
				reader.close();
				return sbf.toString();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} finally {
			closePost(httppost);
			closeInstream(instream);
		}
		return "";
	}

	public static String sendPostJson(Map<String, Object> params, String url) {
		HttpPost httppost = getHttppost(url);
		StringBuffer sbf = new StringBuffer();
		InputStream instream = null;
		try {
			httppost.addHeader("Content-type", "application/json; charset=utf-8");
			httppost.setHeader("Accept", "application/json");
			httppost.setEntity(new StringEntity(new Gson().toJson(params), "utf-8"));
			HttpResponse response = getClient().execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				instream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream, UTF8));
				String strRead;
				while ((strRead = reader.readLine()) != null) {
					sbf.append(strRead);
				}
				reader.close();
				return sbf.toString();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} finally {
			closePost(httppost);
			closeInstream(instream);
		}
		return "";
	}

	private static HttpPost getHttppost(String url) {
		HttpPost httppost = new HttpPost();
		URI uri;
		try {
			uri = new URI(url);
			httppost.setURI(uri);
		} catch (URISyntaxException e) {
		}
		return httppost;
	}


	private static HttpGet getHttpget(String url) {
		HttpGet httpget = new HttpGet();
		URI uri;
		try {
			uri = new URI(url);
			httpget.setURI(uri);
		} catch (URISyntaxException e) {
		}
		return httpget;
	}


	private static void closePost(HttpPost httppost) {
		if (httppost != null) {
			httppost.abort();
		}
	}

	private static void closeGet(HttpGet httpget) {
		if (httpget != null) {
			httpget.abort();
		}
	}

	private static void closeInstream(InputStream instream) {
		if (null != instream) {
			try {
				instream.close();
			} catch (IOException e) {
			}
		}
	}


	/**
	 * 发送https请求
	 * 
	 * @param requestUrl    请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr     提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);

			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str;
			
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			conn.disconnect();
		} catch (ConnectException ce) {
			log.error("连接超时：{}", ce.getMessage(), ce);
		} catch (Exception e) {
			log.error("https请求异常：{}", e.getMessage(), e);
		}
		return buffer.toString();
	}


	/**
	 * 将网页返回为解析后的文档格式
	 * 
	 * @param html
	 * @return
	 */
	private static Document parseHtmlToDoc(String html) {
		return removeHtmlSpace(html);
	}

	private static Document removeHtmlSpace(String str) {
		Document doc = Jsoup.parse(str);
		String result = doc.html().replace("&nbsp;", "");
		return Jsoup.parse(result);
	}
	

	private MyHttpUtil() {}

	private static HttpClient getClient() {
		if (null == httpClient) {
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
			connManager.setMaxTotal(200);
			connManager.setDefaultMaxPerRoute(20);
			httpClient = HttpClients.custom().setConnectionManager(connManager).build();
		}
		return httpClient;
	}


}
