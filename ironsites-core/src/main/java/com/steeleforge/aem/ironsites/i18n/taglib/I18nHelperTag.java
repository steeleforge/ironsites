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
package com.steeleforge.aem.ironsites.i18n.taglib;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.LanguageManager;
import com.steeleforge.aem.ironsites.i18n.I18nResourceBundle;
import com.steeleforge.aem.ironsites.page.WCMUtil;

public class I18nHelperTag extends TagSupport {
	private static final long serialVersionUID = 3168552551019136674L;
	private static final Logger log = LoggerFactory.getLogger(I18nHelperTag.class);
	
	static final String DEFAULT_I18N_NAME = "i18n";
	static final String DEFAULT_MESSAGES_NAME = "messages";
	private SlingHttpServletRequest request;
	private Source source;
	private Locale language;
	private String baseName;
	private String messagesName;
	private String i18nName;

	public int doEndTag() {
		try {
			init();
			
			ValueMap values = (ValueMap)request.getResource().adaptTo(ValueMap.class);
			
			I18nResourceBundle bundle = new I18nResourceBundle(values,
					getResourceBundle(getBaseName(), request));
			
			// make this resource bundle & i18n available as ValueMap for TEI
			pageContext.setAttribute(getMessagesName(), bundle.adaptTo(ValueMap.class), PageContext.PAGE_SCOPE);
			pageContext.setAttribute(getI18nName(), new I18n(bundle), PageContext.PAGE_SCOPE);

			// make this resource bundle available for fmt functions
			Config.set(pageContext, "javax.servlet.jsp.jstl.fmt.localizationContext", new LocalizationContext(bundle, getLocale(request)), 1);
		} catch(Exception e) {
			log.error(e.getMessage());
		}
		return EVAL_PAGE;
	}

	public void setSource(String source) throws JspException {
		try {
			this.source = Source.valueOf(source.toUpperCase());
		}
		catch(IllegalArgumentException e) {
			log.error("Source must match: " + Arrays.toString(Source.values()), e.getMessage());
		}
	}

	public String getSource() {
		return source.toString();
	}

	public String getLanguage() {
		return language.getLanguage();
	}

	public void setLanguage(String language) {
		this.language = new Locale(language);
	}

	private ResourceBundle getResourceBundle(String baseName, SlingHttpServletRequest request) {
		return request.getResourceBundle(baseName, getLocale(request));
	}

	private Locale getLocale(SlingHttpServletRequest request) {
		Source source = this.source;
		if (null == source) {
			source = language == null ? Source.AUTO : Source.STATIC;
		}
		if (source == Source.STATIC) {
			return language == null ? Locale.getDefault() : language;
		}

		Locale locale = null;
		if (source == Source.PAGE)  {
			locale = getPageLocale(request.getResource());
			if (null != locale) {
				return locale;
			} else {
				return language == null ? Locale.getDefault() : language;
			}
		}
		if (Source.REQUEST == source) {
			return request.getLocale();
		}

		locale = getPageLocale(request.getResource());
		if (null != locale) {
			return locale;
		}
		return request.getLocale();
	}

	private Locale getPageLocale(Resource resource) {
		SlingScriptHelper scriptHelper = WCMUtil.getSlingScriptHelper(pageContext);
		LanguageManager languageManager = (LanguageManager)scriptHelper.getService(LanguageManager.class);
		if (null == languageManager) {
			return null;
		}
		return languageManager.getLanguage(resource);
	}

	public void release() {
		request = null;
		source = null;
		language = null;
		baseName = null;
		i18nName = null;
	}
	
	private void init() {
		request = WCMUtil.getSlingRequest(pageContext);
		if (StringUtils.isBlank(getBaseName())) {
			setBaseName(request.getResource().getResourceType());
		}
		if (StringUtils.isBlank(getI18nName())) {
			setI18nName(DEFAULT_I18N_NAME);
		}
		if (StringUtils.isBlank(getMessagesName())) {
			setMessagesName(DEFAULT_MESSAGES_NAME);
		}
	}

	private static enum Source {
		STATIC, PAGE, REQUEST, AUTO;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

	public String getMessagesName() {
		return messagesName;
	}

	public void setMessagesName(String messagesName) {
		this.messagesName = messagesName;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String i18nName) {
		this.i18nName = i18nName;
	}
}
