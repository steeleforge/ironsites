<%--
    This is free and unencumbered software released into the public domain.
    
    Anyone is free to copy, modify, publish, use, compile, sell, or
    distribute this software, either in source code form or as a compiled
    binary, for any purpose, commercial or non-commercial, and by any
    means.
    
    In jurisdictions that recognize copyright laws, the author or authors
    of this software dedicate any and all copyright interest in the
    software to the public domain. We make this dedication for the benefit
    of the public at large and to the detriment of our heirs and
    successors. We intend this dedication to be an overt act of
    relinquishment in perpetuity of all present and future rights to this
    software under copyright law.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
    MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
    OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
    ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
    OTHER DEALINGS IN THE SOFTWARE.
    
    For more information, please refer to <http://unlicense.org/>
    ***************************************************************************
    
    ironsites Kitchen Sink
    
--%><%@page import="com.steeleforge.aem.ironsites.cache.service.SimpleCacheService,
                    com.google.common.cache.Cache,
                    org.apache.commons.lang.StringUtils,
                    com.steeleforge.aem.ironsites.i18n.I18nUtil,
                    com.steeleforge.aem.ironsites.wcm.page.filter.HidePageFilter,
                    com.steeleforge.aem.ironsites.wcm.page.filter.ShowPageFilter"%><%
%><%@include file="/apps/ironsites/common/global.jsp"%>
<isi:i18nHelper messagesName="m" i18nName="i"/>
<isw:modeHelper mapName="mm" wcmmodeName="mo"/><%
%><c:if test="${mm.EDIT}"><%
    SimpleCacheService scs = sling.getService(SimpleCacheService.class);
    Cache c = scs.getCache("kitchensink");
    c.put("basic", m.get("basictext", ""));
    c.put("token", m.get("tokentext", ""));
    c.put("rich", m.get("richtext", ""));
    
    Page pg = pageManager.getPage(properties.get("path", currentPage.getPath()));
%>
<p>
    <h3>Setup</h3>
    <b>Basic Text:</b> <%=c.getIfPresent("basic") %>
    <br/>
    <b>Token Text:</b> <%=c.getIfPresent("token") %>
    <br/>
    <b>Rich Text:</b> <%=c.getIfPresent("rich") %>
    <br/>
    <b>Path:</b> <%=properties.get("path") %>
</p>

<p>
    <h3>i18n</h3>
    <b>Interpolated Token Text:</b> 
    <%=I18nUtil.interpolate(m.get("tokentext",""), 
        "{name}={0};{city}={1}", "David", "Detroit, MI")%>
    <!-- ${isi:swap(m.tokentext, '{name}={0};{city}={1}')} -->
    <!-- ${isi:swap(m.tokentext, '{name}=#{name};{city}=#{city}')} -->
</p>

<p>
    <h3>XSS</h3>
    <b>Filter Rich Text:</b> 
    <isx:filterHTML policy="${properties['xssPolicy']}">${m.richtext}</isx:filterHTML>
<br/>
    <b>Escaped Rich Text:</b> 
    <isx:filterHTML escape="true" policy="${properties['xssPolicy']}">${m.richtext}</isx:filterHTML>
    <br/>
    <b>Basic Text - encodeForHTML:</b> ${isx:encodeForHTML(m.basictext, pageContext)}
    <br/>
    <b>Basic Text - encodeForHTMLAttr:</b> ${isx:encodeForHTMLAttr(m.basictext, pageContext)} 
    <br/>
    <b>Basic Text - encodeForJSString:</b> ${isx:encodeForJSString(m.basictext, pageContext)}
    <br/>
    <b>Basic Text - encodeForXML:</b> ${isx:encodeForXML(m.basictext, pageContext)}
    <br/>
    <b>Basic Text - encodeForXMLAttr:</b> ${isx:encodeForXMLAttr(m.basictext, pageContext)}
    <br/>
    <b>Basic Text - filterHTML:</b> ${isx:filterHTML(properties.xssPolicy, m.basictext, pageContext)}
    <br/>
    <b>Basic Text - getValidHref:</b> ${isx:getValidHref(m.basictext, pageContext)} 
    <br/>
    <b>Basic Text - getValidInteger:</b> ${isx:getValidInteger(m.basictext, -1, pageContext)} (Invalid)
    <br/>
    <b>Basic Text - getValidJSToken:</b> ${isx:getValidJSToken(m.basictext, "(Invalid)", pageContext)}   
</p>

<p>
    <h3>WCMMode</h3>
    <b>Mode Map:</b> ${mm}
    <br/>
    <b>Mode Object:</b> ${mo}
    <br/>
    <isw:wcmmode mode="disabled">
        <b>Set Mode: Disabled</b><isw:modeHelper wcmmodeName="md"/> ${md}
    </isw:wcmmode>
    <br/>
    <isw:wcmmode mode="preview">
        <b>Set Mode: Preview</b><isw:modeHelper wcmmodeName="mp"/> ${mp}
    </isw:wcmmode>
    <br/>
    <isw:wcmmode mode="read_only">
        <b>Set Mode: Readonly</b><isw:modeHelper wcmmodeName="mro"/> ${mro}
    </isw:wcmmode>
    <br/>
</p>

<p>
    <h3>Filters</h3>
    <h5>Include <%=pg.getPath()%> in filter?</h5>
    <b>HidePageFilter "hideInNav":</b>
    <%=(null != page)? HidePageFilter.HIDE_IN_NAVIGATION_FILTER.includes(pg):""%> 
    <br/>
    <b>ShowPageFilter "showInNav":</b>
    <%=(null != page)? ShowPageFilter.SHOW_IN_NAVIGATION_FILTER.includes(pg):""%> 
</p>

<h3>Cache Monitor</h3>
<cq:include path="cm" resourceType="ironsites/components/cachemonitor" />
</c:if>