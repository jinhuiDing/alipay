package com.gbicc.util;


import com.gbicc.pojo.BackResult;
import com.github.wxpay.sdk.WXPayConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Component
public class WxUtils {

	private final static Logger logger = LoggerFactory.getLogger(WxUtils.class);

//	@Autowired
//	private OrderService orderService;
	
	/**
	 * 预下单
	 * 
	 * @param m
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Map wxUrl(String url, Map m) throws Exception {
		String xml = XmlOrMapUtils.mapToXml(m);
		String utf = "UTF-8";
		URL httpUrl = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl
				.openConnection();
		httpURLConnection.setRequestProperty("Host", "api.mch.weixin.qq.com");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(10 * 1000);
		httpURLConnection.setReadTimeout(10 * 1000);
		httpURLConnection.connect();
		OutputStream outputStream = httpURLConnection.getOutputStream();
		outputStream.write(xml.getBytes(utf));

		// 获取内容
		InputStream inputStream = httpURLConnection.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, "UTF-8"));
		final StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuffer.append(line);
		}
		String resp = stringBuffer.toString();
		if (stringBuffer != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, String> xmlToMap = XmlOrMapUtils.xmlToMap(resp);
		return xmlToMap;

	}

	/*
	 * //退款 public String requestWithCert(String urlSuffix, String uuid, String
	 * data, int connectTimeoutMs, int readTimeoutMs, boolean autoReport) throws
	 * Exception { return this.request(urlSuffix, uuid, data, connectTimeoutMs,
	 * readTimeoutMs, true, autoReport); }
	 */

	/*
	 * private String request(String urlSuffix, String uuid, String data, int
	 * connectTimeoutMs, int readTimeoutMs, boolean useCert, boolean autoReport)
	 * throws Exception { Exception exception = null; long elapsedTimeMillis =
	 * 0; long startTimestampMs = WXPayUtil.getCurrentTimestampMs(); boolean
	 * firstHasDnsErr = false; boolean firstHasConnectTimeout = false; boolean
	 * firstHasReadTimeout = false; IWXPayDomain.DomainInfo domainInfo =
	 * config.getWXPayDomain().getDomain(config); if(domainInfo == null){ throw
	 * new
	 * Exception("WXPayConfig.getWXPayDomain().getDomain() is empty or null"); }
	 * try { String result = requestOnce(domainInfo.domain, urlSuffix, uuid,
	 * data, connectTimeoutMs, readTimeoutMs, useCert); elapsedTimeMillis =
	 * WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
	 * config.getWXPayDomain().report(domainInfo.domain, elapsedTimeMillis,
	 * null); WXPayReport.getInstance(config).report( uuid, elapsedTimeMillis,
	 * domainInfo.domain, domainInfo.primaryDomain, connectTimeoutMs,
	 * readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout,
	 * firstHasReadTimeout); return result; } catch (UnknownHostException ex) {
	 * // dns 解析错误，或域名不存在 exception = ex; firstHasDnsErr = true;
	 * elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
	 * WXPayUtil.getLogger().warn("UnknownHostException for domainInfo {}",
	 * domainInfo); WXPayReport.getInstance(config).report( uuid,
	 * elapsedTimeMillis, domainInfo.domain, domainInfo.primaryDomain,
	 * connectTimeoutMs, readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout,
	 * firstHasReadTimeout ); } catch (ConnectTimeoutException ex) { exception =
	 * ex; firstHasConnectTimeout = true; elapsedTimeMillis =
	 * WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
	 * WXPayUtil.getLogger().warn("connect timeout happened for domainInfo {}",
	 * domainInfo); WXPayReport.getInstance(config).report( uuid,
	 * elapsedTimeMillis, domainInfo.domain, domainInfo.primaryDomain,
	 * connectTimeoutMs, readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout,
	 * firstHasReadTimeout ); } catch (SocketTimeoutException ex) { exception =
	 * ex; firstHasReadTimeout = true; elapsedTimeMillis =
	 * WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
	 * WXPayUtil.getLogger().warn("timeout happened for domainInfo {}",
	 * domainInfo); WXPayReport.getInstance(config).report( uuid,
	 * elapsedTimeMillis, domainInfo.domain, domainInfo.primaryDomain,
	 * connectTimeoutMs, readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout,
	 * firstHasReadTimeout); } catch (Exception ex) { exception = ex;
	 * elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
	 * WXPayReport.getInstance(config).report( uuid, elapsedTimeMillis,
	 * domainInfo.domain, domainInfo.primaryDomain, connectTimeoutMs,
	 * readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout,
	 * firstHasReadTimeout); } config.getWXPayDomain().report(domainInfo.domain,
	 * elapsedTimeMillis, exception); throw exception; }
	 */

	/**
	 * 单例，双重校验，请在 JDK 1.5及更高版本中使用
	 *
	 * @param config
	 * @return
	 */
	/*
	 * public static WXPayReport getInstance(WXPayConfig config) { if (INSTANCE
	 * == null) { synchronized (WXPayReport.class) { if (INSTANCE == null) {
	 * INSTANCE = new WXPayReport(config); } } } return INSTANCE; }
	 */

	/**
	 * 生成带有 sign 的 XML 格式字符串
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @return 含有sign字段的XML
	 */
	public static String generateSignedXml(final Map<String, String> data,
			String key) throws Exception {
		return generateSignedXml(data, key, WXPayConstants.SignType.MD5);
	}

