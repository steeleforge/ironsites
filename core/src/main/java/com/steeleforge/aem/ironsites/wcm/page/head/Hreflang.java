package com.steeleforge.aem.ironsites.wcm.page.head;

import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.day.cq.commons.LanguageUtil;
import com.steeleforge.aem.ironsites.wcm.WCMConstants;

/**
 * Hreflang icons for <link/> tags
 * 
 * @author david
 */
public class Hreflang {
    // statics
    public static String HREFLANG_XDEFAULT = "x-default";
    
    // locals
    private Locale locale = Locale.getDefault();
    private String language = Locale.getDefault().toString();
    private String url;
    
    public Hreflang(final String multifield) {
        String[] fields = StringUtils.split(multifield, WCMConstants.DELIMITER_MULTIFIELD);
        if (ArrayUtils.getLength(fields) > 1) {
    		if (StringUtils.isNotBlank(fields[0]) &&
    			null != LanguageUtil.getLocale(fields[0])) {
    			setLocale(LanguageUtil.getLocale(fields[0]));
    			setLanguage(getLocale().toString());
    		}
            setUrl(fields[1]);
        }
    }

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
