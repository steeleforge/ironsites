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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import com.day.cq.commons.Filter;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.foundation.Sitemap;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;
import com.steeleforge.aem.ironsites.wcm.page.filter.HidePageFilter;

/**
 * IronSitemap builds on the Sitemap pattern and can be used to generate
 * an XML sitemap as well. This version allows for inclusion and exclusion
 * of paths. Inclusions are assumed at level 0 and exclusion takes precedence.
 * Lastly a configurable Filter<Page> can be utilized.
 *
 * @author David Steele
 */
public class IronSitemap extends Sitemap {
    private SlingHttpServletRequest request = null;
    private Map<Integer, List<String>> paths = null;
    private List<String> inclusions = null;
    private List<String> exclusions = null;
    private Filter<Page> filter = null;
    private static final int ROOT_LEVEL = 0;

    /**
     * Constructor takes a sling request, root, page filter, included paths, 
     * and excluded paths
     * 
     * @param request
     * @param root
     * @param filter
     * @param inclusions
     * @param exclusions
     */
    public IronSitemap(SlingHttpServletRequest request, Page root, Filter<Page> filter, List<String> inclusions, List<String> exclusions) {
        super(null);
        this.request = request;
        this.filter = filter;
        this.inclusions = inclusions;
        this.exclusions = exclusions;
        if (null != root) {
            populatePaths(root, ROOT_LEVEL);
            populateInclusions(root);
            populateLinks(root);
        }
    }
    
    /**
     * This is not the preferred constructor but should be supported.
     * Lazy accessors make this relatively null-safe
     * Do.Not.Use.
     * 
     * @param root
     */
    public IronSitemap(Page root) {
        this(null, root, null, null, null);
    }

    /**
     * Similar to {@link Sitemap#buildLinkAndChildren(Page, int)} but paths are
     * collected instead of instantiating new Sitemap.Link objects.
     * 
     * @param page
     * @param level
     */
    private void populatePaths(Page page, int level) {
        addPath(level, page.getPath());

        Iterator<Page> children = page.listChildren(getFilter());
        while (children.hasNext()) {
            Page child = (Page)children.next();
            populatePaths(child, level + 1);
        }
    }
    
    /**
     * Determine inclusion path level and ammend it to paths map
     * 
     * @param page root page
     */
    private void populateInclusions(Page root) {
        int base = root.getDepth();
        int level = -1;
        for(String path : getInclusions()) {
            // handle external links gracefully, assume level 0
            if (StringUtils.startsWith(path,"/")) {
                level = base - StringUtils.countMatches(path, "/") - 1;
            } else {
                level = -1;
            }
            if (level < 0) {
                level = ROOT_LEVEL;
            }
            getPaths(level).add(path);
        }
    }
    
    /**
     * Use path map to generate a collection of Sitemap.Link objects
     * 
     * @param root
     */
    private void populateLinks(Page root) {
        PageManager pageManager = root.getPageManager();
        // paths per level
        for(Integer level : getPaths().keySet()) {
            for(String path : getPaths(level)) {
                // only create a Link for paths that are not excluded
                if (!getExclusions().contains(path)) {
                    getLinks().add(createLink(pageManager, path, level));
                }
            }
        }
    }
    
    /**
     * Link generation to support internal and external links. Supports URI=Title format.
     * 
     * @param pageManger CQ API PageManager
     * @param path direct or relative URI
     * @param level sitemap level
     */
    private Link createLink(PageManager pageManager, String path, int level) {
        Link link = null;
        // internal links
        if (StringUtils.startsWith(path, "/")) {
            Page page = pageManager.getPage(path);
            // prevent non-existent, invalid, and hidden pages
            if (getFilter().includes(page)) {
                link = new Link(WCMUtil.getPageURL(request, page.getPath()), WCMUtil.getPageTitle(page), level);
            }
        } else {
            // support external links with URL=title format
            String[] parts = StringUtils.split(path, "=");
            if (parts.length > 1) {
                link = new Link(WCMUtil.getExternalURL(request, parts[0], null, null), parts[1], level);
            } else {
                link = new Link(WCMUtil.getExternalURL(request, parts[0], null, null), parts[0], level);
            }
        }
        return link;
    }

    /**
     * Page paths by level
     * 
     * @return sitemap paths
     */
    public Map<Integer, List<String>> getPaths() {
        if (null == paths) {
            paths = new LinkedHashMap<Integer,List<String>>();
        }
        return paths;
    }
    
    /**
     * Page paths for a given level
     * 
     * @param level
     * @return sitemap paths for a given level
     */
    public List<String> getPaths(int level) {
        if (!getPaths().containsKey(level)) {
            getPaths().put(level, new ArrayList<String>());
        }
        return getPaths().get(level);
    }

    /**
     * Add a page path at a given level.
     * 
     * @param level
     * @param path
     */
    private void addPath(int level, String path) {
        getPaths(level).add(path);
    }

    /**
     * Lazy accessor to inclusion list
     * 
     * @return exclusion paths
     */
    private List<String> getInclusions() {
        if (null == inclusions) {
            inclusions = new ArrayList<String>();
        }
        return inclusions;
    }
    
    /**
     * Lazy accessor to exclusion list
     * 
     * @return exclusion paths
     */
    private List<String> getExclusions() {
        if (null == exclusions) {
            exclusions = new ArrayList<String>();
        }
        return exclusions;
    }

    /**
     * Default usage is presently HidePageFilter.HIDE_IN_NAVIGATION_FILTER
     * 
     * @return page filter
     */
    private Filter<Page> getFilter() {
        if (null == filter) {
            filter = HidePageFilter.HIDE_IN_NAVIGATION_FILTER;
        }
        return filter;
    }
}

