/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asual.summer.json;

import java.util.Map;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.asual.summer.core.view.AbstractResponseView;

/**
 * 
 * @author Rostislav Hristov
 *
 */
@Named
public class JsonView extends AbstractResponseView {

	private static final String DEFAULT_CONTENT_TYPE = "application/json";
	private static final String DEFAULT_EXTENSION = "json";

	public JsonView() {
		super();
		setContentType(DEFAULT_CONTENT_TYPE);
		setExtension(DEFAULT_EXTENSION);
	}
	
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String encoding = request.getCharacterEncoding();
		String callback = (String) request.getParameter("callback");

		response.setContentType(getContentType());
		response.setCharacterEncoding(encoding);
		
		if (callback != null) {
			response.getOutputStream().write((callback + " && " + callback + "(").getBytes(encoding));
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.AUTO_CLOSE_TARGET, false);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		mapper.writeValue(response.getOutputStream(), filterModel(model));

		if (callback != null) {
			response.getOutputStream().write(");".getBytes(encoding));
		}

	}

}