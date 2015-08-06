package com.steeleforge.aem.ironsites.wcm.page;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletResponse;

import com.adobe.cq.sightly.WCMUse;
import com.adobe.granite.xss.XSSAPI;
import com.day.cq.wcm.api.Page;
import com.steeleforge.aem.ironsites.wcm.WCMConstants;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;

public class RedirectUse extends WCMUse {
    // statics
    private static final String PN_REDIRECT_PROPERTY = "property";
    private static final String PN_DEFAULT_REDIRECT_PROPERTY = "redirectTarget";
    private static final String PN_REDIRECT_PATH = "redirectPath";
    private static final String PN_REDIRECT_TYPE = "redirectType";

    // locals
    String redirectPath = null;
    String redirectProperty = null;
    Integer redirectType = null;

    @Override
    public void activate() throws Exception {
        redirectProperty = get(PN_REDIRECT_PROPERTY, String.class);
        if (null == redirectProperty) {
            redirectProperty = PN_DEFAULT_REDIRECT_PROPERTY;
        }
        redirectType = getPageProperties().get(PN_REDIRECT_TYPE, Integer.class);
        // address unspecified or invalid redirect type, assume temporary
        if (null == redirectType || 
                redirectType < SlingHttpServletResponse.SC_MULTIPLE_CHOICES || 
                redirectType > SlingHttpServletResponse.SC_BAD_REQUEST) {
            redirectType = SlingHttpServletResponse.SC_FOUND;
        }
        // generate a redirect path the passed in property or default page prop
        String path = get(PN_REDIRECT_PATH, String.class);
        if (null == path) {
            path = getPageProperties().get(redirectProperty, String.class);
        }
        redirectPath = getRedirect(path);
        
        // redirect should be only performed in disabled or preview mode
        // but not if a looping condition is possible
        if (getWcmMode().isDisabled() || getWcmMode().isPreview()) {
            // no-op if the redirect path is currentPage path
            if (null != redirectPath && !StringUtils.equals(getCurrentPage().getPath(), 
                    StringUtils.substringBefore(path, WCMConstants.DELIMITER_EXTENSION + WCMConstants.HTML))) {
                // only Response#sendRedirect for SC_FOUND
                if (SlingHttpServletResponse.SC_FOUND == redirectType) {
                    getResponse().sendRedirect(redirectPath);
                } else {
                    // other types of 3xx statuses
                    getResponse().setStatus(redirectType);
                    getResponse().setHeader(WCMConstants.HEADER_LOCATION, redirectPath);
                }

            }

        }


    }

    /**
     * Set and return redirect given a path
     * 
     * @param path
     * @return xss scrubbed URL or null
     */
    private String getRedirect(final String path) {
        // immediately return previously acquired target or blank path
        if (null != redirectPath || StringUtils.isBlank(path)) return redirectPath;
        
        String target = null;
        XSSAPI xssApi = getSlingScriptHelper().getService(XSSAPI.class);

        // consider relative paths
        if (StringUtils.startsWith(path, WCMConstants.DELIMITER_PATH)) {
            Page page = WCMUtil.getPage(getRequest(), path);
            if (null != page) {
                if (!page.isValid()) {
                    // empty target for invalid page
                    return redirectPath;
                }
                // attempt to get URL of valid Page
                target = WCMUtil.getPageURL(getRequest(), path);
            } else if (null != getResourceResolver().resolve(path)) {
                // may be a valid resource but not a page
                target = path;
            }
            // path is not a valid, visible resource per request
        } 
        // external location or possibly fully qualified URL
        target = path;
        redirectPath = xssApi.getValidHref(target);
        return redirectPath;
    }
    
    /**
     * @return redirectPath if provided
     */
    public String getRedirect() {
        return redirectPath;
    }
    
    /**
     * @return redirect type
     */
    public int getType() {
        return redirectType;
    }
}
