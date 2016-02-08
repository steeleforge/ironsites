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
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.day.cq.wcm.api.Page;
import com.steeleforge.aem.ironsites.wcm.components.Link;

public class BreadcrumbUse extends LocaleUse {    
    // locals
    private List<Link> links = null;
    private Link homepage = null;
    
    public BreadcrumbUse() {
        super();
    }

    @Override
    public void activate() throws Exception {
        getLinks();
        getHomepage();
    }

    public List<Link> getLinks() {
    	if (null != links) return links;
    	links = new ArrayList<Link>();
    	getLinksInternal(links, getCurrentPage());
    	Collections.reverse(links);
    	return links;
    }

    public Link getHomepage() {
    	if (null != homepage) return homepage;
    	if (StringUtils.isNotBlank(getHomepagePath())) {
    		homepage = new Link(getRequest(), getHomepagePath());
    	}
    	return homepage;
    }
    
    private void getLinksInternal(List<Link> links, Page page) {
    	if (null == links || null == page) return;
		if (null == page.getParent() || isLanguageRoot(page)) {
			getLinksInternal(links, null);
		} else {
			links.add(new Link(getRequest(), page));
			getLinksInternal(links, page.getParent());
		}
    }
    
    private boolean isLanguageRoot(Page page) {
    	return StringUtils.equals(getLocalePath(), page.getPath());
    }
}