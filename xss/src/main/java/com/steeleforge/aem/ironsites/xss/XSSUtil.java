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
package com.steeleforge.aem.ironsites.xss;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.xss.ProtectionContext;
import com.adobe.granite.xss.XSSAPI;
import com.adobe.granite.xss.XSSFilter;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;

/**
 * Conveniences to access ESAPI, XSSAPI/XSSFilter methods in JSPs
 *
 * @author David Steele
 */
public enum XSSUtil {
    INSTANCE;
    
    private static final Logger LOG = LoggerFactory.getLogger(XSSUtil.class);

    /**
     * XSSAPI wrapper for ESAPI generated valid integer based on String
     * 
     * @param source input
     * @param defaultValue value if valid integer cannot be produced
     * @param pageContext
     * @return safe Integer
     * @see XSSAPI#getValidInteger(String, int)
     */
    public static Integer getValidInteger(String source, Integer defaultValue, PageContext pageContext) {
        return getXSSAPI(pageContext).getValidInteger(source, defaultValue);
    }

    /**
     * XSSAPI wrapper for ESAPI validated anchor href
     * 
     * @param source
     * @param pageContext
     * @return safe URL
     * @see XSSAPI#getValidHref(String)
     */
    public static String getValidHref(String source, PageContext pageContext) {
        return getXSSAPI(pageContext).getValidHref(getSafeString(source));
    }

    /**
     * XSSAPI wrapper for ESAPI validated Javascript token
     * 
     * @param source
     * @param defaultValue
     * @param pageContext
     * @return safe URL
     * @see XSSAPI#getValidJSToken(String, String)
     */
    public static String getValidJSToken(String source, String defaultValue, PageContext pageContext) {
        return getXSSAPI(pageContext).getValidJSToken(getSafeString(source), getSafeString(defaultValue));
    }

    /**
     * XSSAPI wrapper for ESAPI encoded HTML entities
     * 
     * @param source
     * @param pageContext
     * @return encoded String for HTML
     * @see XSSAPI#encodeForHTML(String)
     */
    public static String encodeForHTML(String source, PageContext pageContext) {
        return getXSSAPI(pageContext).encodeForHTML(getSafeString(source));
    }
    
    /**
     * XSSAPI wrapper for ESAPI encoded HTML attribute entities
     * 
     * @param source
     * @param pageContext
     * @return encoded String for HTML attributes
     * @see XSSAPI#encodeForHTMLAttr(String)
     */
    public static String encodeForHTMLAttr(String source, PageContext pageContext) {
        return getXSSAPI(pageContext).encodeForHTMLAttr(getSafeString(source));
    }
    
    /**
     * XSSAPI wrapper for ESAPI encoded XML entities
     * 
     * @param source
     * @param pageContext
     * @return encoded String for XML
     * @see XSSAPI#encodeForXML(String)
     */
    public static String encodeForXML(String source, PageContext pageContext) {
        return getXSSAPI(pageContext).encodeForXML(getSafeString(source));
    }
    
    /**
     * XSSAPI wrapper for ESAPI encoded XML attribute entities
     * 
     * @param source
     * @param pageContext
     * @return encoded String for XML Attributes
     * @see XSSAPI#encodeForXMLAttr(String)
     */
    public static String encodeForXMLAttr(String source, PageContext pageContext) {
        return getXSSAPI(pageContext).encodeForXMLAttr(getSafeString(source));
    }

    /**
     * XSSAPI wrapper for ESAPI encoded Javascript strings
     * 
     * @param source
     * @param pageContext
     * @return encoded Javascript String
     * @see XSSAPI#encodeForJSString(String)
     */
    public static String encodeForJSString(String source, PageContext pageContext) {
        return getXSSAPI(pageContext).encodeForJSString(getSafeString(source));
    }
            
    /**
     * @param context
     * @return ProtectionContext
     */
    private static ProtectionContext getProtectionContext(String context) {
        ProtectionContext protectionContext = XSSFilter.DEFAULT_CONTEXT;
        
        if (StringUtils.isBlank(context)) {
            return XSSFilter.DEFAULT_CONTEXT;
        }
        
        protectionContext = ProtectionContext.fromName(context);
        
        if (null == protectionContext) {
            return XSSFilter.DEFAULT_CONTEXT;
        }
        return protectionContext;
    }    

