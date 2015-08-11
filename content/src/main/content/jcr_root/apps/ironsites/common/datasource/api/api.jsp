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
    
    ironsites - API datasource.
    
--%><%
%><%@include file="/apps/ironsites/common/global.jsp"%><%
%><%@page session="false"
          import="java.util.HashMap,
                  com.steeleforge.aem.ironsites.wcm.page.PageUse,
                  org.apache.commons.collections.Transformer,
                  org.apache.commons.collections.iterators.IteratorChain,
                  org.apache.commons.collections.iterators.TransformIterator,
                  org.apache.sling.api.resource.ResourceMetadata,
                  org.apache.sling.api.resource.ResourceResolver,
                  org.apache.sling.api.wrappers.ValueMapDecorator,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.ds.DataSource,
                  com.adobe.granite.ui.components.ds.SimpleDataSource,
                  com.adobe.granite.ui.components.ds.ValueMapResource,
                  com.day.cq.commons.inherit.InheritanceValueMap
                  com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap"%><%
/**
    A datasource returning tag key-value pairs representing service options for a given site

    @datasource
    @name Tags
    @location /apps/ironsites/common/datasources/api
    
    @property {String[]} [root] root page path, required to locate site

 */
Config dsCfg = new Config(resource.getChild("datasource")); 
String site = null;
String path = dsCfg.get("page", String.class);
ApiService apiService = sling.getService(ApiService.class);
Page page = WCMUtil.getPage(request, path);
if (null != page) {
    InheritanceValueMap vm = new HierarchyNodeInheritanceValueMap(page.getContentResource());
    site = vm.getInherited(PageUse.PN_SITE, String.class);
}
            
IteratorChain services = new IteratorChain();
if (null != apiService) {
    services.addIterator(apiService.getServices(site).iterator());
}

@SuppressWarnings("unchecked")
DataSource datasource = new SimpleDataSource(new TransformIterator(services, new Transformer() {
    public Object transform(Object o) {
        String svc = (String)o;
        ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
        vm.put("value", svc);
        vm.put("text", svc);
        return new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm);
    }
}));

request.setAttribute(DataSource.class.getName(), datasource);
%>