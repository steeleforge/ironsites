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
	
	ironsites Simple Cache Monitor
	
--%><%@page import="com.steeleforge.aem.ironsites.cache.service.SimpleCacheService,
                    com.google.common.cache.Cache,
                    com.google.common.cache.CacheStats"%><%
%><%@include file="/apps/ironsites/common/global.jsp"%><%
%><ism:modeHelper/><isi:i18nHelper/><%
%><c:if test="${ismode.EDIT}"><%
	SimpleCacheService cacheService = sling.getService(SimpleCacheService.class);
	Cache<Object, Object> cache;
	CacheStats stats;
	if (null != cacheService) {
		for (String name : cacheService.getCaches()) {
			cache = cacheService.getCache(name);
			stats = cacheService.getStats(name);
%>	<dl>
		<dt>${messages.name}</dt> <dd><h2><%=name%></h2></dd>
		<dt>${messages.size}</dt> <dd><%=cache.size()%></dd>
		<%	if (null == stats) {  %>
		<dt>${messages.stats}</dt> <dd>${i18n.statsDisabled}</dd>
		<%	} else { %>
		<dt>${messages.hitCount}</dt> <dd><%=stats.hitCount()%></dd>
		<dt>${messages.missCount}</dt> <dd><%=stats.missCount()%></dd>
		<dt>${messages.loadSuccessCount}</dt> <dd><%=stats.loadSuccessCount()%></dd>
		<dt>${messages.loadExceptionCount}</dt> <dd><%=stats.loadExceptionCount()%></dd>
		<dt>${messages.totalLoadTime}</dt> <dd><%=stats.totalLoadTime()%></dd>
		<dt>${messages.evictionCount}</dt> <dd><%=stats.evictionCount()%></dd>
		<%	} %>
	</dl><%
		}
	} %></c:if>