	/**
	 * 生成带有 sign 的 XML 格式字符串
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @param signType
	 *            签名类型
	 * @return 含有sign字段的XML
	 */
	public static String generateSignedXml(final Map<String, String> data,
			String key, WXPayConstants.SignType signType) throws Exception {
		String sign = generateSignature(data, key, signType);
		data.put(WXPayConstants.FIELD_SIGN, sign);
		return XmlOrMapUtils.mapToXml(data);
	}

	/**
	 * 判断签名是否正确
	 *
	 * @param xmlStr
	 *            XML格式数据
	 * @param key
	 *            API密钥
	 * @return 签名是否正确
	 * @throws Exception
	 */
	public static boolean isSignatureValid(String xmlStr, String key)
			throws Exception {
		Map<String, String> data = XmlOrMapUtils.xmlToMap(xmlStr);
		if (!data.containsKey(WXPayConstants.FIELD_SIGN)) {
			return false;
		}
		String sign = data.get(WXPayConstants.FIELD_SIGN);
		return generateSignature(data, key).equals(sign);
	}

	/**
	 * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @return 签名是否正确
	 * @throws Exception
	 */
	public static boolean isSignatureValid(Map<String, String> data, String key)
			throws Exception {
		return isSignatureValid(data, key, WXPayConstants.SignType.MD5);
	}

	/**
	 * 判断签名是否正确，必须包含sign字段，否则返回false。
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @param signType
	 *            签名方式
	 * @return 签名是否正确
	 * @throws Exception
	 */
	public static boolean isSignatureValid(Map<String, String> data,
			String key, WXPayConstants.SignType signType) throws Exception {
		if (!data.containsKey(WXPayConstants.FIELD_SIGN)) {
			return false;
		}
		String sign = data.get(WXPayConstants.FIELD_SIGN);
		return generateSignature(data, key, signType).equals(sign);
	}

	/**
	 * 生成签名
	 *
	 * @param data
	 *            待签名数据
	 * @param key
	 *            API密钥
	 * @return 签名
	 */
	public static String generateSignature(final Map<String, String> data,
			String key) throws Exception {
		return generateSignature(data, key, WXPayConstants.SignType.MD5);
	}

	/**
	 * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
	 *
	 * @param data
	 *            待签名数据
	 * @param key
	 *            API密钥
	 * @param signType
	 *            签名方式
	 * @return 签名
	 */
	public static String generateSignature(final Map<String, String> data,
			String key, WXPayConstants.SignType signType) throws Exception {
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		for (String k : keyArray) {
			if (k.equals(WXPayConstants.FIELD_SIGN)) {
				continue;
			}
			if (data.get(k).trim().length() > 0) {// 参数值为空，则不参与签名
				sb.append(k).append("=").append(data.get(k).trim()).append("&");
			}
		}
		sb.append("key=").append(key);
		if (WXPayConstants.SignType.MD5.equals(signType)) {
			return md5(sb.toString()).toUpperCase();
		} else if (WXPayConstants.SignType.HMACSHA256.equals(signType)) {
			return hMACSHA256(sb.toString(), key);
		} else {
			throw new Exception(
					String.format("Invalid sign_type: %s", signType));
		}
	}

	/**
	 * 生成 MD5
	 *
	 * @param data
	 *            待处理数据
	 * @return MD5结果
	 */
	public static String md5(String data) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100)
					.substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 生成 HMACSHA256
	 * 
	 * @param data
	 *            待处理数据
	 * @param key
	 *            密钥
	 * @return 加密结果
	 * @throws Exception
	 */
	public static String hMACSHA256(String data, String key) throws Exception {
		Mac sha256HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretkey = new SecretKeySpec(key.getBytes("UTF-8"),
				"HmacSHA256");
		sha256HMAC.init(secretkey);
		byte[] array = sha256HMAC.doFinal(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100)
					.substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	public Map twoWayCertificate(Map<String, String> data, String url,char[] password)
			throws Exception {

		
		BackResult br = new BackResult();

		BasicHttpClientConnectionManager connManager;
		InputStream certStream = getCertStream();
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(certStream, password);

		// 实例化密钥库 & 初始化密钥工厂
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
				.getDefaultAlgorithm());
		kmf.init(ks, password);

		// 创建 SSLContext
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
				sslContext, new String[] { "TLSv1" }, null,
				new DefaultHostnameVerifier());

		connManager = new BasicHttpClientConnectionManager(RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http",
						PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslConnectionSocketFactory).build(), null,
				null, null);

		HttpClient httpClient = HttpClientBuilder.create()
				.setConnectionManager(connManager).build();

		// url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(2000).setConnectTimeout(10000).build();
		httpPost.setConfig(requestConfig);

		StringEntity postEntity = new StringEntity(
				XmlOrMapUtils.mapToXml(data), "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + data); //
																			// 很重要，用来检测
																			// 的使用情况，要不要加上商户信息？
		httpPost.setEntity(postEntity);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();

		String refundXml = EntityUtils.toString(httpEntity, "UTF-8");

		Map<String, String> xmlToMap = XmlOrMapUtils.xmlToMap(refundXml);

		
		
		return xmlToMap;
	}

	// 获得证书的内容
	public static InputStream getCertStream() throws Exception {
		
//		String certPath = "/home/cer/apiclient_cert.p12";
		String certPath = "/cer/apiclient_cert.p12";
		File file = new File(certPath);
		InputStream certStream = new FileInputStream(file);
		byte[] certData = new byte[(int) file.length()];
		certStream.read(certData);
		certStream.close();
		ByteArrayInputStream certBis;
		certBis = new ByteArrayInputStream(certData);
		return certBis;
	}
}
