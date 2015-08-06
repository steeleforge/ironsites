package com.steeleforge.aem.ironsites.wcm.page.head;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.steeleforge.aem.ironsites.wcm.WCMConstants;

/**
 * Page icons for <link/> tags
 * 
 * @author david
 */
public class Icon {
    // locals
    private String rel;
    private String sizes;
    private String href;
    private String type;
    
    // constructor
    public Icon(final String multifield) {
        String[] fields = StringUtils.split(multifield, WCMConstants.DELIMITER_MULTIFIELD);
        if (ArrayUtils.getLength(fields) > 2) {
            setRel(fields[0]);
            setSizes(fields[1]);
            setHref(fields[2]);
            if (ArrayUtils.getLength(fields) > 3) {
                setType(fields[3]);
            }
        }
    }

    /**
     * @return the rel
     */
    public String getRel() {
        return rel;
    }

    /**
     * @param rel the rel to set
     */
    public void setRel(final String rel) {
        this.rel = rel;
    }

    /**
     * @return the sizes
     */
    public String getSizes() {
        return sizes;
    }

    /**
     * @param sizes the sizes to set
     */
    public void setSizes(final String sizes) {
        this.sizes = sizes;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href the href to set
     */
    public void setHref(final String href) {
        this.href = href;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
