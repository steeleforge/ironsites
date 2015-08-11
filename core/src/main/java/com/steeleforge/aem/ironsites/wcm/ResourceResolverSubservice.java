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
package com.steeleforge.aem.ironsites.wcm;

import java.util.Collections;
import java.util.HashMap;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResourceResolverSubservice {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceResolverSubservice.class);
    
    protected ResourceResolver resourceResolver;
    
    /**
     * @return UserServiceMapper identity
     */
    public abstract String getServiceMapperIdentity();
    
    /**
     * Closes resource resolver
     */
    public void closeResolver() {
        if (null != this.resourceResolver && this.resourceResolver.isLive()) {
            this.resourceResolver.close();
        }
    }
    
    /**
     * Acquires JCR administrative session from a resource resolver
     * 
     * @return JCR Session
     * @throws RepositoryException 
     */
    public Session getSession(final ResourceResolverFactory resourceResolverFactory) {
        Session session = getResourceResolver(resourceResolverFactory).adaptTo(Session.class);
        if (null != session) {
            try {
                session.refresh(true);
            } catch (RepositoryException e) {
                LOG.trace("Could not refresh JCR session: ", e.getMessage());
            }
        }
        return session;
    }
    
    /**
     * @return ResourceResolver from factory
     */
    public ResourceResolver getResourceResolver(final ResourceResolverFactory resourceResolverFactory) {
        if (null == resourceResolverFactory || StringUtils.isBlank(getServiceMapperIdentity())) {
            return this.resourceResolver;
        }
        if (null == this.resourceResolver || !this.resourceResolver.isLive()) {
            try {
                this.resourceResolver = resourceResolverFactory.getServiceResourceResolver(Collections.unmodifiableMap(
                        new HashMap<String,Object>() {
                            private static final long serialVersionUID = 1L;
                            { put(ResourceResolverFactory.SUBSERVICE, getServiceMapperIdentity() ); }
                        }));
            } catch (LoginException e) {
                LOG.debug("Unable to retrieve resource resolver for {}:", getServiceMapperIdentity(), e.getMessage());
            }
        }
        return this.resourceResolver;
    }

}
