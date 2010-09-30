/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asual.summer.core.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.asual.summer.core.RequestFilter;

/**
 * 
 * @author Rostislav Hristov
 *
 */
@Component
public class RequestUtils {

    public static HttpServletRequest getRequest() {
        return RequestFilter.getRequest();
    }

    public static String getRequestURI() {
        String requestUri = (String) getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE);
        if (requestUri != null) {
            return requestUri;
        }
        return getRequest().getRequestURI();
    }

    public static String getQueryString() {
        String requestURI = (String) getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE);
        if (requestURI != null) {
            return (String) getAttribute(WebUtils.FORWARD_QUERY_STRING_ATTRIBUTE);
        }
        return getRequest().getQueryString();
    }
    
    public static Map<String, Object[]> getParametersMap() {
        Map<String, Object[]> normalized = new HashMap<String, Object[]>();
        Map<String, String[]> params = getRequest().getParameterMap();
        for (String key : params.keySet()) {
            String[] value = (String[]) params.get(key);
            Object[] result = new Object[value.length];
            for (int i = 0; i < value.length; i++) {
                result[i] = ObjectUtils.convert(value[i]);
            }
            normalized.put(key, result);
        }
        return normalized;
    }

    public static Object getParameter(String name) {
        if (getParametersMap().get(name) != null) {
        	return getParametersMap().get(name)[0];
        }
        return null;
    }

    public static Object[] getParameterValues(String name) {
        return getParametersMap().get(name);
    }
    
    public static String getHeader(String name) {
        return getRequest().getHeader(name);
    }

    public static String getUserAgent() {
        return getHeader("User-Agent");
    }
    
    public static boolean isAjaxRequest() {
        return "XMLHttpRequest".equals(getRequest().getHeader("X-Requested-With"));
    }
        
    public static boolean isGetRequest() {
        return "GET".equalsIgnoreCase(getRequest().getMethod());
    }

    public static boolean isPostRequest() {
        return "POST".equalsIgnoreCase(getRequest().getMethod());
    }
    
    public static boolean isMethodBrowserSupported(String method) {
        return ("GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method));
    }
    
    public static boolean isMozilla() {
        return Pattern.compile("Mozilla").matcher(getUserAgent()).find() && !Pattern.compile("compatible|WebKit").matcher(getUserAgent()).find();
    }
    
    public static boolean isMSIE() {
        return Pattern.compile("MSIE").matcher(getUserAgent()).find() && !Pattern.compile("Opera").matcher(getUserAgent()).find();
    }
    
    public static boolean isOpera() {
        return Pattern.compile("Opera").matcher(getUserAgent()).find();
    }
    
    public static boolean isWebKit() {
        return Pattern.compile("WebKit").matcher(getUserAgent()).find();
    }
    
    public static boolean isMobile() {
        return Pattern.compile(" Mobile/").matcher(getUserAgent()).find();
    }
    
    public static void setAttribute(String name, Object value) {
        getRequest().setAttribute(name, value);
    }

    public static Object getAttribute(String name) {
        return getRequest().getAttribute(name);
    }
    
    public static <T> T mergeProperties(T source, T target) {
		List<String> ignoreList = new ArrayList<String>();
		PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(source.getClass());
		for (PropertyDescriptor sourcePd : sourcePds) {
			String name = sourcePd.getName();
			if (getRequest().getParameter(name) == null && getRequest().getParameter("_" + name) != null) {
				ignoreList.add(name);
			}
		}
        BeanUtils.copyProperties(source, target, ignoreList.toArray(new String[] {}));
        return target;
    }
    
    public static String contextRelative(String uri, boolean contextRelative) {
        if (uri != null) {
            String contextPath = getRequest().getContextPath();
            if (contextRelative) {
                return uri.startsWith("/") ? contextPath.concat(uri) : uri;
            } else {
                return !"".equals(contextPath) ? uri.replaceFirst("^" + contextPath, "") : uri;
            }
        }
        return null;
    }

    public static Object getError() {
        return getAttribute("javax.servlet.error.exception");
    }

    public static int getErrorCode() {
        return (Integer) getAttribute("javax.servlet.error.status_code");
    }

}