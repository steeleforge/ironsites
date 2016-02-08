package com.steeleforge.aem.ironsites.wcm.page;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.day.cq.wcm.api.Page;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;
import com.steeleforge.aem.ironsites.wcm.api.ApiService;

public class LocaleUse extends PageUse {
	// statics
    public static final String PN_API = "api";
	public static final String PN_HOMEPAGE_PATH = "homepage";
	
	// locals
    private String localePath = null;
    private String homepagePath = null;
    
	public LocaleUse() {
		super();
	}

    @Override
    public void activate() throws Exception {
    	final Page localePage = getPageManager()
    			.getContainingPage(WCMUtil
    					.getLanguageRoot(getResource().getPath()));
    	if (null != localePage) {
    		localePath = localePage.getPath();
    		homepagePath = localePage.getProperties().get(PN_HOMEPAGE_PATH, StringUtils.EMPTY);
    	}
    }
    
	/**
	 * @return the localePath
	 */
	public String getLocalePath() {
		return localePath;
	}

	/**
	 * @param localePath the localePath to set
	 */
	public void setLocalePath(String localePath) {
		this.localePath = localePath;
	}

	/**
	 * @return the homepagePath
	 */
	public String getHomepagePath() {
		return homepagePath;
	}

	/**
	 * @param homepagePath the homepagePath to set
	 */
	public void setHomepagePath(String homepagePath) {
		this.homepagePath = homepagePath;
	}

	/**
     * @return set of configured APIs for a given site
     */
    public Set<String> getApis() {
        ApiService api = getSlingScriptHelper().getService(ApiService.class);
        return api.getServices(getSite());        
    }
    
    /**
     * Given a service option, return API value if available
     * 
     * @param serviceID
     * @return
     */
    public String getApi() {
        ApiService api = getSlingScriptHelper().getService(ApiService.class);
        if (null != api) {
            return api.getApi(getSite(), get(PN_API, String.class));
        }
        return null;
    }
}
