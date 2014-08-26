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
package com.steeleforge.aem.ironsites.wcm.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.WCMMode;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;

/**
 * Temporarily change WCMMode if possible/valid
 * 
 * @author David Steele
 */
public class SetModeTag extends BodyTagSupport {
    private static final long serialVersionUID = -3275369346344760726L;
    private static final Logger LOG = LoggerFactory.getLogger(SetModeTag.class);
    private String mode = null;
    private WCMMode wcmmode = null;

    @Override
    public int doStartTag() throws JspException {
        try {
            if (StringUtils.isNotBlank(getMode())) {
                this.wcmmode = WCMMode.fromRequest(WCMUtil.getSlingRequest(pageContext));
                for (WCMMode candidate : WCMMode.values()) {
                    if (StringUtils.equalsIgnoreCase(getMode(), candidate.toString())) {
                        candidate.toRequest(WCMUtil.getSlingRequest(pageContext));;
                        break;
                    }
                }
            }
        } catch(RuntimeException re) {
            LOG.debug(re.getMessage());
            throw new JspException(re);
        }
        return EVAL_BODY_BUFFERED;
    }
    
    @Override
    public int doAfterBody() throws JspException {
        BodyContent bc = getBodyContent();
        try {
            bc.getEnclosingWriter().print(bc.getString());
        } catch (IOException ioe) {
            LOG.debug(ioe.getMessage());
            throw new JspException(ioe);
        } finally {
            bc = null;
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            if (null != this.wcmmode) {
                this.wcmmode.toRequest(WCMUtil.getSlingRequest(pageContext));
            }
        } catch(RuntimeException re) {
            LOG.debug(re.getMessage());
            throw new JspException(re);
        }
        return EVAL_PAGE;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
