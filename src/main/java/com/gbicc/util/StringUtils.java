package com.gbicc.util;


import com.gbicc.conf.Tools;

import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String str;
	public static final String EMPTY_STRING = "";
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };


	private static String byteToHexString(byte b) {
		int n = b;
		if(n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}


	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	/**
	 * 转十六进制
	 *
	 * @param bytes
	 * @return
	 */
	public static String bytes2Hex(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) { 			
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
	
	/**
	 * 整数转十六进制
	 * @param integer
	 * @return
	 */
	public static String int2Hex(int integer) {
		StringBuffer buf = new StringBuffer(2);
        if (((int) integer & 0xff) < 0x10) { 
            buf.append("0"); 
        } 
        buf.append(Long.toString((int) integer & 0xff, 16));
    return buf.toString(); 
	}
	
	/**
	 * 通过手机号码判断运营商
	 * @param mobile
	 * @return
	 */
	public static String checkSP(String mobile){
		if(mobile==null){ return "未知";}
		
		List<String> uncom= Arrays.asList("130", "131", "132", "145","155", "156", "185","186");
		List<String> mobcom1= Arrays.asList("135", "136", "137", "138", "139", "147","150", "151", "152", "157", "158", "159", "182", "183","187","188");
		List<String> mobcom2= Arrays.asList("1340", "1341", "1342", "1343", "1344", "1345", "1346", "1347", "1348");
		List<String> telecom= Arrays.asList("133","1349","153","180","181","189");
		List<String> vircom= Arrays.asList("170");
		
		mobile=mobile.replace("+", "");
		if(mobile.startsWith("86")){ mobile=mobile.substring(2);}
		if(mobile.startsWith("0")){ mobile=mobile.substring(1);}
		if(mobile.length()==11){
			boolean isUncom=uncom.contains(mobile.substring(0, 3));
			boolean isMobcom1=mobcom1.contains(mobile.substring(0, 3));
			boolean isMobcom2=mobcom2.contains(mobile.substring(0,4));
			boolean isTelecom=telecom.contains(mobile.substring(0, 3));
			boolean isVircom=vircom.contains(mobile.substring(0, 3));
			
			if(isUncom){ return "联通";}
			if(isMobcom1||isMobcom2){ return "移动";}
			if(isTelecom){ return "电信";}
			if(isVircom){ return "虚拟";}
		}else{
			return "号码段长度不符合手机号码长度";
		}
		return null;
	}
	
	public static String toString(Collection<String> list, String divide){
		if(list==null||list.size()==0||divide==null){
			return null;
		}
		String str = "";
		for(Object obj : list){
			str += obj.toString() + divide;
		}
		str = str.substring(0,str.length()-1);
		return str;
	}
	
	/**
	 * 
	 * @Description: 随机字符串
	 * @author 441889070@qq.com
	 * @date 2015年11月12日 下午1:55:21
	 * @param
	 */
	 public static String randomString(int length)
	 {
		  String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		  Random random = new Random();
		  StringBuffer buf = new StringBuffer();
		  for(int i = 0 ;i < length ; i ++)
		  {
			   int num = random.nextInt(62);
			   buf.append(str.charAt(num));
		  }
		  return buf.toString();
	 }
	 
	 /**
	  * 
	  * @Description: 随机数字类型字符串
	  * @author 441889070@qq.com
	  * @date 2015年11月12日 下午1:55:31
	  * @param
	  */
	 public static String randomNumber(int length)
	 {
		  String str="0123456789";
		  Random random = new Random();
		  StringBuffer buf = new StringBuffer();
		  for(int i = 0 ;i < length ; i ++)
		  {
			   int num = random.nextInt(9);
			   buf.append(str.charAt(num));
		  }
		  return buf.toString();
	 }

	/**
	 * 取第一个可用的参数
	 * @param args
	 * @return
	 */
	public static String coalesce(String... args){
    	for (int i = 0; i < args.length; i++) {
 		   if(null != args[i] && !"".equals(args[i])){
 			   return args[i];
 		   }
 		}
    	return "";
     }

	/**
	 * 参数验证
	 * @param args
	 * @return
	 */
	public static boolean vaildeParam(String... args){
    	for (int i = 0; i < args.length; i++) {
 		   if(null == args[i] || "".equals(args[i])){
 			   return false;
 		   }
 		}
    	return true;
     }

	public static String filterEmoji(String source) {
		if(source != null)
		{
			Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern. CASE_INSENSITIVE ) ;
			Matcher emojiMatcher = emoji.matcher(source);
			if ( emojiMatcher.find())
			{
				source = emojiMatcher.replaceAll("*");
				return source ;
			}
			return source;
		}
		return source;
	}

	/**
	 * 保留数字、字母、中文、标点符号
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public   static String stringFilter(String str) {
		if(!Tools.vaildeParam(str)){
			return str;
		}
		try {
			String regEx="[^0-9a-zA-Z\\u4e00-\\u9fa5.，,。？“”]+";
			Pattern p   =   Pattern.compile(regEx);
			Matcher m   =   p.matcher(str);
			return   m.replaceAll("").trim();
		}catch (Exception e){
			e.printStackTrace();
		}
		return str;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}


	//不足位数前面补0
	public static String addZeroForNum(String str, int strLength) {
		int strLen =str.length();
		if (strLen <strLength) {
			while (strLen< strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);//左补0
				//sb.append(str).append("0");//右补0
				str= sb.toString();
				strLen= str.length();
			}
		}

		return str;
	}

	public static Integer[] arrayStringtoIntArr(String arr){
		String[]  arrs = arr.split(",");
		Integer[] result = new Integer[arrs.length];
		for(int i=0;i<arrs.length;i++){
			result[i] = Integer.valueOf(arrs[i]);
		}
		return result;
	}


	public static Long[] arrayStringtoLongArr(String arr){
		String[]  arrs = arr.split(",");
		Long[] result = new Long[arrs.length];
		for(int i=0;i<arrs.length;i++){
			result[i] = Long.valueOf(arrs[i]);
		}
		return result;
	}

	//将数组字符串转化成Integer数组List
	public static List<Integer> arrayStringtoIntegerList(String arr){
		List<Integer> result = new ArrayList<>();
		String[]  arrs = arr.split(",");
		for(int i=0;i<arrs.length;i++){
			result.add(Integer.parseInt(arrs[i]));
		}
		return result;
	}

	//将数组字符串转化成Long数组List
	public static List<Long> arrayStringtoLongList(String arr){
		List<Long> result = new ArrayList<>();
		String[]  arrs = arr.split(",");
		for(int i=0;i<arrs.length;i++){
			result.add(Long.parseLong(arrs[i]));
		}
		return result;
	}

	/**
	 * 数组转换成十六进制字符串
	 * @return HexString
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2){
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase()+" ");
		}
		return sb.toString();
	}


	public static boolean isInteger(String input){
		Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
		return mer.find();
	}

}
