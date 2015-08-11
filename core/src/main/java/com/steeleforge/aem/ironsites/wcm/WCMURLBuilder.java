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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

public class WCMURLBuilder {
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
    
    public WCMURLBuilder(final SlingHttpServletRequest request) {
        super();
        this.request = request;
    }
    
    public String toString() {
        return getUrl();
    }
    
    public String getUrl() {
        if (StringUtils.isBlank(this.url)) {
            build();
        }
        return this.url;
    }
    
    /**
     * @return the url
     */
    public String build() {
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
    public WCMURLBuilder setPath(String path) {
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
    public WCMURLBuilder setSelectors(String selectors) {
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
    public WCMURLBuilder setExtension(String extension) {
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
    public WCMURLBuilder setSuffix(String suffix) {
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
    public WCMURLBuilder setDomain(String domain) {
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
    public WCMURLBuilder setFragment(String fragment) {
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
    public WCMURLBuilder setQuery(String query) {
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
    public WCMURLBuilder setExtensionSuffix(boolean extensionSuffix) {
        this.extensionSuffix = extensionSuffix;
        return this;
    }

    /**
     * @param extensionSuffix the extensionSuffix to set
     */
    public WCMURLBuilder isExtensionSuffix(boolean extensionSuffix) {
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
    public WCMURLBuilder setJcrMangle(boolean jcrMangle) {
        this.jcrMangle = jcrMangle;
        return this;
    }
    
    /**
     * @param jcrMangle the jcrMangle to set
     */
    public WCMURLBuilder isJcrMangle(boolean jcrMangle) {
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
    public WCMURLBuilder setResolverMap(boolean resolverMap) {
        this.resolverMap = resolverMap;
        return this;
    }

    /**
     * @param resolverMap the resolverMap to set
     */
    public WCMURLBuilder isResolverMap(boolean resolverMap) {
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
    public WCMURLBuilder setSanitize(boolean sanitize) {
        this.sanitize = sanitize;
        return this;
    }

    /**
     * @param sanitize the sanitize to set
     */
    public WCMURLBuilder isSanitize(boolean sanitize) {
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
    public WCMURLBuilder isExternal(String domain) {
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
    public WCMURLBuilder setProtocolRelative(boolean protocolRelative) {
        this.protocolRelative = protocolRelative;
        return this;
    }

    /**
     * @param protocolRelative the protocolRelative to set
     */
    public WCMURLBuilder isProtocolRelative(boolean protocolRelative) {
        return isProtocolRelative(protocolRelative);
    }
}
