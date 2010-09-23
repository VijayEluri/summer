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

package com.asual.summer.core.faces;

import javax.el.ELContext;
import javax.el.ELException;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;

import com.asual.summer.core.ErrorResolver;
import com.asual.summer.core.util.RequestUtils;
import com.asual.summer.core.util.ResourceUtils;
import com.asual.summer.core.util.StringUtils;

/**
 * 
 * @author Rostislav Hristov
 *
 */
public class FacesELResolver extends SpringBeanFacesELResolver {

	private static String MESSAGES = "messages";
	private static String PROPERTIES = "properties";
	
	private static final ThreadLocal<String> keyHolder = new NamedThreadLocal<String>("key");

	public Object getValue(ELContext elContext, Object base, Object property) throws ELException {
		
		Object target  = RequestUtils.getAttribute(ErrorResolver.ERRORS_TARGET);
		if (target != null) {
			String targetName = StringUtils.toCamelCase(target.getClass().getSimpleName());
			if (targetName.equals(property)) {
				elContext.setPropertyResolved(true);
				return target;
			}
		}
		
		Object value = super.getValue(elContext, base, property);
		if (value == null) {
			try {
				if (property instanceof String) {
					String prop = (String) property;
					if (base == null && (MESSAGES.equals(prop) || PROPERTIES.equals(prop))) {
						elContext.setPropertyResolved(true);
						return new String(prop);
					}
					if (base instanceof String) {
						String bs = (String) base;
						boolean keyStored = false;
						if (MESSAGES.equals(bs)) {
							elContext.setPropertyResolved(true);
							return ResourceUtils.getMessage(prop);
						} else if (PROPERTIES.equals(bs)) {
							elContext.setPropertyResolved(true);
							return ResourceUtils.getProperty(prop);
						} else if (bs.startsWith("{") && bs.endsWith("}") || (keyStored = keyHolder.get() != null)) {
							elContext.setPropertyResolved(true);
							if (keyStored) {
								bs = "{" + keyHolder.get() + "}";
							}
							keyHolder.set(bs.substring(1, bs.length() - 1) + "." + prop);
							String message = ResourceUtils.getMessage(keyHolder.get());
							if (message.startsWith("{") && message.endsWith("}")) {
								return ResourceUtils.getProperty(keyHolder.get());
							} else {
								return message;
							}
						}
					}
				}
			} catch (Exception e) {}
		}
		keyHolder.set(null);
		return value;
	}

}