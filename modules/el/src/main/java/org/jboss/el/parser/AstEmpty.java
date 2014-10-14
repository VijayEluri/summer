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

/* Generated By:JJTree: Do not edit this line. AstEmpty.java */

package org.jboss.el.parser;

import java.util.Collection;
import java.util.Map;

import javax.el.ELException;

import org.jboss.el.lang.EvaluationContext;



/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
@SuppressWarnings("rawtypes")
public final class AstEmpty extends SimpleNode {
	public AstEmpty(int id) {
		super(id);
	}

	public Class getType(EvaluationContext ctx)
			throws ELException {
		return Boolean.class;
	}

	public Object getValue(EvaluationContext ctx)
			throws ELException {
		Object obj = this.children[0].getValue(ctx);
		if (obj == null) {
			return Boolean.TRUE;
		} else if (obj instanceof String) {
			return Boolean.valueOf(((String) obj).length() == 0);
		} else if (obj instanceof Object[]) {
			return Boolean.valueOf(((Object[]) obj).length == 0);
		} else if (obj instanceof Collection) {
			return Boolean.valueOf(((Collection) obj).isEmpty());
		} else if (obj instanceof Map) {
			return Boolean.valueOf(((Map) obj).isEmpty());
		}
		return Boolean.FALSE;
	}
}
