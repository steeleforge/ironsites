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
package com.steeleforge.aem.ironsites.page.taglib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.WCMMode;

/**
 * Provides JSP variable availability to WCMMode object & state map.
 *
 * @author David Steele
 */
public class ModeHelperTEI extends TagExtraInfo {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ModeHelperTag.class);
	private static final String ATTR_MODEMAP_NAME = "mapName";
	private static final String ATTR_MODEOBJECT_NAME = "wcmmodeName";
	private static final String MODEMAP_CLASS = Map.class.getName();
	private static final String MODEOBJECT_CLASS = WCMMode.REQUEST_ATTRIBUTE_NAME;
	
	public VariableInfo[] getVariableInfo(TagData data) {
		List<VariableInfo> variableInfos = new ArrayList<VariableInfo>();
		variableInfos.addAll(Arrays.asList(super.getVariableInfo(data)));
		
		variableInfos.add(createVariableInfo(data, ATTR_MODEMAP_NAME, ModeHelperTag.DEFAULT_MODEMAP_NAME, MODEMAP_CLASS));
		variableInfos.add(createVariableInfo(data, ATTR_MODEOBJECT_NAME, ModeHelperTag.DEFAULT_MODEOBJECT_NAME, MODEOBJECT_CLASS));
		
		return ((VariableInfo[])variableInfos.toArray(new VariableInfo[variableInfos.size()]));
	}
	
	private VariableInfo createVariableInfo(TagData data, String attrName, String defaultName, String clazz) {
		String varName = data.getAttributeString(attrName);
		if (null == varName) {
			varName = defaultName;
		}
		return new VariableInfo(varName, clazz, true, VariableInfo.AT_END);
	}
}
