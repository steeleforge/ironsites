package com.steeleforge.aem.ironsites.wcm.page.head;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.steeleforge.aem.ironsites.wcm.WCMConstants;

/**
 * Page icons for <link/> tags
 * 
 * @author david
 */
public class Meta {
    // locals
    private String name;
    private String content;
    
    public Meta(final String multifield) {
        String[] fields = StringUtils.split(multifield, WCMConstants.DELIMITER_MULTIFIELD);
        if (ArrayUtils.getLength(fields) > 1) {
            setName(fields[0]);
            setContent(fields[1]);
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(final String content) {
        this.content = content;
    }
}
