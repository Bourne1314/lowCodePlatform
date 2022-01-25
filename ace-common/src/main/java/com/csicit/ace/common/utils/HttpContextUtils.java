package com.csicit.ace.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取http请求内容
 *
 * @author shanwj
 * @date 2019-04-10 18:57:46
 * @version V1.0
 */
public class HttpContextUtils {
	/**
	 * 获取当前web请求
	 *
	 * @return javax.servlet.http.HttpServletRequest
	 * @author shanwj
	 * @date 2019/5/9 8:34
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
}