    /**
     * @param request
     * @param policy
     * @return anti-samy policy path
     */
    private static String findPolicyPath(SlingHttpServletRequest request, String policy) {
        String path = null;
        if (StringUtils.isBlank(policy)) {
        return path;
        }
        ResourceResolver resolver = request.getResourceResolver();
        Resource resource = request.getResource();
        String resourceType = resource.getResourceType();
        if (StringUtils.startsWith(policy, "/")) {
        path = policy;
        } else {
        if ((StringUtils.startsWith(resourceType, "./")) || (StringUtils.startsWith(resourceType, "../"))) {
            resourceType = ResourceUtil.normalize(resource.getResourceType() + "/" + resourceType);
        }
        path = resourceType + "/" + policy;
        }
        path = ResourceUtil.normalize(path);
        if (!StringUtils.endsWith(path, ".xml")) {
        path = path + ".xml";
        }
        if (null == resolver.getResource(path)) {
        LOG.debug("Could not find XSS policy at {}", path);
        path = null;
        }
        return path;
    }

    /**
     * @param policy
     * @param context
     * @param source
     * @param pageContext
     * @return
     */
    public static String filterHTML(String policy, String context, String source, PageContext pageContext) {
        return filterHTML(policy, context, source, WCMUtil.getSlingRequest(pageContext));
    }
    
    /**
     * Given an anti-samy file path (component relative or absolute), an optional 
     * protection context, and JSP page context, the input source is run 
     * through the XSSFilter which applies the antisamy policy to source.
     * 
     * @param policy policy name/path
     * @param context protection context name
     * @param source source string
     * @param request sling request
     * @return scrubbed source, or empty string if markup is malformed
     * @see XSSFilter#filter(ProtectionContext, String, String)
     */
    public static String filterHTML(String policy, String context, String source, SlingHttpServletRequest request) {
        String output = "";
        ProtectionContext pc = getProtectionContext(context);
        String policyPath = findPolicyPath(request, policy);
        XSSFilter xss = getXSSFilter(request);
        try {
        output = xss.filter(pc, source, policyPath);
        } catch (RuntimeException re) {
        LOG.error("Parsing/Filtering error: {}", source, re.getMessage());
        }
        return output;
    }

    /**
     * Overloaded method for {@link XSSUtil#filterHTML(String, String, String, PageContext)}
     * 
     * @param policy policy name/path
     * @param source source string
     * @param pageContext JSP page context
     * @return scrubbed source, or empty string if markup is malformed
     * @see XSSUtil#filterHTML(String, String, String, PageContext)
     */
    public static String filterHTML(String policy, String source, PageContext pageContext) {
        return filterHTML(policy, null, source, WCMUtil.getSlingRequest(pageContext));
    }
    
    /**
     * Given an anti-samy file path (component relative or absolute), an optional 
     * protection context, and JSP page context, the input source is run 
     * through the XSSFilter which applies the antisamy policy to source.
     * 
     * @param policy policy name/path
     * @param context protection context name
     * @param source source string
     * @param request Sling request
     * @return scrubbed source, or empty string if markup is malformed
     * @see XSSFilter#filter(ProtectionContext, String, String)
     */
    public static String filterHTML(String policy, String source, SlingHttpServletRequest request) {
        return filterHTML(policy, null, source, request);
    }
    
    /**
     * Acquire XSSFilter from Sling Request
     * 
     * @param request
     * @return XSSFilter service
     */
    private static XSSFilter getXSSFilter(SlingHttpServletRequest request) {
        return (XSSFilter)request.adaptTo(XSSFilter.class);
    }
    
    /**
     * Acquire XSSAPI from JSP PageContext
     * 
     * @param pageContext
     * @return XSSAPI service
     */
    private static XSSAPI getXSSAPI(PageContext pageContext) {
        SlingHttpServletRequest slingRequest = WCMUtil.getSlingRequest(pageContext);
        
        return getXSSAPI(slingRequest);
    }
    
    /**
     * Acquire XSSAPI from Sling Request
     * @param request
     * @return
     */
    private static XSSAPI getXSSAPI(SlingHttpServletRequest request) {
        return (XSSAPI)request.adaptTo(XSSAPI.class);
    }
    
    /**
     * null safe method to return empty string if source is null/blank
     * 
     * @param source
     * @return original or empty String
     */
    private static String getSafeString(String source) {
        if (StringUtils.isEmpty(source)) {
            return StringUtils.EMPTY;
        }
        return source;
    }
}
