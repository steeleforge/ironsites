package com.steeleforge.aem.ironsites.service;

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
