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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.commons.jcr.JcrConstants;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;

public class InheritUse extends WCMUsePojo {
	// statics
	public static final String PN_RELPATH = "path";
	
	// locals
	private String relPath;
	private String localePath;
	private String path;
	
    public InheritUse() {
        super();        
    }

    @Override    
    public void activate() throws Exception {
        localePath = WCMUtil.getLanguageRoot(getCurrentPage().getPath());
        relPath = get(PN_RELPATH, String.class);
        if (StringUtils.isBlank(relPath)) relPath = 
        		StringUtils.substringAfterLast(getResource().getPath(), JcrConstants.JCR_CONTENT);
        final ResourceResolver resolver = getResourceResolver();
        if (null != resolver.resolve(localePath + relPath)) {
        	path = localePath + relPath;
        } else {
        	path = relPath;
        }
    }

	public String getPath() {
		return path;
	}
}