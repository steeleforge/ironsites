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

import java.io.IOException;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.api.RenditionPicker;
import com.day.cq.wcm.foundation.Image;
import com.steeleforge.aem.ironsites.wcm.MimeTypeExtension;
import com.steeleforge.aem.ironsites.wcm.RenditionSource;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;

/**
 * Image based on original rendition
 */
public class IronImage extends Image {
    // statics
    public static final String PN_LINK = "linkURL";
    public static final String PN_TARGET = "linkURL";
    public static final String SRC_PLACEHOLDER = "/etc/designs/default/0.gif";
    public static final String CLASS_PLACEHOLDER = "cq-placeholder file cq-image-placeholder";
    
    // locals
    private String path;
    private String src;
    private String srcset;
    private String alt;
    private String title;
    private String style;
    private String link;
    private String target;
    private String hash;
    private RenditionPicker rendition;
    private boolean empty = true;

    public IronImage(Resource resource) {
        super(resource);
        if (null == getFileReference() || 
            resource instanceof NonExistingResource) {
            setStyle(IronImage.CLASS_PLACEHOLDER);
            setSrc(IronImage.SRC_PLACEHOLDER);
            setPath(IronImage.SRC_PLACEHOLDER);
        } else {
            empty = false;
            setHash(WCMUtil.getFastHash(resource.getPath()));
            setLink(resource.adaptTo(ValueMap.class).get(PN_LINK, String.class));
            setTarget(resource.adaptTo(ValueMap.class).get(PN_TARGET, String.class));
            try {
                setExtension(MimeTypeExtension.getExtensionByMimeType(super.getMimeType()));
            } catch (RepositoryException re) {
                // NO OP
                // eat this exception
            }
        }
    }
    
    @Override
    protected Resource getReferencedResource(final String path) {
        Resource resource = getResourceResolver().getResource(path);
        if (null != resource) {
            Asset asset = resource.adaptTo(Asset.class);
            if (null != asset) {
                // obtain original asset rendition
                Rendition rendition = asset.getRendition(getRendition());
                resource = (null != rendition)? rendition.adaptTo(Resource.class) : null;
            }
        }
        return resource;
    }

    /**
     * @return width if layer can be retrieved
     */
    public Integer getWidth() {
        try {
            final Integer width = getLayer(false, false, false).getWidth();
            return width;
        } catch (IOException e) {
            // NO OP
            // cannot acquire layer
        } catch (RepositoryException e) {
            // NO OP
            // cannot acquire layer
        }        
        return -1;
    }
    
    /**
     * @return the path
     */
    public String getPath() {
        if (null == path) return super.getPath();
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the src
     */
    public String getSrc() {
        if (null == src) return super.getSrc();
        return src;
    }

    /**
     * @param src the src to set
     */
    public void setSrc(String src) {
        super.setSrc(src);
        this.src = src;
    }

    /**
     * @return the srcset
     */
    public String getSrcset() {
        return srcset;
    }

    /**
     * @param srcset the srcset to set
     */
    public void setSrcset(String srcset) {
        this.srcset = srcset;
    }

    /**
     * @return the alt
     */
    public String getAlt() {
        if (null == alt) return super.getAlt();
        return alt;
    }

    /**
     * @param alt the alt to set
     */
    public void setAlt(String alt) {
        super.setAlt(alt);
        this.alt = alt;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        if (null == title) return super.getTitle();        
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        super.setTitle(title);
        this.title = title;
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * @return the rendition
     */
    public RenditionPicker getRendition() {
        if (null == rendition) {
            return RenditionSource.ORIGINAL;
        }
        return rendition;
    }

    /**
     * @param rendition the rendition to set
     */
    public void setRendition(RenditionPicker rendition) {
        this.rendition = rendition;
    }

    /**
     * @return the empty
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * @param empty the empty to set
     */
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
