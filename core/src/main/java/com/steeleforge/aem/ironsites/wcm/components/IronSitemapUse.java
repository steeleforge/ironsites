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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.foundation.Sitemap.Link;
import com.steeleforge.aem.ironsites.wcm.page.filter.HidePageFilter;

/**
 * IronSitemapUse leverages sightly WCMUse API to populate IronSitemap
 *
 * @author David Steele
 */
public class IronSitemapUse extends WCMUse {
    private IronSitemap sitemap = null;
    
    @Override
    public void activate() throws Exception {
        String path = getProperties().get("path", "");
        String hide = getProperties().get("hideProperty", "hideInNav");
        String[] inclusions = getProperties().get("inclusions", new String[0]);
        String[] exclusions = getProperties().get("exclusions", new String[0]);

        if (path.length() == 0 && null != getCurrentPage().getParent()) {
            path = getCurrentPage().getParent().getPath();
        }
        
        Page root = getPageManager().getPage(path);
        sitemap = new IronSitemap(getRequest(),
                root, 
                new HidePageFilter(hide), 
                Arrays.asList(inclusions), 
                Arrays.asList(exclusions));
    }
    
    /**
     * WCMUse accessor method
     * 
     * @return Sitemap#getLinks() or empty list
     */
    public List<Link> getLinks() {
        if (null != sitemap) {
            return sitemap.getLinks();
        }
        return Collections.emptyList();
    }
    
    /**
     * @return IronSitemap instance
     */
    public IronSitemap getSitemap() {
        return sitemap;
    }
}

