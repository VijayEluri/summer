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

package com.asual.summer.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.asual.summer.core.util.RequestUtils;
import com.asual.summer.core.util.ResourceUtils;
import com.asual.summer.core.util.StringUtils;

/**
 * 
 * @author Rostislav Hristov
 *
 */
@Named
public class ErrorResolver implements HandlerExceptionResolver {
	
	public static final String ERRORS = "com.asual.summer.core.ErrorResolver.ERRORS";
	public static final String OBJECT_NAME = "com.asual.summer.core.ErrorResolver.OBJECT_NAME";
	public static final String TARGET = "com.asual.summer.core.ErrorResolver.TARGET";
	
    @SuppressWarnings("unchecked")
    public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception e) {
        
	    if (e instanceof BindException) {
	        
	    	Map<String, Map<String, Object>> errors = new HashMap<String, Map<String, Object>>();
	        
	        for (FieldError fe : (List<FieldError>) ((BindException) e).getFieldErrors()) {
		        Map<String, Object> error = new HashMap<String, Object>();
		        Object[] args = fe.getArguments();
		        String key = fe.isBindingFailure() ? 
		        		fe.getCodes()[2].replaceFirst("typeMismatch", "conversion") : "validation." + fe.getCodes()[2];
		        String message = ResourceUtils.getMessage(key, args);
		        if (message == null) {
		        	if (!fe.isBindingFailure()) {
		        		if (key.split("\\.").length > 3) {
		        			message = ResourceUtils.getMessage(key.substring(0, 
		        					key.indexOf(".", key.indexOf(".") + 1)) + key.substring(key.lastIndexOf(".")), args);
		        		}
		        		if (message == null && key.split("\\.").length > 2) {
		        			message = ResourceUtils.getMessage(key.substring(0, 
		        					key.indexOf(".", key.indexOf(".") + 1)), args);
		        		}
		        	} else if (fe.isBindingFailure() && message == null && key.split("\\.").length > 2) {
			        	message = ResourceUtils.getMessage(key.substring(0, 
			        			key.indexOf(".")) + key.substring(key.lastIndexOf(".")), args);
			        }
		        }
		        error.put("message",  message != null ? message : "Error!");
		        error.put("value", fe.getRejectedValue());
	        	errors.put(fe.getField(), error);
	        }
	        
	        String form = (String) RequestUtils.getParameter("_form");
	        if (form != null) {
	        	request.getSession().setAttribute(ERRORS, errors);
	        	request.getSession().setAttribute(OBJECT_NAME, ((BindException) e).getObjectName());
	        	request.getSession().setAttribute(TARGET, ((BindException) e).getTarget());
	        	return new ModelAndView(new RedirectView(form, false));
	        } else {
	            List<String> pairs = new ArrayList<String>();
	            for (String key : errors.keySet()) {
	                pairs.add(key + "=" + errors.get(key).get("message"));
	            }
		        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);	            
		        response.setHeader("Warning", StringUtils.join(pairs, ";"));	        	
	        }
	        
	    }
	    
		return null;
    }
    
}