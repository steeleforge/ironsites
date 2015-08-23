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
package com.steeleforge.aem.ironsites.wcm.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.steeleforge.aem.ironsites.wcm.WCMConstants;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;
import com.steeleforge.aem.ironsites.wcm.api.ApiService;
import com.steeleforge.aem.ironsites.wcm.page.head.Icon;
import com.steeleforge.aem.ironsites.wcm.page.head.Meta;

public class PageUse extends WCMUsePojo {
    // statics
    public static final String PN_CATEGORIES = "categories";
    public static final String PN_JS = "js";
    public static final String PN_CSS = "css";
    public static final String PN_SITE = "site";
    public static final String PN_FAVICON = "favicon";
    public static final String PN_ICONS = "icons";
    public static final String PN_META = "meta";
    public static final String PN_API = "api";
    
    // locals
    private String keywords = null;
    private String title = null;
    private String navTitle = null;
    private String template = null;
    private String designPath = null;
    private String canonical = null;
    private String favicon = null;
    private String site = null;
    private List<String> categories = null;
    private List<String> js = null;
    private List<String> css = null;
    private List<Icon> icons = null;
    private List<Meta> meta = null;
    
    public PageUse() {
        super();
    }

    @Override
    public void activate() throws Exception {
        // noop
    }
    
    /**
     * @return current page path
     */
    public String getPath() {
        return getCurrentPage().getPath();
    }
    
    /**
     * @return current page description
     */
    public String getDescription() {
        return getCurrentPage().getDescription();
    }
        
    /**
     * @return tag title keywords
     */
    public String getKeywords() {
        if (null != keywords || null == getCurrentPage()) return keywords;
        // generate keywords from localized tag titles
        final Tag[] tags = getCurrentPage().getTags();
        if (ArrayUtils.getLength(tags) > 0 ) {
            StringBuilder titles = new StringBuilder();
            // append comma delimited tag titles
            for(Tag tag : tags) {
                if (titles.length() > 0) titles.append(WCMConstants.DELIMITER_COMMA);
                titles.append(tag.getTitle(getCurrentPage().getLanguage(false)));
            }
            if (titles.length() > 0) keywords = titles.toString();
        }
        return keywords;
    }
    
    /**
     * @return design path
     */
    public String getDesignPath() {
        if (null != designPath || null == getCurrentDesign()) return designPath;
        designPath = getCurrentDesign().getPath();
        return designPath;
    }
    
    /**
     * @return page title
     */
    public String getTitle() {
        if (null != title || null == getCurrentPage()) return title;
        title = WCMUtil.getPageTitle(getCurrentPage());
        return title;
    }
    
    /**
     * @return navigation title
     */
    public String getNavTitle() {
        if (null != navTitle) return navTitle;
        navTitle = WCMUtil.getPageNavigationTitle(getCurrentPage());
        return navTitle;
    }
    
    /**
     * @return template slug
     */
    public String getTemplate() {
        if (null != template || null == getCurrentPage()) return template;
        template = StringUtils.substringAfterLast(getCurrentPage().getTemplate().getPath(), WCMConstants.DELIMITER_PATH);
        return template;
    }
    
    /**
     * @return canonical URL
     */
    public String getCanonical() {
        if (null != canonical || null == getCurrentPage()) return canonical;
        canonical = WCMUtil.getPageURL(getRequest(), getCurrentPage().getPath());
        return canonical;
    }
    
    /**
     * @return path based or base favicon path
     */
    public String getFavicon() {
        if (null != favicon) return favicon;
        favicon = getInheritedProperties().get(PN_FAVICON, String.class);
        if (null == favicon) {
            if (null != getResourceResolver().resolve(getDesignPath() + WCMConstants.PATH_FAVICON)) {
                favicon = getDesignPath() + WCMConstants.PATH_FAVICON;
            } else {
                favicon = WCMConstants.PATH_FAVICON;
            }
        }
        return favicon;
    }
    
    /**
     * @return get inherited site identifier
     */
    public String getSite() {
        if (null != site) return site;
        site = getInheritedProperties().get(PN_SITE, String.class);
        return site;
    }
    
    /**
     * @return inherited categories
     */
    public List<String> getCategories() {
        if (null != categories) return categories;
        final String[] inherited = getInheritedProperties().get(PN_CATEGORIES, String[].class);
        if (ArrayUtils.getLength(inherited) > 0) {
            categories = new ArrayList<String>();
            for(String item : inherited) {
                if (StringUtils.isNotBlank(item)) categories.add(item);
            }
        }
        return categories;
    }
    
    /**
     * @return delimited category names
     */
    public String getNamedCategories() {
        return StringUtils.join(getCategories(), WCMConstants.DELIMITER_COMMA);
    }
    
    /**
     * @return current page name
     */
    public String getName() {
        return getCurrentPage().getName();
    }

    /**
     * @return inherited javascript include paths
     */
    public List<String> getJs() {
        if (null != js) return js;
        final String[] inherited = getInheritedProperties().get(PN_JS, String[].class);
        if (ArrayUtils.getLength(inherited) > 0) {
            js = new ArrayList<String>();
            for(String path : inherited) {
                if (null != getResourceResolver().resolve(path)) js.add(path);
            }
        }
        return js;
    }

    /**
     * @return inherited css include paths
     */
    public List<String> getCss() {
        if (null != css) return css;
        final String[] inherited = getInheritedProperties().get(PN_CSS, String[].class);
        if (ArrayUtils.getLength(inherited) > 0) {
            css = new ArrayList<String>();
            for(String path : inherited) {
                if (null != getResourceResolver().resolve(path)) css.add(path);
            }
        }
        return css;
    }
    
    /**
     * @return inherited icons
     */
    public List<Icon> getIcons() {
        if (null != icons) return icons;
        final String[] inherited = getInheritedProperties().get(PN_ICONS, String[].class);
        if (ArrayUtils.getLength(inherited) > 0) {
            icons = new ArrayList<Icon>();
            for(String item : inherited) icons.add(new Icon(item));
        }
        return icons;
    }
    
    /**
     * @return inherited metatags
     */
    public List<Meta> getMeta() {
        if (null != meta) return meta;
        final String[] inherited = getInheritedProperties().get(PN_META, String[].class);
        if (ArrayUtils.getLength(inherited) > 0) {
            meta = new ArrayList<Meta>();
            for(String item : inherited) meta.add(new Meta(item));
        }
        return meta;
    }
    
    /**
     * @return set of configured APIs for a given site
     */
    public Set<String> getApis() {
        ApiService api = getSlingScriptHelper().getService(ApiService.class);
        return api.getServices(getSite());        
    }
    
    /**
     * Given a service option, return API value if available
     * 
     * @param serviceID
     * @return
     */
    public String getApi() {
        ApiService api = getSlingScriptHelper().getService(ApiService.class);
        if (null != api) {
            return api.getApi(getSite(), get(PN_API, String.class));
        }
        return null;
    }
}