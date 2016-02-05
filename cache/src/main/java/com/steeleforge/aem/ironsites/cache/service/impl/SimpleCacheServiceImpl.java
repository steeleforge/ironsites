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
package com.steeleforge.aem.ironsites.cache.service.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.steeleforge.aem.ironsites.cache.service.SimpleCacheConfiguration;
import com.steeleforge.aem.ironsites.cache.service.SimpleCacheService;

/**
 * Provides access to OSGi factory configured Cache instances.
 * 
 * @author David Steele
 */
@Service
@Component(label = "ironsites - Simple Cache Service",
    description = "Acquire configured cache instances from this service.",
    immediate = true)
@References({
    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC, 
            referenceInterface = SimpleCacheConfiguration.class,
            name = "SimpleCacheConfiguration",
            bind = "bindConfigurations",
            unbind = "unbindConfigurations")
})
public class SimpleCacheServiceImpl implements SimpleCacheService {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(SimpleCacheServiceImpl.class);

    ComponentContext componentContext;
    private Cache<String, Cache<Object, Object>> caches;
    private Map<String, SimpleCacheConfiguration> configs;

    public Cache<Object, Object> getCache(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Cache<Object, Object> cache = caches.getIfPresent(name);
        if (null == cache) {
            if (configs.containsKey(name)) {
                cache = getCache(name, configs.get(name).getSpecs(), configs.get(name).isRecordStats());
            } else {
                cache = getCache(name, SimpleCacheConfigurationImpl.DEFAULT_CACHE_SPECFICIATIONS, 
                        SimpleCacheConfigurationImpl.DEFAULT_CACHE_STATS_ENABLED);
            }
        }
        return cache;
    }

    public Cache<Object, Object> getCache(String name, String spec, boolean stats) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Cache<Object, Object> cache = caches.getIfPresent(name);
        if (null != cache) {
            return cache;
        }
        cache = buildCache(spec, stats);   
        caches.put(name, cache);
        return cache;
    }

    public Map<Object, Object> getCacheMap(String name) {
        return getCache(name).asMap();
    }

    public CacheStats getStats(String name) {
        return getCache(name).stats();
    }

    public Set<String> getCaches() {
        return caches.asMap().keySet();
    }
    
    public void clearCache(String name) {
        Cache<Object, Object> cache = caches.getIfPresent(name);
        if (null != cache) {
            cache.invalidateAll();
            caches.invalidate(name);
        }
    }
    
    private Cache<Object, Object> buildCache(String spec, boolean stats) {
        CacheBuilder<Object, Object> builder = CacheBuilder.from(spec);
        if (stats) {
            builder = builder.recordStats();
        }
        return builder.build();
    }

    @Activate
    protected void activate(ComponentContext ctx) {
        this.componentContext = ctx;
        this.caches = CacheBuilder.newBuilder().build();
        this.configs = new ConcurrentHashMap<String, SimpleCacheConfiguration>();
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {
        this.caches.invalidateAll();
        this.caches = null;
        this.componentContext = null;
    }

    protected void bindConfigurations(ServiceReference<?> ref) {
        Object svc = this.componentContext.locateService("SimpleCacheConfiguration", ref);
        SimpleCacheConfiguration config;
        if (svc instanceof SimpleCacheConfiguration) {
            config = (SimpleCacheConfiguration)svc;
            configs.put(config.getName(), config);
            caches.put(config.getName(), buildCache(config.getSpecs(), config.isRecordStats()));
        }
    }

    protected void unbindConfigurations(ServiceReference<?> ref) {
        caches.invalidateAll();
    }
}
