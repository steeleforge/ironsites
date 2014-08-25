/** 
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <http://unlicense.org/>
 */
package com.steeleforge.aem.ironsites.xss.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.xss.ProtectionContext;
import com.steeleforge.aem.ironsites.xss.XSSUtil;

/**
 * Apply anti-samy policy to HTML in tag body.
 * 
 * @author David Steele
 */
public class XSSFilterHTMLTag extends BodyTagSupport {
    private static final long serialVersionUID = -3547258335652911076L;
    private static final Logger LOG = LoggerFactory.getLogger(XSSFilterHTMLTag.class);
    private String policy = null;
    private Boolean escape = null;
    
    @Override
    public int doAfterBody() throws JspException {
        BodyContent bodyContent;
        String body;
        JspWriter out;
        try {
            bodyContent = getBodyContent();
            out = bodyContent.getEnclosingWriter();
            body = XSSUtil.filterHTML(getPolicy(), 
                    getContext(), 
                    bodyContent.getString(), 
                    pageContext);
            if (StringUtils.isNotEmpty(body)) {
                out.print(body);
            }
        } catch (IOException ioe) {
            LOG.debug(ioe.getMessage());
            throw new JspException(ioe);
        } finally {
            bodyContent = null;
            body = null;
            out = null;
        }
        return SKIP_BODY;
    }
    
    private String getContext() {
        if (BooleanUtils.toBoolean(isEscape())) {
            return ProtectionContext.PLAIN_HTML_CONTENT.getName();
        }

        return ProtectionContext.HTML_HTML_CONTENT.getName();
    }

    public String getPolicy() {
        return policy;
    }
    
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Boolean isEscape() {
        return escape;
    }

    public void setEscape(Boolean escape) {
        this.escape = escape;
    }

}
