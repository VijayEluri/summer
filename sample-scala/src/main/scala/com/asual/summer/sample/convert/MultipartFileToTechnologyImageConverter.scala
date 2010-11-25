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

package com.asual.summer.sample.convert;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

import com.asual.summer.sample.domain.Technology;

/**
 * 
 * @author Rostislav Hristov
 *
 */
@Named
class MultipartFileToTechnologyImageConverter extends Converter[MultipartFile, Technology.Image] {

    private val logger:Log = LogFactory.getLog(getClass());
    
    var mimeTypes:List[String] = new ArrayList[String]();
    {
        mimeTypes.add("image/gif");
        mimeTypes.add("image/jpeg");
        mimeTypes.add("image/jpeg");
        mimeTypes.add("image/png");
        mimeTypes.add("image/svg+xml");
    }
    
	def convert(source:MultipartFile):Technology.Image = {
		if (source.getSize() != 0 && mimeTypes.contains(source.getContentType())) {
			try {
				return new Technology.Image(source);
			} catch {
				case e: Exception => logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

}