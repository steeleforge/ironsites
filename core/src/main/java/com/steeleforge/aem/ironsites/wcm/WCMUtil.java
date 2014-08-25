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
package com.steeleforge.aem.ironsites.wcm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.jsp.util.TagUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.LanguageUtil;
import com.day.cq.wcm.api.LanguageManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.adobe.cq.wcm.launches.utils.LaunchUtils;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * AEM WCM/Page related utilities
 *
 * @author David Steele
 */
public enum WCMUtil {
    INSTANCE;

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(WCMUtil.class);
    
    /**
     * Utility method to extract sling request from JSP page context
     * 
     * @param pageContext
     * @return SlingHttpServletRequest instance
     */
    public static SlingHttpServletRequest getSlingRequest(PageContext pageContext) {
        return TagUtil.getRequest(pageContext);
    }
    
    /**
     * Utility method to extract sling script helper from SlingHttpServletRequest
     * 
     * @param request
     * @return SlingScriptHelper instance
     */
    public static SlingScriptHelper getSlingScriptHelper(SlingHttpServletRequest request) {
        SlingBindings bindings = (SlingBindings)request.getAttribute(SlingBindings.class.getName());
        return bindings.getSling();
    }
    
    /**
     * Utility method to extract sling script helper from JSP page context
     * 
     * @param pageContext
     * @return SlingScriptHelper instance
     */
    public static SlingScriptHelper getSlingScriptHelper(PageContext pageContext) {
        SlingHttpServletRequest request = getSlingRequest(pageContext);
        return getSlingScriptHelper(request);
    }
    
    /**
     * Append extension as necessary to relative path; String escape all paths
     * 
     * @param path
     * @return page path
     */
    public static String getSafePath(String path) {
        String safePath = StringEscapeUtils.escapeHtml(path);
        if (StringUtils.startsWith(path, "/")) {
            safePath = safePath + ".html";
        }
        return safePath;
    }
    
    /**
     * General purpose hashing for Strings such as node path.
     * 
     * @param token String to hash
     * @param minimumLength minimum length of hash
     * @return hashed value
     * @see <a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/hash/HashCode.html">HashCode</a>
     */
    public static String getFastHash(String token, int minimumLength) {
        HashFunction hf = Hashing.goodFastHash(minimumLength);
        HashCode hc = hf.newHasher()
           .putString(token, Charsets.UTF_8)
           .hash();
        return hc.toString();
    }
    
    /**
     * General purpose hashing for Strings such as node path. 
     * Defaults to minimum length of 5.
     * 
     * @param token String to hash
     * @see WCMUtil#getFastHash(String, int)
     */
    public static String getFastHash(String token) {
        return getFastHash(token, 5);
    }
    
    /**
     * URL encode a given String, per a given encoding
     * 
     * @param token
     * @param encoding, default to UTF-8
     * @return URL encoded String
     */
    public static String getURLEncoded(String token, String encoding) {
    	if (StringUtils.isNotEmpty(token)) {
	    	try {
	    		if (StringUtils.isNotEmpty(encoding)) {
	    			return URLEncoder.encode(token, encoding);
	    		}
	    		return URLEncoder.encode(token, CharEncoding.UTF_8);
	    	} catch(UnsupportedEncodingException uee) {
	    		LOG.debug(uee.getMessage());
	    	}
    	}
    	return token;
    }


    /**
     * URL encode a given String, assuming UTF-8
     * 
     * @param token
     * @return URL encoded String
     */
    public static String getURLEncoded(String token) {
    	return getURLEncoded(token, CharEncoding.UTF_8);
    }
    
    /**
     * Retrieve AEM Page from PageManager based on path
     * 
     * @param request
     * @param path
     * @return Page, null if request or path are empty, or if path doesn't resolve
     */
    public static Page getPage(SlingHttpServletRequest request, String path) {
        Page page = null;
        if (null == request) {
           return page;
        }
        ResourceResolver resolver = request.getResourceResolver();
        if (StringUtils.startsWith(path, "/") && null != resolver) {
            PageManager pageManager = resolver.adaptTo(PageManager.class);
            page = pageManager.getContainingPage(path);
            pageManager = null;
        }
        return page;
    }

    /**
     * Retrieve AEM Page from Sling Request
     * 
     * @param request
     * @return Page, null if request is null
     */
    public static Page getPage(SlingHttpServletRequest request) {
        Page page = null;
        if (null != request) {
           page = getPage(request, request.getResource().getPath());
        }
        return page;
    }
    
    /**
     * If page title is null, use navigation title. If that is null, use name.
     * 
     * @param page
     * @return page title, nav title, or null
     */
    public static String getPageTitle(Page page) {
        String title = null;
        if (null != page) {
            if (null != page.getTitle()) {
                title = page.getTitle();
            } else if (null != page.getNavigationTitle()) {
                title = page.getNavigationTitle();
            } else {
                title = page.getName();
            }
        }
        return title;
    }

    /**
     * If page navigation is null, use page title. If that is null, use name.
     * 
     * @param page
     * @return nav title, page title, or null
     */
    public static String getPageNavigationTitle(Page page) {
        String title = null;
        if (null != page) {
            if (null != page.getNavigationTitle()) {
                title = page.getNavigationTitle();
            } else if (null != page.getTitle()) {
                title = page.getTitle();
            } else {
                title = page.getName();
            }
        }
        return title;
    }

    /**
     * Based on a given resource path, extract locale/language level
     * agnostic to a AEM Launches based path
     * 
     * @param path
     * @return locale level resource path
     */
    public static String getLanguageRoot(String path) {
    	return LaunchUtils.getProductionResourcePath(LanguageUtil
    			.getLanguageRoot(path));
    }
    /**
     * Attempt to acquire a locale based on the requested resource.
     * 	
     * @param request
     * @return
     */
    public static Locale getLocale(SlingHttpServletRequest request) {
        SlingScriptHelper scriptHelper = WCMUtil.getSlingScriptHelper(request);
        LanguageManager languageManager = (LanguageManager)scriptHelper.getService(LanguageManager.class);
        if (null == languageManager) {
            return null;
        }
        return languageManager.getLanguage(request.getResource());
    }
}
