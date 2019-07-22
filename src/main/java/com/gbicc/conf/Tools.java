package com.gbicc.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bianmaren
 * @mail 441889070@qq.com
 * @date 2017/11/11
 */
public class Tools {

    private static final Logger logger = LoggerFactory.getLogger(Tools.class);

    /**
     * @description 参数验证
     * @author 编码人
     * @date 2015年12月6日 下午10:18:48
     * @param
     */
    public static boolean vaildeParam(Object... args){
        for (int i = 0; i < args.length; i++) {
            if(null == args[i]){
                return false;
            }else{
                if(args[i].getClass().equals(String.class)){
                    if("".equals(args[i])){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 返回值为列表当中的第一个非 NULL值，在没有非NULL 值得情况下返回值为 NULL 。
     * @param args
     * @return
     */
    private static Object coalesce(Object... args){

        for (int i = 0; i < args.length; i++) {
            if(vaildeParam(args[i])){
                return args[i];
            }
        }

        return null;
    }

    /**
     * @description 获取IP
     * @author 编码人
     * @date 2015年12月6日 下午10:18:48
     * @param
     */
    public static String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean isResponseBody(HttpServletRequest request) {
        if (request.getRequestURI().indexOf("ajax") > -1) {
            return true;
        }
        return false;
    }


    /**
     * 得到请求的根目录
     *
     * @param request
     * @return
     */
    public static String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + path;
        return basePath;
    }

    /**
     * 得到结构目录
     *
     * @param request
     * @return
     */
    public static String getContextPath(HttpServletRequest request) {
        String path = request.getContextPath();
        return path;
    }


    /**
     * 截取两位精度
     * @param f
     * @return
     */
    public static double DecimalFormatDouble(double f){
        BigDecimal b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 时间相加
     * @param time
     * @return
     */
    public static Date setExpirationTime(long time){
        try{
            long expirationTime =  time +System.currentTimeMillis();
            return new Date(expirationTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Date();

    }


    public static String getFileNameUrl(String url){
        try {
            String[] url_split = url.split("/");
            String name = url_split[url_split.length-1];
            String suffixes = name.split("\\.")[name.split("\\.").length - 1];
            return getFileNameNoEx(name)+"."+suffixes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @Title: getFileNameNoEx
     * @author dengwenbing
     * @Description: 获取不带后缀的文件名
     * @date: 2015年4月10日
     *
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 获取文件后缀
     * @param url
     * @return
     */
    public static String getFileExByUrl(String url){
        try {
            String[] url_split = url.split("/");
            String name = url_split[url_split.length-1];
            String suffixes = name.split("\\.")[name.split("\\.").length - 1];
            if(vaildeParam(suffixes)){
                suffixes = suffixes.toLowerCase();
            }
            return suffixes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前网络ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request){

        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割 //"***.***.***.***".length() = 15
        if(ipAddress!=null && ipAddress.length()>15){
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


    public static double cutDouble(double f) {
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        return f1;
    }

    public static double cutDoubleUp(int scale,double f) {
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(scale, BigDecimal.ROUND_UP).doubleValue();
        return f1;
    }

    public static double cutDoubleDown(int scale,double f) {
        BigDecimal b = new BigDecimal(f);
        double f1 = b.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
        return f1;
    }

    /**
     * 拆分集合
     * @param resList
     * @param count
     * @param <T>
     * @return
     */
    public static  <T> List<List<T>> splitList(List<T> resList, int count){
        if(resList==null ||count<1)
            return  null ;
        List<List<T>> ret=new ArrayList<List<T>>();
        int size=resList.size();
        if(size<=count){ //数据量不足count指定的大小
            ret.add(resList);
        }else{
            int pre=size/count;
            int last=size%count;
            //前面pre个集合，每个大小都是count个元素
            for(int i=0;i<pre;i++){
                List<T> itemList=new ArrayList<T>();
                for(int j=0;j<count;j++){
                    itemList.add(resList.get(i*count+j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if(last>0){
                List<T> itemList=new ArrayList<T>();
                for(int i=0;i<last;i++){
                    itemList.add(resList.get(pre*count+i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }


    /**
     * base64 转 MultipartFile
     * @param base64
     * @return
     */
    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[0]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 生成产品编号
     * @return
     */
    public static synchronized String getProductSN() {
        long orderNum = 0l;
        String date = null ;
        String str = new SimpleDateFormat("yyMMddHHmm").format(new Date());
        if(date==null||!date.equals(str)){
            date = str;
            orderNum  = 0l;
        }
        orderNum ++;
        long orderNo = Long.parseLong((date)) * 10000;
        orderNo += orderNum;;
        return "pr"+orderNo+(buildRandom(6)+"");
    }


    /**
     * 生成定长随机数
     * @param length
     * @return
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        } for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 替换参数
     * @param
     * @return
     */
    public static String paramToStr(String... args){
        String str = "";
        for(int i = 0; i < args.length; i++){
            if(null == args[i]||"".equals(args[i])){
                continue;
            }
            str =str+","+args[i];
        }
        return str+",";
    }


    /**
     * 生成订单编号
     * @return
     */
    public static synchronized String getOrderNo() {
        long orderNum = 0L;
        String date = null ;
        String str = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        if(date==null||!date.equals(str)){
            date = str;
            orderNum  = 0L;
        }
        orderNum ++;
        long orderNo = Long.parseLong((date)) * 10000;
        orderNo += orderNum;
        return "sn"+orderNo+(buildRandom(6)+"");
    }

    /**
     * 是否是测试环境
     * @return
     */
    public static boolean isTestEnv(HttpServletRequest request){
        String isTest = request.getParameter("isTest");

        if(-1 != request.getRequestURL().indexOf("localhost")
                || -1 != request.getRequestURL().indexOf("127.0.0.1")
                || -1 != request.getRequestURL().indexOf("192.")
                || ("true".equals(isTest))){
            return true;
        }
        return false;
    }


    /**
     * 移除url里面的某些参数
     * @param url
     * @param params
     * @return
     */
    public static String removeParams(String url, String[] params) {
        if(params != null && params.length > 0) {
            StringBuffer ps = new StringBuffer();
            ps.append("(");
            for(int i = 0; i < params.length; i++) {
                ps.append(params[i]).append("|");
            }
            ps.deleteCharAt(ps.length() - 1);
            ps.append(")");
            Pattern pattern = Pattern.compile("(&|\\?)" + ps.toString() + "=[^&]*&?");
            Matcher matcher = pattern.matcher(url);
            if(matcher.find()) {
                url = matcher.replaceAll("$1");
            }
            return url;
        }
        return url;
    }


    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }


    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim())) {
            return s.matches("^[0-9]*$");
        }else{
            return false;
        }
    }

    /**
     * 获取异常堆栈信息
     * @param t
     * @return
     */
    public static String printStackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }

    /**
     * 保留两位小数
     * @param f
     */
    public static String m2(double f) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(f);
    }

}
