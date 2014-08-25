package com.steeleforge.aem.ironsites.i18n;

import com.steeleforge.aem.ironsites.i18n.I18nResourceBundle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.commons.osgi.ServiceUtil;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.osgi.service.component.ComponentContext;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.LanguageManager;
import com.google.gson.Gson;

@SlingServlet(label = "ironsites - I18n Resource JSON Servlet",
    description = "JSON serialization of sling:MessageEntry based on sling:esourceType",
    name = "com.steeleforge.aem.ironsites.i18n.I18nResourceJsonServlet",
    resourceTypes = { "sling/servlet/default" },
    metatype = true, extensions = { "json" }, selectors = { "i18n" })
@References({
    @Reference(cardinality = ReferenceCardinality.MANDATORY_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            referenceInterface = ResourceBundleProvider.class,
            bind = "bindResourceBundleProviders", 
            unbind = "unbindResourceBundleProviders")
})
public class I18nResourceJsonServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = -8837830903603951318L;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(I18nResourceJsonServlet.class);

    private Gson gson = null;
    private ComponentContext componentContext = null;
    private final Map<Object, ResourceBundleProvider> providers = new HashMap<Object, ResourceBundleProvider>();
    static final boolean DEFAULT_ENABLED = true;

    @Reference
    LanguageManager languageManager;

    @Property(label = "Enable", 
            description = "Enable JSON serialization of sling:MessageEntry resolution by resource type",
            boolValue = DEFAULT_ENABLED)
    static final String PROPERTY_ENABLED = "enabled";
    protected Boolean enabled;

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        Resource resource = request.getResource();
        if (enabled && null != resource) {
            ResourceBundle bundle = null;
            ResourceBundleProvider provider = null;
            Iterator<Object> keys = providers.keySet().iterator();
            while(keys.hasNext() && (null == bundle)) {
                provider = providers.get(keys.next());
                bundle = provider.getResourceBundle(resource.getResourceType(), 
                        languageManager.getLanguage(resource));
            }
            if (null == bundle) {
                bundle = request.getResourceBundle(resource.getResourceType(), 
                    languageManager.getLanguage(resource));
            }
            I18nResourceBundle i18nBundle = new I18nResourceBundle(null, bundle);

            // utilizing I18nResourceBundle wrapper for locale-specific bundle
            // this is for adaptTo(ValueMap) for serialization.
            // Gson#toJson against ResourceBundle causes stackoverflow.
            String json = gson.toJson(i18nBundle.adaptTo(ValueMap.class));

            byte[] jsonBytes = json.getBytes("UTF-8");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setContentLength(jsonBytes.length);
            response.getOutputStream().write(jsonBytes);
        } else {
            response.sendError(SlingHttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Activate
    protected void activate(ComponentContext context) {
        this.componentContext = context;
        this.gson = new Gson();
        this.enabled = PropertiesUtil.toBoolean(context.getProperties()
                .get(PROPERTY_ENABLED), DEFAULT_ENABLED);
    }
    
    protected void bindResourceBundleProviders(ResourceBundleProvider provider, Map<String, Object> props) {
        synchronized(this.providers) {
            this.providers.put(ServiceUtil.getComparableForServiceRanking(props), provider);
        }
    }

    protected void unbindResourceBundleProviders(ResourceBundleProvider provider, Map<String, Object> props) {
        synchronized(this.providers) {
            this.providers.remove(ServiceUtil.getComparableForServiceRanking(props));
        }
    }
}
