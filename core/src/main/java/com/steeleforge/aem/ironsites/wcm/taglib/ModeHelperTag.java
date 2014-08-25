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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.WCMMode;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;

/**
 * Establishes PageContext objects to access determine WCMMode of a request.
 *
 * @author David Steele
 */
public class ModeHelperTag extends TagSupport {
    private static final long serialVersionUID = -6588302924881433906L;
    private static final Logger LOG = LoggerFactory.getLogger(ModeHelperTag.class);
    
    public static final String DEFAULT_MODEMAP_NAME = "ismode";
    public static final String DEFAULT_MODEOBJECT_NAME = "wcmmode";
    
    // DISABLED, EDIT, PREVIEW, ANALYTICS, READ_ONLY, DESIGN
    public static final String PROPERTY_DISABLED = WCMMode.DISABLED.name();
    public static final String PROPERTY_EDIT = WCMMode.EDIT.name();
    public static final String PROPERTY_PREVIEW = WCMMode.PREVIEW.name();
    public static final String PROPERTY_ANALYTICS = WCMMode.ANALYTICS.name();
    public static final String PROPERTY_READ_ONLY = WCMMode.READ_ONLY.name();
    public static final String PROPERTY_DESIGN = WCMMode.DESIGN.name();
    
    private SlingHttpServletRequest request;
    private WCMMode mode;
    private String mapName;
    private String wcmmodeName;
    
    public int doEndTag() throws JspException {
        try {
            init();
            setupModeMap();
            setupModeObject();
        } catch(Exception e) {
            LOG.error(e.getMessage());
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }
    
    public void release() {
        request = null;
    }

    protected void init() {
        request = WCMUtil.getSlingRequest(pageContext);
        mode = WCMMode.fromRequest(request);
        
        if (StringUtils.isBlank(getMapName())) {
            setMapName(DEFAULT_MODEMAP_NAME);
        }
        if (StringUtils.isBlank(getWcmmodeName())) {
            setWcmmodeName(DEFAULT_MODEOBJECT_NAME);
        }
    }
    
    protected void setupModeMap() {
        if (null == pageContext.getAttribute(getMapName(), PageContext.REQUEST_SCOPE)) {
            Map<String, Boolean> modeMap = new HashMap<String, Boolean>();
            
            // DISABLED, EDIT, PREVIEW, ANALYTICS, READ_ONLY, DESIGN
            modeMap.put(PROPERTY_DISABLED, WCMMode.DISABLED.equals(mode));
            modeMap.put(PROPERTY_DISABLED.toLowerCase(), WCMMode.DISABLED.equals(mode));

            modeMap.put(PROPERTY_EDIT, WCMMode.EDIT.equals(mode));
            modeMap.put(PROPERTY_EDIT.toLowerCase(), WCMMode.EDIT.equals(mode));

            modeMap.put(PROPERTY_PREVIEW, WCMMode.PREVIEW.equals(mode));
            modeMap.put(PROPERTY_PREVIEW.toLowerCase(), WCMMode.PREVIEW.equals(mode));
            
            modeMap.put(PROPERTY_ANALYTICS, WCMMode.ANALYTICS.equals(mode));
            modeMap.put(PROPERTY_ANALYTICS.toLowerCase(), WCMMode.ANALYTICS.equals(mode));

            modeMap.put(PROPERTY_READ_ONLY, WCMMode.READ_ONLY.equals(mode));
            modeMap.put(PROPERTY_READ_ONLY.toLowerCase(), WCMMode.READ_ONLY.equals(mode));

            modeMap.put(PROPERTY_DESIGN, WCMMode.DESIGN.equals(mode));
            modeMap.put(PROPERTY_DESIGN.toLowerCase(), WCMMode.DESIGN.equals(mode));
            
            pageContext.setAttribute(getMapName(), modeMap, PageContext.REQUEST_SCOPE);
        }
    }
    
    protected void setupModeObject() {
        if (null == pageContext.getAttribute(getWcmmodeName(), PageContext.REQUEST_SCOPE)) {
            pageContext.setAttribute(getWcmmodeName(), mode, PageContext.REQUEST_SCOPE);
        }
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getWcmmodeName() {
        return wcmmodeName;
    }

    public void setWcmmodeName(String wcmmodeName) {
        this.wcmmodeName = wcmmodeName;
    }
}
