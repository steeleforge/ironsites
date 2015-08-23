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

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.sightly.WCMUsePojo;
import com.steeleforge.aem.ironsites.wcm.servlet.AdaptiveImageConfig;

public class ImageUse extends WCMUsePojo {
    // statics
    private static final String PN_PATH_IMAGE = "image";
    private static final String PN_WIDTHS_IMAGE = "widths";
    private static final String DEFAULT_PATH_IMAGE = "/image";
    private static final String ATTR_SOURCE_SRCSET = "srcset";
    private static final String ATTR_SOURCE_MEDIA_MINWIDTH = "minWidth";
    private static final String FMT_SOURCE_SRCSET = "{0}.img.{1,number,#}.high.{2}{3}";
    private static final String FMT_SOURCE_FULL = "{0}.img.full.high.{1}{2}";
    
    // local
    private List<Integer> widths = null;
    private IronImage image = null;
    private List<Map<String, String>> sources = null;
    
    public ImageUse() {
        super();
    }

    public void activate() throws Exception {
        final AdaptiveImageConfig config = getSlingScriptHelper().getService(AdaptiveImageConfig.class);
        image = setImage();
        widths = setWidths(config.getWidths());
        sources = setSources(widths);
    }

    public IronImage getImage() {
        return image;
    }
    
    public List<Map<String, String>> getSources() {
        return sources;
    }
    
    public String getFullSource() {
        return MessageFormat.format(FMT_SOURCE_FULL,
                getImage().getPath(), 
                getImage().getExtension(), 
                getImage().getSuffix());
    }
    
    /**
     * initialize image
     * 
     * @return
     * @throws RepositoryException
     */
    private IronImage setImage() {
        Resource imageResource = null;
        String resourcePath = get(PN_PATH_IMAGE, String.class);
        if (null != resourcePath) {
            imageResource = getResourceResolver().resolve(getResource().getPath() 
                    + resourcePath);
        } else {
            imageResource = getResourceResolver().resolve(getResource().getPath() 
                    + DEFAULT_PATH_IMAGE);
        }
        return new IronImage(imageResource);
    }
    
    /**
     * initialize widths
     * 
     * @return
     * @throws RepositoryException
     */
    private List<Integer> setWidths(List<Integer> configWidths) {
        widths = new LinkedList<Integer>();
        Object[] optionWidths = get(PN_WIDTHS_IMAGE, Object[].class);
        if (ArrayUtils.getLength(optionWidths) > 0) {
            // loop through Use width options for a given image
            // allow only those matching configWidths
            for(Object width : optionWidths) {
                // allow only those matching configWidths
                if (null != width && configWidths.contains(Integer.parseInt(width.toString()))) {
                    widths.add(Integer.parseInt(width.toString()));
                }
            }
        } else {
            // no widths provided to ImageUse
            widths.addAll(configWidths);
        }
        Collections.sort(widths);
        return widths;
    }
    
    /**
     * initialize source tag data
     * 
     * @param widths
     * @return
     */
    private List<Map<String, String>> setSources(List<Integer> widths) {
        sources = new LinkedList<Map<String, String>>();
        if (null != widths) {
            Integer prev = 0;
            for(Integer width : widths) {
                // prevent srcset for widths exceeding original asset width
                Map<String,String> attrs = new HashMap<String, String>();
                prev += 1;
                // update next min-width media query
                // source tag attributes
                attrs.put(ATTR_SOURCE_SRCSET, 
                        MessageFormat.format(FMT_SOURCE_SRCSET,
                                getImage().getPath(), 
                                width, 
                                getImage().getExtension(), 
                                getImage().getSuffix()));
                attrs.put(ATTR_SOURCE_MEDIA_MINWIDTH, Integer.toString(prev));
                prev = width;
                sources.add(attrs);
            }
        }
        Collections.reverse(sources);
        return sources;
    }
}
