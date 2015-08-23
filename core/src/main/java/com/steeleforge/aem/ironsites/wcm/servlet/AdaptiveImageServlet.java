package com.steeleforge.aem.ironsites.wcm.servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.RepositoryException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.xss.XSSAPI;
import com.day.cq.wcm.commons.AbstractImageServlet;
import com.day.cq.wcm.foundation.AdaptiveImageHelper;
import com.day.cq.wcm.foundation.Image;
import com.day.image.Layer;
import com.steeleforge.aem.ironsites.wcm.components.IronImage;

@Service
@SlingServlet(label = "ironsites - Adaptive Image Servlet",
    description = "Configurable constraints to resized images.",
    resourceTypes = { "sling/servlet/default", "foundation/components/image", "wcm/foundation/components/image" },
    selectors = { "img" },
    methods = { "GET" },
    extensions = { "jpg", "jpeg", "png", "gif" })
public class AdaptiveImageServlet extends AbstractImageServlet {
    // TODO FIX DIALOG RESTYPE
    // statics
    private static final Logger LOG = LoggerFactory.getLogger(AdaptiveImageServlet.class);
    private static final long serialVersionUID = 1378224055887559553L;
    private static final String SELECTOR_FULL = "full";
    private static final String DEFAULT_MIME_TYPE = "image/jpeg";
    private static final int SCALAR_HEIGHT = 0;
    
    // services
    @Reference
    private XSSAPI xssapi;
    @Reference
    private AdaptiveImageConfig config;
    
    // locals
    private Image image = null;
    
    public AdaptiveImageServlet() {
        super();
    }

    @Activate
    protected void activate(ComponentContext componentContext) {
        // NO OP
    }

    @Override
    protected Layer createLayer(ImageContext imageContext) throws RepositoryException, IOException {
        SlingHttpServletRequest request = imageContext.request;
        image = new IronImage(imageContext.resource);
        final String selectors[] = request.getRequestPathInfo().getSelectors();
        
        if (!image.hasContent()) {
            // no file reference or child image
            // empty layer, results in placeholder
            return null;
        }
        
        final AdaptiveImageHelper adaptiveHelper = new AdaptiveImageHelper();
        Layer styleLayer = adaptiveHelper.applyStyleDataToImage(image, imageContext.style);
        
        // minimum 2 selectors to qualify resizing
        if (ArrayUtils.getLength(selectors) < 2) {
            LOG.debug("Dimension and quality not provided");
            return styleLayer;
        }

        // selectors 0=img 1=width 2=quality
        String widthRequest = SELECTOR_FULL;
        if (ArrayUtils.getLength(selectors) >= 2) {
            widthRequest = selectors[1];
        }
        // ensure this is one of our supported widths
        if (false == isDimensionSupported(widthRequest)) {
            LOG.debug("Invalid width width: {}.", widthRequest);
            return styleLayer;
        } 

        // no need to resize because requested width is greater than image width
        if (SELECTOR_FULL.equals(widthRequest)) {
            return styleLayer;
        } else if (Integer.parseInt(widthRequest) >= styleLayer.getWidth()) {
            return styleLayer;
        }

        // attempt to resize
        return adaptiveHelper.scaleThisImage(image, 
                Integer.parseInt(widthRequest), 
                SCALAR_HEIGHT, 
                imageContext.style);
    }

    /**
     * @param widthRequest
     * @return true if width dimension request is supported, false otherwise
     */
    protected boolean isDimensionSupported(final String widthRequest) {
        // return immediately for quality dimension
        if (StringUtils.equals(SELECTOR_FULL, widthRequest)) {
            return true;
        }
        String dimension = xssapi.getValidDimension(widthRequest, SELECTOR_FULL);
        final Iterator<Integer> iterator = config.getWidths().iterator();
        if (SELECTOR_FULL.equals(dimension)) {
            return true;
        }
        // prevents parsing quality dimension
        int width = Integer.parseInt(widthRequest);
        while (iterator.hasNext()) {
            if (width == (iterator.next())) {
                // requested width is a supported configuration
                return true;
            }
        }

        // requested width is not supported
        return false;
    }

    @Override
    protected void writeLayer(SlingHttpServletRequest request, SlingHttpServletResponse response, 
            ImageContext context, Layer layer) 
                    throws IOException, RepositoryException {
        double quality = -1;
        // determine if quality selector is requested, attempt to use
        final String selectors[] = request.getRequestPathInfo().getSelectors();
        if (3 == ArrayUtils.getLength(selectors)) {
            String qualityRequest = selectors[2];
            quality = getRequestedImageQuality(qualityRequest);
        } else {
            // get default quality 
            quality = getImageQuality();
        }

        writeLayer(request, response, context, layer, quality);
    }

    /**
     * @param qualityRequest
     * @return return image quality value
     */
    private double getRequestedImageQuality(final String qualityRequest) {
        // If imageQualitySelector is not a valid Quality, fall back to the default
        final AdaptiveImageHelper.Quality quality = AdaptiveImageHelper.getQualityFromString(qualityRequest);
        if (null != quality) {
            // return valid quality
            return quality.getQualityValue();
        }
        // return default
        return getImageQuality();
    }

    @Override
    protected String getImageType() {
        if (null != image) {
            try {
                return image.getMimeType();
            } catch (RepositoryException re) {
                LOG.debug("Unable to retrieve image resource", re.getMessage());
            }
        }
        return DEFAULT_MIME_TYPE;
    }
}