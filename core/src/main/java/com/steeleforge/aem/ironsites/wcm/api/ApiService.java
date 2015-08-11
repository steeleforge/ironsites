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

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.steeleforge.aem.ironsites.wcm.ResourceResolverSubservice;

/**
 * API Factory
 * 
 * @author David Steele
 */
@Service(value = ApiService.class)
@Component(label = "ironsites - API Factory Service",
    description = "Acquire API URLs from this service.",
    immediate = true)
@References({
    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC, 
            referenceInterface = ApiConfig.class,
            name = "ApiConfig",
            bind = "bindConfigurations",
            unbind = "unbindConfigurations")
})
public class ApiService extends ResourceResolverSubservice {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ApiService.class);
    
    // statics
    public static final String IDENTITY_SERVICEMAPPER = "ApiService";

    // locals
    private ComponentContext componentContext;
    private Table<String, String, String> configs;
    
    @Activate
    protected void activate(ComponentContext ctx) {
        this.componentContext = ctx;
        this.configs = HashBasedTable.create();
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {
        this.componentContext = null;
    }

    protected void bindConfigurations(ServiceReference ref) {
        Object svc = this.componentContext.locateService("ApiConfig", ref);
        ApiConfig config;
        if (svc instanceof ApiConfig) {
            config = (ApiConfig)svc;
            if (StringUtils.isNotBlank(config.getSite()) && 
                    StringUtils.isNotBlank(config.getService()) && 
                    StringUtils.isNotBlank(config.getUrl())) {
                configs.put(config.getSite(), config.getService(), getApi(config));
            }
        }
    }

    protected void unbindConfigurations(ServiceReference ref) {
        configs.clear();
    }
    
    /**
     * Provides URL formatting with configured API tokens.
     * 
     * @param cfg
     * @return
     */
    private String getApi(final ApiConfig cfg) {
        if (ArrayUtils.isNotEmpty(cfg.getKeys())) {
            return MessageFormat.format(cfg.getUrl(), 
                    (Object[])cfg.getKeys());
        }
        return cfg.getUrl();
    }
    
    /**
     * Given a site and service identifier, return the configured API URL.
     * 
     * @param siteID
     * @param serviceID
     * @return API URL or empty if no matching site/service has a configured URL
     */
    public String getApi(final String siteID, final String serviceID) {
        // fail fast
        if (StringUtils.isBlank(siteID) || StringUtils.isBlank(serviceID)) return null;
        // lookup API value from table
        if (configs.contains(siteID, serviceID)) {
            return configs.get(siteID, serviceID);
        }
        return null;
    }
    
    /**
     * Returns a set of configured services by siteID
     * 
     * @param siteID
     * @return
     */
    public Set<String> getServices(final String siteID) {
        if (configs.containsRow(siteID)) {
            return configs.row(siteID).keySet();
        }
        return Collections.emptySet();
    }
    
    @Override
    public String getServiceMapperIdentity() {
        return IDENTITY_SERVICEMAPPER;
    }
}
