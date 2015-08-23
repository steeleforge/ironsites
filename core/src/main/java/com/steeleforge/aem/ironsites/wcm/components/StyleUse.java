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
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.steeleforge.aem.ironsites.wcm.WCMConstants;

public class StyleUse extends WCMUsePojo {
    private static final String PN_STYLE = "style";
    
    private String[] style = null;
    
    public StyleUse() {
        super();
    }

    @Override    
    public void activate() throws Exception {
        // if style is not provided to options, acquire from component
        style = get(PN_STYLE, String[].class);
        if (null == style) style = getProperties()
                .get(PN_STYLE, String[].class);
    }

    /**
     * @param tagIDs
     * @return list of valid tags given array of IDs
     */
    private List<Tag> getTags(final String[] tagIDs) {
        if (0 == ArrayUtils.getLength(tagIDs)) return Collections.emptyList(); 
        final TagManager tm = getResourceResolver().adaptTo(TagManager.class);
        List<Tag> tags = new ArrayList<Tag>();
        
        Tag tag = null;
        for(String id : tagIDs) {
            tag = tm.resolve(id);
            if (null != tag) tags.add(tag);
            tag = null;
        }
        
        return tags;
    }
    
    /**
     * @param tagIDs
     * @return list of tag descriptions given array of IDs
     */
    public List<String> getTagDescriptions(final String[] tagIDs) {
        List<String> descriptions = new ArrayList<String>();
        for(Tag tag : getTags(style)) {
            if (StringUtils.isNotBlank(tag.getDescription())) {
                descriptions.add(tag.getDescription());
            }
        }
        return descriptions;
    }
    
    /**
     * @return style string from style option or property
     */
    public String getStyles() {
        return StringUtils.join(getTagDescriptions(style), WCMConstants.DELIMITER_COMMA_SPACED);
    }
}
