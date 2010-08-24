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

/* Generated By:JJTree: Do not edit this line. AstFunction.java */

package com.asual.summer.core.el.parser;

import java.lang.reflect.Method;

import javax.el.ELException;
import javax.el.FunctionMapper;

import com.asual.summer.core.el.lang.EvaluationContext;
import com.asual.summer.core.el.util.MessageFactory;
import com.asual.summer.core.el.util.ReflectionUtil;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
@SuppressWarnings("rawtypes")
public final class AstFunction extends SimpleNode {
    
    protected String localName = "";
    
    protected String prefix = "";
    
    public AstFunction(int id) {
        super(id);
    }
    
    public String getLocalName() {
        return localName;
    }
    
    public String getOutputName() {
        if (this.prefix == null) {
            return this.localName;
        } else {
            return this.prefix + ":" + this.localName;
        }
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public Class getType(EvaluationContext ctx)
    throws ELException {
        
        FunctionMapper fnMapper = ctx.getFunctionMapper();
        
        // quickly validate again for this request
        if (fnMapper == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.null"));
        }
        Method m = fnMapper.resolveFunction(this.prefix, this.localName);
        if (m == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.method",
                    this.getOutputName()));
        }
        return m.getReturnType();
    }
    
    public Object getValue(EvaluationContext ctx)
    throws ELException {
        
        FunctionMapper fnMapper = ctx.getFunctionMapper();
        
        // quickly validate again for this request
        if (fnMapper == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.null"));
        }
        Method m = fnMapper.resolveFunction(this.prefix, this.localName);
        if (m == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.method",
                    this.getOutputName()));
        }
        
        // If no params, there are no children, jjtGetNumChildren returns 0 if no children, not NPE
        Object[] params = new Object[this.jjtGetNumChildren()];
        for (int i = 0; i < this.jjtGetNumChildren(); i++) {
            params[i] = this.children[i].getValue(ctx);
        }
        
        return ReflectionUtil.invokeMethod(null, m, params);
    }
    
    public void setLocalName(String localName) {
        this.localName = localName;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    
    public String toString() {
        return ELParserTreeConstants.jjtNodeName[id] + "[" + this.getOutputName() + "]";
    }
}
