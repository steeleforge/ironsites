package com.steeleforge.aem.ironsites.wcm.link;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.day.cq.wcm.api.Page;
import com.steeleforge.aem.ironsites.wcm.WCMUtil;

/**
 * @link https://developer.mozilla.org/en-US/docs/Web/HTML/Element/a
 */
public class Link {
    // statics
    public static final String PN_ID = "id";
    public static final String PN_HREF = "href";
    public static final String PN_NAME = "name";
    public static final String PN_STYLES = "styles";
    public static final String PN_TARGET = "target";
    public static final String PN_REL = "rel";
    public static final String PN_DOWNLOAD = "download";
    public static final String PN_MEDIA = "media";
    public static final String PN_HREFLANG = "hreflang";
    public static final String PN_TYPE = "type";
    public static final String PN_PING = "ping";
    public static final String PN_TEXT = "text";
    
    private String id = StringUtils.EMPTY;
    private String href = StringUtils.EMPTY;
    private String name = StringUtils.EMPTY;
    private String styles = StringUtils.EMPTY;
    private String target = StringUtils.EMPTY;
    private String rel = StringUtils.EMPTY;
    private String download = StringUtils.EMPTY;
    private String media = StringUtils.EMPTY;
    private String hreflang = StringUtils.EMPTY;
    private String type = StringUtils.EMPTY;
    private boolean ping = false;
    private String text = StringUtils.EMPTY;
    
    private Map<String, String> dataAttributes = new HashMap<String, String>();
    
    public Link() {
        super();
    }
    
    public Link(final Page page) {
        if (null != page) {
            setText(WCMUtil.getPageTitle(page));
            setHreflang(page.getLanguage(false).toString());
        }
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }
    
    /**
     * @param href the href to set
     */
    public void setHref(String href) {
        this.href = href;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the styles
     */
    public String getStyles() {
        return styles;
    }
    
    /**
     * @param styles the styles to set
     */
    public void setStyles(String styles) {
        this.styles = styles;
    }
    
    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }
    
    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }
    
    /**
     * @return the rel
     */
    public String getRel() {
        return rel;
    }
    
    /**
     * @param rel the rel to set
     */
    public void setRel(String rel) {
        this.rel = rel;
    }
    
    /**
     * @return the download
     */
    public String getDownload() {
        return download;
    }
    
    /**
     * @param download the download to set
     */
    public void setDownload(String download) {
        this.download = download;
    }
    
    /**
     * @return the media
     */
    public String getMedia() {
        return media;
    }
    
    /**
     * @param media the media to set
     */
    public void setMedia(String media) {
        this.media = media;
    }
    
    /**
     * @return the hreflang
     */
    public String getHreflang() {
        return hreflang;
    }
    
    /**
     * @param hreflang the hreflang to set
     */
    public void setHreflang(String hreflang) {
        this.hreflang = hreflang;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @return the ping
     */
    public boolean isPing() {
        return ping;
    }
    
    /**
     * @param ping the ping to set
     */
    public void setPing(final boolean ping) {
        this.ping = ping;
    }
    
    /**
     * @param ping the ping to set
     */
    public void setIsPing(final boolean ping) {
        setPing(ping);
    }
    
    /**
     * @return the dataAttributes
     */
    public Map<String, String> getDataAttributes() {
        return dataAttributes;
    }
    
    /**
     * @param dataAttribute
     * @return
     */
    public String getDataAttribute(final String dataAttribute) {
        return getDataAttributes().get(dataAttribute);
    }
    
    /**
     * @param dataAttributes the dataAttributes to set
     */
    public void putDataAttribute(final String dataAttribute, final String dataAttributeValue) {
        getDataAttributes().put(dataAttribute, dataAttributeValue);
    }
    
    /**
     * @param dataAttribute
     * @return the dataAttributeValue
     */
    public String removeDataAttribute(final String dataAttribute) {
        return getDataAttributes().remove(dataAttribute);
    }
    
    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
}