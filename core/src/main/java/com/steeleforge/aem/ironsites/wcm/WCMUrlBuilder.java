package com.steeleforge.aem.ironsites.wcm;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

public class WCMUrlBuilder {
    private SlingHttpServletRequest request;
    private String url = StringUtils.EMPTY;
    private String path;
    private String selectors;
    private String extension;
    private String suffix;
    private String domain;
    private String protocol;
    private String fragment;
    private String query;
    private boolean extensionSuffix = true;
    private boolean jcrMangle = true;
    private boolean resolverMap = true;
    private boolean sanitize = false;
    private boolean protocolRelative = false;
    
    public WCMUrlBuilder(SlingHttpServletRequest request) {
        super();
        this.request = request;
    }
    
    public String toString() {
        return getUrl();
    }
    
    /**
     * @return the url
     */
    public String getUrl() {
        if (isExternal()) {
            this.url = WCMUtil.getExternalURL(this.request, this.path, this.protocol, this.domain);
            if (isProtocolRelative()) {
                this.url = WCMUtil.getProtocolRelativeURL(this.url);
            }
        } else {
            this.url = WCMUtil.getRelativeURL(this.request, 
                    this.path, 
                    this.selectors, 
                    this.extension, 
                    this.suffix, 
                    this.extensionSuffix, 
                    this.jcrMangle, 
                    this.resolverMap, 
                    this.sanitize);
        }
        if (StringUtils.isNotBlank(this.fragment)) {
            if (!StringUtils.startsWith(this.fragment, WCMConstants.DELIMITER_FRAGMENT)) {
                this.url += WCMConstants.DELIMITER_FRAGMENT;
            }
            this.url += fragment;
        }

        if (StringUtils.isNotBlank(this.query)) {
            this.url += WCMConstants.DELIMITER_QUERY + WCMUtil.getURLEncoded(query);
        }
        return this.url;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public WCMUrlBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    /**
     * @return the selectors
     */
    public String getSelectors() {
        return selectors;
    }

    /**
     * @param selectors the selectors to set
     */
    public WCMUrlBuilder setSelectors(String selectors) {
        this.selectors = selectors;
        return this;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public WCMUrlBuilder setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public WCMUrlBuilder setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public WCMUrlBuilder setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the fragment
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * @param fragment the fragment to set
     */
    public WCMUrlBuilder setFragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    /**
     * @return the query string
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query the querystring to set
     */
    public WCMUrlBuilder setQuery(String query) {
        this.query = query;
        return this;
    }

    /**
     * @return the extensionSuffix
     */
    public boolean isExtensionSuffix() {
        return extensionSuffix;
    }

    /**
     * @param extensionSuffix the extensionSuffix to set
     */
    public WCMUrlBuilder setExtensionSuffix(boolean extensionSuffix) {
        this.extensionSuffix = extensionSuffix;
        return this;
    }

    /**
     * @param extensionSuffix the extensionSuffix to set
     */
    public WCMUrlBuilder isExtensionSuffix(boolean extensionSuffix) {
        return setExtensionSuffix(extensionSuffix);
    }

    /**
     * @return the jcrMangle
     */
    public boolean isJcrMangle() {
        return jcrMangle;
    }

    /**
     * @param jcrMangle the jcrMangle to set
     */
    public WCMUrlBuilder setJcrMangle(boolean jcrMangle) {
        this.jcrMangle = jcrMangle;
        return this;
    }
    
    /**
     * @param jcrMangle the jcrMangle to set
     */
    public WCMUrlBuilder isJcrMangle(boolean jcrMangle) {
        return setJcrMangle(jcrMangle);
    }

    /**
     * @return the resolverMap
     */
    public boolean isResolverMap() {
        return resolverMap;
    }

    /**
     * @param resolverMap the resolverMap to set
     */
    public WCMUrlBuilder setResolverMap(boolean resolverMap) {
        this.resolverMap = resolverMap;
        return this;
    }

    /**
     * @param resolverMap the resolverMap to set
     */
    public WCMUrlBuilder isResolverMap(boolean resolverMap) {
        return setResolverMap(resolverMap);
    }


    /**
     * @return the sanitize
     */
    public boolean isSanitize() {
        return sanitize;
    }

    /**
     * @param sanitize the sanitize to set
     */
    public WCMUrlBuilder setSanitize(boolean sanitize) {
        this.sanitize = sanitize;
        return this;
    }

    /**
     * @param sanitize the sanitize to set
     */
    public WCMUrlBuilder isSanitize(boolean sanitize) {
        return setSanitize(sanitize);
    }
    
    /**
     * @return the external
     */
    public boolean isExternal() {
        return StringUtils.isNotBlank(domain);
    }

    /**
     * @param domain the externalizer domain to set
     */
    public WCMUrlBuilder isExternal(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * @return the protocolRelative
     */
    public boolean isProtocolRelative() {
        return protocolRelative;
    }

    /**
     * @param protocolRelative the protocolRelative to set
     */
    public WCMUrlBuilder setProtocolRelative(boolean protocolRelative) {
        this.protocolRelative = protocolRelative;
        return this;
    }

    /**
     * @param protocolRelative the protocolRelative to set
     */
    public WCMUrlBuilder isProtocolRelative(boolean protocolRelative) {
        return isProtocolRelative(protocolRelative);
    }
}
