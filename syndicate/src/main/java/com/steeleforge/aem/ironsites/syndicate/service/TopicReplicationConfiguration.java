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
package com.steeleforge.aem.ironsites.syndicate.service;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;

import com.google.common.collect.Sets;

/**
 * OSGi configuration factory for Topic Replication
 * 
 * @author David Steele
 */
@Service(value = TopicReplicationConfiguration.class)
@Component(label = "ironsites - Topic Replication Configuration",
    description = "Configuration for the Topic Replication Service",
    immediate = true,
    configurationFactory = true,
    metatype = true, 
    policy = ConfigurationPolicy.REQUIRE)
public class TopicReplicationConfiguration {
    @SuppressWarnings("unused")
    private ComponentContext componentContext = null;
    public static final String REFERENCE = "TopicReplicationConfiguration";
    
    @Property(label = "Topic", description = "Topic name/descriptor")
    static final String PN_TOPIC = "topicreplication.configuration.topic";
    protected String topic = null;
    
    @Property(label = "Topic Agents", description = "AgentId for filter")
    static final String PN_AGENTS = "topicreplication.configuration.agents";
    protected String[] agents = null;
    private Set<String> agentIds = Collections.emptySet();
    static final String[] DEFAULT_AGENTS = new String[] {};
   
    @Activate
    protected void activate(ComponentContext ctx) {
        Dictionary<?,?> props = ctx.getProperties();
        this.componentContext = ctx;
        this.topic = PropertiesUtil.toString(props.get(PN_TOPIC), ctx.getComponentInstance().toString());
        this.agents = PropertiesUtil.toStringArray(props.get(PN_AGENTS), DEFAULT_AGENTS);
        if (this.agents.length > 0) {
            this.agentIds = Sets.newHashSet();
            for(String agent : this.agents) {
                this.agentIds.add(agent);
            }
        }
    }

    /**
     * Replication Topic
     * 
     * @return cache name
     */
    public String getTopic() {
        return this.topic;
    }

    /**
     * Replication AgentId (filters)
     */
    public Set<String> getAgents() {
        return this.agentIds;
    }
}
