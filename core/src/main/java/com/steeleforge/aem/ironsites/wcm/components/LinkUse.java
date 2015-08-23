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
package com.steeleforge.aem.ironsites.wcm.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import com.steeleforge.aem.ironsites.wcm.WCMConstants;
import com.steeleforge.aem.ironsites.wcm.WCMURLBuilder;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;
import com.steeleforge.aem.ironsites.wcm.page.filter.HidePageFilter;
import com.steeleforge.aem.ironsites.wcm.page.filter.InvalidPageFilter;

public class LinkUse extends WCMUsePojo {
    // statics
    public static final String PN_PATH = "path";
    public static final String PN_TEXT = "text";
    public static final String PN_NAVTITLE = "navTitle";
    public static final String PN_HIDEINNAV = "hideInNav";
    
    // local
    private String path;
    private Link link;
    private Page page;
    private Boolean navTitle = false;
    private Boolean hideInNav = true;
    
    private String id;
    private String href;
    private String name;
    private String styles;
    private String target;
    private String rel;
    private String download;
    private String media;
    private String hreflang;
    private String type;
    private Boolean ping;
    private String text;
    
    public LinkUse() {
        super();
    }

    @Override    
    public void activate() throws Exception {
        path = get(PN_PATH, String.class);
        if (null == path) path = getProperties().get(PN_PATH, String.class);
        page = WCMUtil.getPage(getRequest(), path);
        navTitle = get(PN_NAVTITLE, Boolean.class);
        navTitle = (null == navTitle)? false : true;
        hideInNav = get(PN_HIDEINNAV, Boolean.class);
        hideInNav = (null == hideInNav)? true : false;
        
        id = get(Link.PN_ID, String.class);
        href = get(Link.PN_HREF, String.class);
        name = get(Link.PN_NAME, String.class);
        styles = get(Link.PN_STYLES, String.class);
        target = get(Link.PN_TARGET, String.class);
        rel = get(Link.PN_REL, String.class);
        download = get(Link.PN_DOWNLOAD, String.class);
        media = get(Link.PN_MEDIA, String.class);
        hreflang = get(Link.PN_HREFLANG, String.class);
        type = get(Link.PN_TYPE, String.class);
        ping = get(Link.PN_PING, Boolean.class);
        text = get(Link.PN_TEXT, String.class);
        if (null == text) text = getProperties().get(PN_TEXT, String.class);
        
        link = setLink();
    }
    
    private Link setLink() {
        link = (null != page)? new Link(getRequest(), page) : new Link();
        if (null != page) {
            link.setPage(page.getPath());
            if (getCurrentPage().getPath().equals(page.getPath())) {
                link.setCurrent(true);
            }
            if (StringUtils.startsWith(getCurrentPage().getPath(),
                       page.getPath() + WCMConstants.DELIMITER_PATH)) {
                link.setDescendent(true);
            }
            if (navTitle) {
                text = WCMUtil.getPageNavigationTitle(page);
            } else {
                text = WCMUtil.getPageTitle(page);
            }
        } else {
            final String[] pathTokens = StringUtils.split(path, WCMConstants.DELIMITER_MULTIFIELD);
            if (ArrayUtils.getLength(pathTokens) > 1) {
                // <path>|<title>
                path = pathTokens[0];
                if (StringUtils.isBlank(text)) text = pathTokens[1];
            } else {
                text = path;
            }
        }

        link.setText(text);
        path = new WCMURLBuilder(getRequest()).setPath(path).build();
        link.setHref(path);
        
        if (null != id) link.setId(id);
        if (null != href) link.setHref(href);
        if (null != name) link.setName(name);
        if (null != styles) link.setStyles(styles);
        if (null != target) link.setTarget(target);
        if (null != rel) link.setRel(rel);
        if (null != download) link.setDownload(download);
        if (null != media) link.setMedia(media);
        if (null != hreflang) link.setHreflang(hreflang);
        if (null != type) link.setType(type);
        if (null != ping) link.setPing(ping);
        
        return link;
    }

    public Link getLink() {
        return link;
    }
    
    public List<Link> getLinks() {
        if (null != page) {
            List<Link> links = new ArrayList<Link>();
            Iterator<Page> children = (hideInNav)? 
                       page.listChildren(HidePageFilter.HIDE_IN_NAVIGATION_FILTER)
                       : page.listChildren(InvalidPageFilter.INVALID_PAGE_FILTER);
            while(children.hasNext()) {
                links.add(new Link(getRequest(), children.next()));
            }
            return links;
        }
        return Collections.emptyList();
    }
}
