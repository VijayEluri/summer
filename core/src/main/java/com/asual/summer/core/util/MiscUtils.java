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

import java.io.FileInputStream;
import java.util.Locale;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Rostislav Hristov
 *
 */
@Component
public class MiscUtils {
	
    public static String getManifestAttribute(String value) {
        try {
        	ServletContext servletContext = RequestUtils.getRequest().getSession().getServletContext();
            Manifest mf = new Manifest();
			mf.read(new FileInputStream(servletContext.getResource("/META-INF/MANIFEST.MF").getFile()));
	        return mf.getMainAttributes().getValue(value);
		} catch (Exception e) {
			return "";
		}
    }
    
    public static Locale getLocale() {
    	return LocaleContextHolder.getLocale();
    }
}