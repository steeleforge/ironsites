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
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.jsp.util.TagUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.launches.utils.LaunchUtils;
import com.adobe.granite.xss.XSSAPI;
import com.day.cq.commons.Externalizer;
import com.day.cq.commons.LanguageUtil;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
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
     * @throws UnsupportedEncodingException 
     */
    public static String getURLEncoded(String token, String encoding) throws UnsupportedEncodingException {
        if (StringUtils.isNotEmpty(token)) {
            try {
                if (StringUtils.isNotEmpty(encoding)) {
                    return URLEncoder.encode(token, encoding);
                }
                return URLEncoder.encode(token, CharEncoding.UTF_8);
            } catch(UnsupportedEncodingException uee) {
                LOG.debug(uee.getMessage());
                throw uee;
            }
        }
        return token;
    }


    /**
     * URL encode a given String, assuming UTF-8
     * 
     * @param token
     * @return URL encoded String
     * @throws UnsupportedEncodingException 
     */
    public static String getURLEncoded(String token) throws UnsupportedEncodingException {
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
            if (null != page.getPageTitle()) {
                title = page.getPageTitle();
            } else if (null != page.getTitle()) {
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
            }
            title = getPageTitle(page);
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
        PageManager pageManager = (PageManager)request.getResourceResolver().adaptTo(PageManager.class);
        if (null == pageManager) {
            return Locale.getDefault();
        }
        Page page = pageManager.getContainingPage(request.getResource());
        return page.getLanguage(false);
    }
    
    /**
     * JCR Manlged path (i.e. jcr:content to _jcr_content)
     * 
     * @param path
     * @return
     */
    public static String getMangledPath(String path) {
        if (StringUtils.isBlank(path)) {
            return path;
        }
        return path.replaceAll("\\/(\\w+):(\\w+)", "/_$1_$2");
    }
    
    /**
     * Avoids repeat delimiters
     * 
     * @param token
     * @param delimiter
     * @return
     */
    public static String getDelimitered(String token, String delimiter) {
        if (StringUtils.isNotBlank(token) && !StringUtils.startsWith(token, delimiter)) {
            return delimiter + token;
        }
        return token;
    }
    
    
    /**
     * Given a relative path, this method can resolve mappings, append suffix
     * mangle JCR paths, XSS cleanse, append selectors, and apply extension(s).
     * 
     * @param request
     * @param path
     * @param selectors
     * @param extension
     * @param suffix
     * @param extensionSuffix
     * @param jcrMangle
     * @param resolverMap
     * @param sanitize
     * @return
     */
    public static String getRelativeURL(SlingHttpServletRequest request, 
            String path,
            String selectors, 
            String extension, 
            String suffix,
            boolean extensionSuffix,
            boolean jcrMangle, 
            boolean resolverMap,
            boolean sanitize) {
        String href = path;
        SlingScriptHelper sling = WCMUtil.getSlingScriptHelper(request);
        ResourceResolver resolver = request.getResourceResolver();
        
         // fail fast if path input doesn't appear to be relative
        // nor internal
        if (!StringUtils.startsWith(path, "/") || null == resolver.resolve(path)) {
            return href;
        }
        
        // check for resolver mapping if requested as parameter
        href = (resolverMap) ? resolver.map(path) : path;
        // mangle sling resource paths if necessary (e.g. jcr:content -> _jcr:content))
        if (jcrMangle) {
            href = getMangledPath(href);
        }
        StringBuilder sb = new StringBuilder(href);
                
        // selectors, if provided
        if (StringUtils.isNotBlank(selectors)) {
            sb.append(getDelimitered(selectors, WCMConstants.DELIMITER_SELECTOR));
        }
        
        // base resource path extension is presumably ".html" by default
        String ext = (StringUtils.isNotBlank(extension)) ? extension
                : WCMConstants.HTML;
        sb.append(getDelimitered(ext, 
                WCMConstants.DELIMITER_EXTENSION));
        
        // suffix (and extension) if provided
        if (StringUtils.isNotBlank(suffix)) {
            sb.append(getDelimitered(suffix, WCMConstants.DELIMITER_SUFFIX));
            if (extensionSuffix) {
                sb.append(ext);
            }
        }
        href = sb.toString();
        if (sanitize) {
            XSSAPI xss = (XSSAPI) sling.getService(XSSAPI.class);
            xss = xss.getRequestSpecificAPI(request);
            return xss.getValidHref(href);
        }
        return href;
    }
    
    /**
     * Use of AEM Externalizer service to product fully qualified URLs 
     * including protocol specification and environment specific domains
     * 
     * @param request
     * @param path relative path
     * @param protocol http/https
     * @param domain domain or Externalizer profile
     * @param sanitize with XSS API
     * @return fully qualified URL
     */
    public static String getExternalURL(SlingHttpServletRequest request,
            String relativePath,
            String protocol,
            String domain,
            boolean sanitize) {
        String href = relativePath;
        SlingScriptHelper sling = getSlingScriptHelper(request);
        ResourceResolver resolver = request.getResourceResolver();
        
        Externalizer externalizer = sling.getService(Externalizer.class);
        if (StringUtils.isNotBlank(protocol)) {
            href = externalizer.externalLink(resolver, domain, protocol, relativePath);
        } else {
            href = externalizer.externalLink(resolver, domain, relativePath);
        }
        // relative path may already be sanitized through xss
        if (sanitize) {
            XSSAPI xss = (XSSAPI) sling.getService(XSSAPI.class);
            xss = xss.getRequestSpecificAPI(request);
            return xss.getValidHref(href);
        }
        return href;
    }
    
    /**
     * Use of AEM Externalizer service to product fully qualified URLs 
     * including protocol specification and environment specific domains
     * 
     * @param request
     * @param path relative path
     * @param protocol http/https
     * @param domain domain:port or Externalizer profile, both with port
     * @return fully qualified URL
     */
    public static String getExternalURL(SlingHttpServletRequest request,
            String relativePath,
            String protocol,
            String domain) {
        return getExternalURL(request, relativePath, protocol, domain, true);
    }

    
    /**
     * Most sling resource hrefs will not need suffixes, additional reasonable
     * default parameters are assumed for getRelativeURL
     * 
     * @param request
     * @param path
     * @param selectors
     * @param extension
     * @return
     */
    public static String getResourceURL(SlingHttpServletRequest request, 
            String path,
            String selectors, 
            String extension) {
        return getRelativeURL(request, path, selectors, extension, null, true, true, true, true);
    }
    
    /**
     * Utility for common case for externalized resource paths
     * 
     * @param request
     * 
     * @param protocol
     * @param domain
     * @param path
     * @return
     */
    public static String getExternalResourceURL(SlingHttpServletRequest request, 
            String path,
            String protocol,
            String domain,
            String selectors,
            String extension) {
        return getExternalURL(request, 
                getResourceURL(request, path, selectors, extension), 
                protocol, 
                domain, 
                true);
    }
    
    /**
     * Most CQ Page hrefs will not need suffixes, additional reasonable
     * default parameters are assumed for getRelativeURL
     * 
     * @param request
     * @param path
     * @param selectors
     * @param extension
     * @return
     */
    public static String getPageURL(SlingHttpServletRequest request, 
            String path) {
        Page page = getPage(request, path);
        if (null == page) {
            return path;
        }
        return getRelativeURL(request, 
                page.getPath(), 
                null, WCMConstants.HTML, null, true, true, true, true);
    }
    
    /**
     * Utility for common case for externalized page paths
     * 
     * @param request
     * @param protocol
     * @param domain
     * @param path
     * @return
     */
    public static String getExternalPageURL(SlingHttpServletRequest request, 
            String path,
            String protocol,
            String domain) {
        return getExternalURL(request, 
                getPageURL(request, path), 
                protocol, 
                domain, 
                true);
    }

    /**
     * For non HTTPS fully qualified URLs, ensure HTTPS
     * 
     * @param fullyQualifiedURL
     * @return
     */
    public static String getSecureURL(String fullyQualifiedURL) {
        if (!StringUtils.startsWith(fullyQualifiedURL, WCMConstants.HTTPS)) {
            return WCMConstants.HTTPS + WCMConstants.DELIMITER_PORT + 
                    StringUtils.substringAfter(fullyQualifiedURL, WCMConstants.PROTOCOL_RELATIVE);
        }
        return fullyQualifiedURL;
    }
    
    /**
     * For protocol specific fully qualified URLs, ensure protocol relativity
     * 
     * @param fullyQualifiedURL
     * @return
     */
    public static String getProtocolRelativeURL(String fullyQualifiedURL) {
        if (!StringUtils.startsWith(fullyQualifiedURL, WCMConstants.PROTOCOL_RELATIVE)) {
            return WCMConstants.PROTOCOL_RELATIVE + StringUtils.substringAfter(fullyQualifiedURL, WCMConstants.PROTOCOL_RELATIVE);
        }
        return fullyQualifiedURL;
    }
    
    /** 
     * Generate mangled path based identifier replacing "/" for "__"
     * 
     * @param path
     * @return identifier
     */
    public static String getPathBasedIdentifier(String path) {
       String mangled = getMangledPath(path);
        if (StringUtils.isBlank(mangled)) {
            return mangled;
        }
        return StringUtils.replace(mangled, WCMConstants.DELIMITER_PATH, WCMConstants.DELIMITER_PATH_IDENTIFIER);       
    }
}
