package com.steeleforge.aem.ironsites.wcm.servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.OptingServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@SlingServlet(label = "ironsites - Tag Options Servlet",
    description = "Given a tag path, provides text/value domain values for widgets",
    paths = { "/bin/options/tag" },
    extensions = { "json" },
    metatype = true)
public class TagOptionsServlet extends SlingSafeMethodsServlet implements OptingServlet {
    // globals
    private static final long serialVersionUID = -1094507554833230130L;
    private static final Logger LOG = LoggerFactory.getLogger(TagOptionsServlet.class);    
    // statics
    private static final boolean DEFAULT_ENABLED = true;
    private static final String PN_PATH = "path";
    private static final String PN_KEY = "text";
    private static final String PN_VALUE = "cssName";

    @Property(label = "Enable", 
            description = "Enable Servlet",
            boolValue = DEFAULT_ENABLED)
    private static final String PROPERTY_ENABLED = "enabled";
    private Boolean enabled;
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String root = request.getParameter(PN_PATH);
        if (StringUtils.isNotBlank(root)) {
            TagManager tagManager = request.getResourceResolver().adaptTo(TagManager.class);
            Tag rootTag = tagManager.resolve(root);
            // cannot resolve root tag
            if (null == rootTag) {
                response.sendError(SlingHttpServletResponse.SC_NOT_FOUND);
            } else {
                JSONArray items = new JSONArray();
                Iterator<Tag> children = rootTag.listChildren();
                Tag child = null;
                // iterate over child tags
                while(children.hasNext()) {
                    child = children.next();
                    try {
                        items.put((Object)new JSONObject()
                                .put(PN_KEY, child.getTitle())
                                .put(PN_VALUE, child.getDescription()));
                    } catch (JSONException e) {
                        LOG.debug(e.getMessage());
                    }
                    child = null;
                }
                
                byte[] jsonBytes = items.toString().getBytes("UTF-8");

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setContentLength(jsonBytes.length);
                response.getOutputStream().write(jsonBytes);
            }
        } else {
            response.sendError(SlingHttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    public boolean accepts(SlingHttpServletRequest request) {
        if (enabled && null != request.getParameter(PN_PATH)) {
            return true;
        }
        return false;
    }
    
    @Activate
    protected void activate(ComponentContext context) {
        this.enabled = PropertiesUtil.toBoolean(context.getProperties()
                .get(PROPERTY_ENABLED), DEFAULT_ENABLED);
    }
}