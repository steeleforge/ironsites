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
package com.steeleforge.aem.ironsites.page.filter;

import org.apache.commons.lang.StringUtils;

import com.steeleforge.aem.ironsites.page.filter.property.BooleanEqualsFilter;

/**
 * Page Filter checking for page validity and a "hidden" boolean property.
 * Drop-in replacement for com.day.cq.wcm.api.PageFilter
 * 
 * Assumes default 'hideInNav' opt-out property.
 *
 * @author David Steele
 */
public class HidePageFilter extends AndPageFilter {
	public static final String DEFAULT_HIDE_PROPERTY = "hideInNav";
	private boolean includeInvalid;
	private boolean includeHidden;
	private String property = DEFAULT_HIDE_PROPERTY;
	
	public static final BooleanEqualsFilter PAGE_NOT_HIDDEN_FILTER = new BooleanEqualsFilter(DEFAULT_HIDE_PROPERTY, Boolean.TRUE, Boolean.TRUE);
	public static final HidePageFilter HIDE_IN_NAVIGATION_FILTER = new HidePageFilter(DEFAULT_HIDE_PROPERTY);
	
	public HidePageFilter(boolean includeInvalid, boolean includeHidden, String property) {
		super(null);
		this.includeInvalid = includeInvalid;
		this.includeHidden = includeHidden;
		this.property = property;
		
		if (!this.includeInvalid) {
			addFilter(InvalidPageFilter.INVALID_PAGE_FILTER);
		}
		if (!this.includeHidden) {
			if (StringUtils.isBlank(property)) {
				addFilter(PAGE_NOT_HIDDEN_FILTER);
			} else {
				addFilter(new BooleanEqualsFilter(this.property, Boolean.TRUE, Boolean.TRUE));
			}
		}
	}

	public HidePageFilter(String property) {
		this(false, false, property);
	}
}
