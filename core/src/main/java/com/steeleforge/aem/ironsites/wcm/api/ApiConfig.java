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
package com.steeleforge.aem.ironsites.wcm.api;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;

@Service(value = ApiConfig.class)
@Component(label = "ironsites - API Service Configuration",
    immediate = true,
    configurationFactory = true, 
    metatype = true, 
    policy = ConfigurationPolicy.REQUIRE)
public class ApiConfig {
    @SuppressWarnings("unused")
    private ComponentContext componentContext = null;
    
    @Property(label = "API Site Identifier", description = "e.g. www.steeleforge.com")
    static final String PN_SITE = "ironsites.api.site";
    protected String site;
    
    @Property(label = "API Service Identifier", description = "e.g. addthis")
    static final String PN_SERVICE = "ironsites.api.service";
    protected String service;
    
    @Property(label = "API URL Pattern", description = "e.g. https://cdn.akamai.net/?foo={0}&bar={1}")
    static final String PN_URL = "ironsites.api.url";
    protected String url;
    
    @Property(label = "API Keys", description = "API keys and customer identifiers per pattern order", value = {"",""})
    static final String PN_KEYS = "ironsites.api.keys";
    protected String[] keys;
        
    @Activate
    protected void activate(ComponentContext ctx) {
        this.componentContext = ctx;
        
        Dictionary<?,?> props = ctx.getProperties();
        this.site = PropertiesUtil.toString(props.get(PN_SITE), StringUtils.EMPTY);
        this.service = PropertiesUtil.toString(props.get(PN_SERVICE), StringUtils.EMPTY);
        this.url = PropertiesUtil.toString(props.get(PN_URL), StringUtils.EMPTY);
        this.keys = PropertiesUtil.toStringArray(props.get(PN_KEYS), new String[] {});
    }

    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
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

    /**
     * @return the keys
     */
    public String[] getKeys() {
        return keys;
    }

    /**
     * @param keys the keys to set
     */
    public void setKeys(String[] keys) {
        this.keys = keys;
    }
}
