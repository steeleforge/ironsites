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
package com.steeleforge.aem.ironsites.wcm.page.grid;

import java.util.List;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

public class ContainerUse extends WCMUsePojo {
    public static final String PN_LAYOUT = "layout";
    public static final String PN_LAYOUT_ROOT = "layoutRoot";
    public static final String DEFAULT_LAYOUT_ROOT = "/etc/tags/ironsites/styles/templates";
    
    // locals
    private TagItem layout = null;
    private List<TagItem> layouts = null;
    private TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
    
    public ContainerUse() {
        super();
    }

    @Override
    public void activate() throws Exception {
        getLayouts();
        getLayout();
    }

    /**
     * @return available layouts
     */
    public List<TagItem> getLayouts() {
        if (null != layouts) return layouts;
        final String root = (null != get(PN_LAYOUT_ROOT, String.class))? 
        		get(PN_LAYOUT_ROOT, String.class) : DEFAULT_LAYOUT_ROOT;
        final Tag base = tagManager.resolve(root);
        if (null != base){
        	final TagItem ti = TagItem.getInstance(base);
        	if (null != ti) {
        		layouts = ti.getItems();
        	}
        }
        return layouts;
    }
    
    /**
     * @return configured layout (if provided)
     */
    public TagItem getLayout() {
        if (null != layout) return layout;
    	final String layoutTagID = getPageProperties().get(PN_LAYOUT, "");
        final Tag layoutTag = tagManager.resolve(layoutTagID);
        layout = TagItem.getInstance(layoutTag);
    	return layout;
    }    
}