package com.steeleforge.aem.ironsites.wcm.link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;
import com.steeleforge.aem.ironsites.wcm.page.filter.HidePageFilter;
import com.steeleforge.aem.ironsites.wcm.page.filter.InvalidPageFilter;

public class LinkUse extends WCMUse {
    // statics
    public static final String PN_PATH = "path";
    public static final String PN_NAVTITLE = "navTitle";
    public static final String PN_HIDEINNAV = "hideInNav";
    
    // local
    private String path;
    private Link link;
    private Page page;
    private Boolean navTitle;
    private Boolean hideInNav;
    
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
    
    @Override
    public void activate() throws Exception {
        path = get(PN_PATH, String.class);
        if (null == path) path = getProperties().get(PN_PATH, String.class);
        page = WCMUtil.getPage(getRequest(), path);
        navTitle = get(PN_NAVTITLE, null);
        navTitle = (null == navTitle)? false : true;
        hideInNav = get(PN_HIDEINNAV, null);
        hideInNav = (null == hideInNav)? true : false;
        path = new WCMURLBuilder(getRequest()).setPath(path).build();
        
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
        ping = get(Link.PN_PING, null);
        text = get(Link.PN_TEXT, String.class);
        
        link = setLink();
    }
    
    private Link setLink() {
        link = (null != page)? new Link(page) : new Link();
        if (null != page) {
            if (navTitle) {
                link.setText(WCMUtil.getPageNavigationTitle(page));
            }
        } else {
            link.setText(text);
            link.setHref(path);
        }
        
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
                links.add(new Link(children.next()));
            }
            return links;
        }
        return Collections.emptyList();
    }
}
