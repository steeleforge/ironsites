package com.steeleforge.aem.ironsites.wcm.servlet;

import java.io.IOException;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API Service supported widths
 * 
 * @author david
 */
@Service(value = AdaptiveImageConfig.class)
@Component(label = "ironsites - Adaptive Image Configurations",
    description = "Adaptive Image width support configurations",
    immediate = true)
public class AdaptiveImageConfig {
    // statics
    private static final Logger LOG = LoggerFactory.getLogger(AdaptiveImageConfig.class);
    private static final String ADAPTIVEIMAGE_SERVLET = "com.day.cq.wcm.foundation.impl.AdaptiveImageComponentServlet";
    
    // services
    @Reference 
    private ConfigurationAdmin configAdmin;

    // locals
    private List<Integer> widths = null;
    
    // properties
    @Property(label = "Additional Supported Widths",
            description = "In addition Adobe CQ Adaptive Image Component Servlet dimensions",
            value = {
            "320", // iPhone 1 portrait
            "420",
            "480", // iPhone 1 landscape
            "620", // iPad 1 landscape
            "640",
            "768",
            "800",
            "992",
            "1024",
            "1200",
            "1280",
            "1400",
            "1440",
            "1920" })
    private static final String PN_SUPPORTED_WIDTHS = "adapt.supported.widths";

    @Activate
    protected void activate(ComponentContext componentContext) {
        Configuration config = null;
        Dictionary<?, ?> parentProperties = null;
        Dictionary<?, ?> properties = componentContext.getProperties();
        // acquire parent servlet properties
        try {
            config = (Configuration) configAdmin.getConfiguration(ADAPTIVEIMAGE_SERVLET);
            parentProperties = config.getProperties();
        } catch (IOException ioe) {
            LOG.debug("Cannot retrieve AdaptiveImageComponentServlet configuration", ioe.getMessage());
        }

        // acquire parent servlet widths
        String[] parentWidths = null;
        if (null != parentProperties) {
            parentWidths = PropertiesUtil.toStringArray(parentProperties.get(PN_SUPPORTED_WIDTHS));
            if (ArrayUtils.getLength(parentWidths) > 0) {
                for (String width : parentWidths) {
                    getWidths().add(Integer.parseInt(width));
                }
            }
        }

        // acquire local servlet widths
        String[] localWidths = PropertiesUtil.toStringArray(properties.get(PN_SUPPORTED_WIDTHS));
        if (ArrayUtils.getLength(localWidths) > 0) {
            for (String width : localWidths) {
                getWidths().add(Integer.parseInt(width));
            }
        }
    }

    /**
     * @return lazily invoke/return set of configurable widths
     */
    public List<Integer> getWidths() {
        if (null == widths) {
            widths = new LinkedList<Integer>();
        }
        return widths;
    }
}
