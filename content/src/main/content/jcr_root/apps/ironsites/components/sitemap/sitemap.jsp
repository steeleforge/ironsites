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
    
    ironsites Sitemap variation of General Sitemap
    
--%><%@page import="com.steeleforge.aem.ironsites.wcm.page.IronSitemap,
                    com.steeleforge.aem.ironsites.wcm.page.filter.HidePageFilter,
                    com.day.cq.wcm.api.Page,
                    com.day.cq.wcm.api.PageManager"%><%
%><%@include file="/apps/ironsites/common/global.jsp"%><%
%><%@include file="init.jsp"%><%
%><c:if test="${not empty sitemap}">
    <c:set var="level" value="0"/>
    <ul class="level-${link.level}">
    <c:forEach var="link" items="${sitemap.links}">
        <c:choose>
            <c:when test="${link.level gt level}">
    <ul class="level-${link.level}">
            </c:when>
        </c:choose>
        <li><a href="${link.path}" target="${properties['target']}">${link.title}</a></li>
        <c:set var="level" value="${link.level}"/>
    </c:forEach>
    <c:forEach var="close" begin="0" end="${level}">
    </ul><!-- /level-${level - close} -->
    </c:forEach>
</c:if>