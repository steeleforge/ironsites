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
package com.steeleforge.aem.ironsites.i18n.sightly;

import java.util.ResourceBundle;

import javax.script.Bindings;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.scripting.api.BindingsValuesProvider;
import org.osgi.framework.Constants;

import com.adobe.cq.sightly.WCMBindings;
import com.steeleforge.aem.ironsites.i18n.I18nResourceBundle;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;

/**
 * Binding Value Provider to replace sightly context properties with i18n fallbacks.
 */
@Service
@Component(label = "ironsites - i18n Sightly Bindings Provider",
    immediate = true)
@Properties({
    @Property(name = "javax.script.name", value = {"sightly","ironsites"}),
    @Property(name = Constants.SERVICE_RANKING, intValue = Integer.MAX_VALUE)
})
public class I18nBindingValueProvider implements BindingsValuesProvider {
    public static final String BINDING_REQUEST = "request";
    
	public void addBindings(Bindings bindings) {
        SlingHttpServletRequest request = (SlingHttpServletRequest)bindings.get(BINDING_REQUEST);
        if (null != request && null != bindings) {
            // get valuemap from bindings, or fall back on Resource#adaptTo(ValueMap)
            ValueMap properties = (bindings.containsKey(WCMBindings.PROPERTIES))?
                    (ValueMap)bindings.get(WCMBindings.PROPERTIES) : 
                    request.getResource().adaptTo(ValueMap.class);
            // generate sling:basename qualified i18n ResourceBundle
            ResourceBundle baseBundle = request.getResourceBundle(
                    request.getResource().getResourceType(), 
                    WCMUtil.getLocale(request));
            I18nResourceBundle i18n = new I18nResourceBundle(properties, baseBundle);
            // apply to bindings for sightly availability
            bindings.put(WCMBindings.PROPERTIES, i18n.adaptTo(ValueMap.class));
            
            // cleanup
            properties = null;
            baseBundle = null;
            i18n = null;
        }
    }
}
