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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.steeleforge.aem.ironsites.wcm.WCMConstants;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;
import com.steeleforge.aem.ironsites.wcm.page.head.Hreflang;
import com.steeleforge.aem.ironsites.wcm.page.head.Icon;
import com.steeleforge.aem.ironsites.wcm.page.head.Meta;

public class PageUse extends WCMUsePojo {
    // statics
    public static final String PN_CATEGORIES = "categories";
    public static final String PN_JS = "js";
    public static final String PN_CSS = "css";
    public static final String PN_PROTOCOL = "protocol";
    public static final String PN_SITE = "site";
    public static final String PN_FAVICON = "favicon";
    public static final String PN_ICONS = "icons";
    public static final String PN_META = "meta";
    public static final String PN_CANONICAL = "canonical";
    public static final String PN_HREFLANG = "hreflang";
    public static final String PN_LAYOUT = "layout";
    
    // locals
    private String keywords = null;
    private String title = null;
    private String navTitle = null;
    private String template = null;
    private String designPath = null;
    private String canonical = null;
    private String favicon = null;
    private String protocol = null;
    private String site = null;
    private String layout = null;
    private List<String> categories = null;
    private List<String> js = null;
    private List<String> css = null;
    private List<Icon> icons = null;
    private List<Meta> meta = null;
    private List<Hreflang> hreflang = null;
    
    public PageUse() {
        super();
    }

    @Override
    public void activate() throws Exception {
        // noop
    }
    
    /**
     * @return locale page
     */
    public Page getLocalePage() {
    	Page localePage = null;
    	final String languageRoot = WCMUtil.getLanguageRoot(getCurrentPage().getPath());
    	if (StringUtils.isNotBlank(languageRoot)) {
    		localePage = WCMUtil.getPage(getRequest(), languageRoot);
    	}
    	return localePage;
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
        canonical = getInheritedProperties().get(PN_CANONICAL, String.class);
        if (null == canonical) {
	        canonical = WCMUtil.getExternalPageURL(getRequest(), 
	        		getCurrentPage().getPath(), getProtocol(), getSite());
        }
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
	 * @return the protocol
	 */
	public String getProtocol() {
        if (null != protocol) return protocol;
        protocol = getInheritedProperties().get(PN_PROTOCOL, String.class);
        if (StringUtils.isBlank(protocol)) {
        	protocol = WCMConstants.HTTP;
        	final String extd = WCMUtil.getExternalPageURL(getRequest(), 
        			getCurrentPage().getPath(), null, getSite());
        	// pluck protocol from externalized path
        	if (StringUtils.startsWith(extd, WCMConstants.HTTPS)) {
        		protocol = WCMConstants.HTTPS;
        	}
        }
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
	 * @param canonical the canonical to set
	 */
	public void setCanonical(String canonical) {
		this.canonical = canonical;
	}

	/**
	 * @return the layout
	 */
	public String getLayout() {
		return layout;
	}
	
	// TODO
	public List<String> getLayouts() {
		return Collections.emptyList();
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(String layout) {
		this.layout = layout;
	}

	/**
	 * ClientLibrary categories
	 * 
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
	 * @return the hreflang language alternatives
	 */
	public List<Hreflang> getHreflang() {
        if (null != hreflang) return hreflang;
        final String[] inherited = getInheritedProperties().get(PN_HREFLANG, String[].class);
        if (ArrayUtils.getLength(inherited) > 0) {
        	hreflang = new ArrayList<Hreflang>();
        	for (String item : inherited) hreflang.add(new Hreflang(item));
        } else {
        	hreflang = new ArrayList<Hreflang>();
        	final Page localePage = getLocalePage();
    		if (null != localePage && null != localePage.getParent()) {
            	// get same path siblings for language alternates
    			final Page parent = localePage.getParent();
				final Iterator<Page> children = parent.listChildren();
    			final String relSubpath = StringUtils.substringAfterLast(getCurrentPage().getPath(), 
    					localePage.getPath());
    			// non-final as it will be reassigned as iterated
				Page siblingOrSelf = null;
				// compare each sibling subpath
				// for a same-name name resource stub
				while(children.hasNext()) {
					siblingOrSelf = children.next();
					if (!localePage.equals(siblingOrSelf)) {
						// sibling rel path ... may not exist
						siblingOrSelf = getPageManager().getContainingPage(siblingOrSelf.getPath()
								+ WCMConstants.DELIMITER_PATH + relSubpath);
						// does the sibling with same subpath exist?
						if (null != siblingOrSelf) {
							// add externalized sibling path if it exists
    						hreflang.add(new Hreflang(siblingOrSelf.getLanguage(false).toString()
    								+ WCMConstants.DELIMITER_MULTIFIELD
    								+ WCMUtil.getExternalPageURL(getRequest(), 
    										siblingOrSelf.getPath(), getProtocol(), getSite())));
						}
					}
				}
				siblingOrSelf = null;
			}
        }
		return hreflang;
	}

	/**
	 * @param hreflang the hreflang to set
	 */
	public void setHreflang(List<Hreflang> hreflang) {
		this.hreflang = hreflang;
	}
}