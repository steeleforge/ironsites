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

import java.util.Dictionary;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;

import com.steeleforge.aem.ironsites.cache.service.SimpleCacheConfiguration;

@Service
@Component(label = "ironsites - Simple Cache Configuration",
    description = "Configuration pool for Google CacheBuilder",
    immediate = true,
    configurationFactory = true, 
    metatype = true, 
    policy = ConfigurationPolicy.REQUIRE)
public class SimpleCacheConfigurationImpl implements SimpleCacheConfiguration {
    private ComponentContext componentContext = null;

    static final String DEFAULT_CACHE_NAME = "Cache_";
    public static final String DEFAULT_CACHE_SPECFICIATIONS = "";
    public static final boolean DEFAULT_CACHE_STATS_ENABLED = true;
    
    @Property(label = "Cache Name", description = "Name of cache")
    static final String PN_CACHE_NAME = "name";
    protected String name;
    
    @Property(label = "Cache Specfications", value = "", 
            description = "http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/CacheBuilderSpec.html")
    static final String PN_CACHE_SPECIFICATIONS = "specs";
    protected String specs;

    @Property(label = "Enable Cache Statistics", 
            description = "Enable statistics",
            boolValue = DEFAULT_CACHE_STATS_ENABLED)
    static final String PN_CACHE_STATS_ENABLED = "statsEnabled";
    protected Boolean statsEnabled;
    
    @Activate
    protected void activate(ComponentContext ctx) {
        Dictionary<?,?> props = ctx.getProperties();
        this.name = PropertiesUtil.toString(props.get(PN_CACHE_NAME), DEFAULT_CACHE_NAME + ctx.getComponentInstance().toString());
        this.specs = PropertiesUtil.toString(props.get(PN_CACHE_SPECIFICATIONS), DEFAULT_CACHE_SPECFICIATIONS);
        this.statsEnabled = PropertiesUtil.toBoolean(props.get(PN_CACHE_STATS_ENABLED), DEFAULT_CACHE_STATS_ENABLED);
        this.componentContext = ctx;
    }

    public String getName() {
        return this.name;
    }

    public String getSpecs() {
        if (null == this.specs) {
            return DEFAULT_CACHE_SPECFICIATIONS;
        }
        return this.specs;
    }

    public Boolean isRecordStats() {
        if (null == this.statsEnabled) {
            return DEFAULT_CACHE_STATS_ENABLED;
        }
        return this.statsEnabled;
    }
}
