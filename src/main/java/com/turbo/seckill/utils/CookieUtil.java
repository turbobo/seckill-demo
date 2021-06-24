package com.turbo.seckill.utils;

import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author Jusven
 * @Date 2021/6/17 22:00
 *
 * Cookie工具类
 */
public final class CookieUtil {
    /**
     * 获取cookie值，不编码
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);

    }

    /**
     * 获取cookie值
     *
     * @param request
     * @param cookieName
     * @param isDecode
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecode) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecode) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 获取cookie值---指定字符串编码
     * @param request
     * @param cookieName
     * @param encodeString
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * 设置cookie值  -- 不设置生效时间，默认浏览器关闭即失效，也不编码
     * @param request
     * @param response
     * @param cookieName
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue){
        setCookie(request,response,cookieName,cookieValue,-1);
    }
    /**
     * 设置cookie值  -- 指定时间内生效，但不编码
     * @param request
     * @param response
     * @param cookieName
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage) {
        setCookie(request,response,cookieName,cookieValue,cookieMaxage,false);
    }
    /**
     * 设置cookie值  -- 不设置时间生效，但编码
     * @param request
     * @param response
     * @param cookieName
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request,response,cookieName,cookieValue,-1,isEncode);
    }
    /**
     * 设置cookie值  -- 指定时间生效，编码
     * @param request
     * @param response
     * @param cookieName
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request,response,cookieName,cookieValue,cookieMaxage,isEncode);
    }
    /**
     * 设置cookie值  -- 指定时间生效，指定编码参数
     * @param request
     * @param response
     * @param cookieName
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, String encodeString) {
        setCookie(request,response,cookieName,cookieValue,cookieMaxage,encodeString);
    }

    /**
     * 删除cookie带cookie域名
     * @param request
     * @param response
     * @param cookieName
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        doSetCookie(request,response,cookieName,"",-1,false);
    }

    /**
     * 设置cookie值，并在指定时间内生效
     * @param request
     * @param response
     * @param cookieName
     * @param cookieMaxage cookie生效最大秒数
     * @param isEncode
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                    String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            if(cookieValue == null){
                cookieValue = "";
            }else if(isEncode){
                cookieValue = URLEncoder.encode(cookieValue,"UTF-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if(cookieMaxage > 0){
                cookie.setMaxAge(cookieMaxage);
            }
            if(null != request){ //设置域名的cookie
                String domainName = getDomainName(request);
                System.out.println(domainName);
                if(!"localhost".equals(domainName)){
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置cookie值，并在指定时间内生效
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxage  cookie生效最大秒数
     * @param encodeString  编码参数
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                    String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if(cookieValue == null){
                cookieValue = "";
            }else {
                cookieValue = URLEncoder.encode(cookieValue,encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if(cookieMaxage > 0){
                cookie.setMaxAge(cookieMaxage);
            }
            if(null != request){ //设置域名的cookie
                String domainName = getDomainName(request);
                System.out.println("域名："+domainName);
                if(!"localhost".equals(domainName)){
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取cookie域名
     * @param request
     * @return
     */
    public static final String getDomainName(HttpServletRequest request){
        String domainName = null;
        //通过request获取访问的url
        String serverName = request.getRequestURL().toString();

        if (serverName == null || serverName.equals("")) {
            domainName = "";

        } else {
            //将url转为小写
            serverName = serverName.toLowerCase();
            //如果url以 http:// 开头，截取
            if(serverName.startsWith("http://")) {
                serverName = serverName.substring(7);
            }
            int end = serverName.length();
            //判断地址是否包含 /
            if(serverName.contains("/")){
                //得到第一个 / 出现的位置
                end = serverName.indexOf("/");
            }
            //截取
            serverName = serverName.substring(0, end);
            //根据 . 分割
            final String[] domains = serverName.split("\\.");

            int len = domains.length;

            if (len > 3) {
                // www.xxx.com.cn
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];

            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }

        return domainName;
    }
}
