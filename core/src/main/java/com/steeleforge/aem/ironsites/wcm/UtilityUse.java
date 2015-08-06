package com.steeleforge.aem.ironsites.wcm;

import org.apache.commons.lang.StringUtils;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.commons.jcr.JcrConstants;

public class UtilityUse extends WCMUse {
    private static final String PN_VALUE = "value";

    String value = null;
    @Override

    public void activate() throws Exception {
         value = get(PN_VALUE, String.class);
         if (StringUtils.isBlank(value)) {
             value = getResource().getPath();
         }
    }
    
    /**
     * @return resource path based identifier
     * @see WCMUtil#getPathBasedIdentifier(String)
     */
    public String getPathId() {
        return WCMUtil.getPathBasedIdentifier(
                StringUtils.replace(getResource().getPath(), 
                        WCMConstants.DELIMITER_PATH + JcrConstants.JCR_CONTENT, 
                        StringUtils.EMPTY));
     }

    /**
     * @return guava hashed value
     * @see WCMUtil#getFastHash(String)
     */
    public String getHashId() {
        return WCMUtil.getFastHash(value);
    }
}
