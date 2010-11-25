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

import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

import com.asual.summer.core.util.StringUtils;
import com.asual.summer.sample.domain.Status;

/**
 * 
 * @author Rostislav Hristov
 *
 */
@Named
class StringToStatusConverter extends Converter[String, Status] {
    
    private val logger:Log = LogFactory.getLog(getClass());

    def convert(source:String):Status = {
		if (!StringUtils.isEmpty(source)) {
			try {
				return Status.find(source);
			} catch {
				case e: Exception => logger.error(e.getMessage(), e);
			}
		}
		return null;
    }

}