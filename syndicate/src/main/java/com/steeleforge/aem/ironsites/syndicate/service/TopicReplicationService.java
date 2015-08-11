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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.AgentFilter;
import com.day.cq.replication.AgentIdFilter;
import com.day.cq.replication.AgentManager;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.Replicator;
import com.google.common.collect.Maps;
import com.steeleforge.aem.ironsites.wcm.ResourceResolverSubservice;

/**
 * OSGi service used to replicate a content path based on a specific topic
 * against dedicated agentIds. This allows APIs, for example, to syndicate
 * content across publish instances if replication agents to adjacent 
 * publish instances are configured.
 * 
 * @author David Steele
 */
@Service(value = TopicReplicationService.class)
@Component(label = "ironsites - Topic Replication Service",
    description = "Replicate resource through given agentIds per topic",
    immediate = true)
@References({
    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC, 
            referenceInterface = TopicReplicationConfiguration.class,
            name = "TopicReplicationConfiguration",
            bind = "bindConfigurations",
            unbind = "unbindConfigurations")
})
public class TopicReplicationService extends ResourceResolverSubservice {
    private static final Logger LOG = LoggerFactory.getLogger(TopicReplicationService.class);

    // OSGi managed services
    @Reference
    Replicator replicator;
    
    @Reference
    AgentManager agentManger;
    
    @Reference
    ResourceResolverFactory resourceResolverFactory;
    
    // statics
    public static final String IDENTITY_SERVICEMAPPER = "TopicReplicationService";
    private static final boolean DEFAULT_SYNCHRONOUS = false;    
    
    // locals
    private Map<String,Set<String>> agentsByTopic;
    protected ComponentContext componentContext;

    // properties
    @Property(label = "Replicate Synchronously", 
            description = "Default = false",
            boolValue = DEFAULT_SYNCHRONOUS)
    static final String PN_SYNCHRONOUS = "topicreplication.synchronous";
    protected boolean synchronous = DEFAULT_SYNCHRONOUS;
    
    @Activate
    protected void activate(ComponentContext context) {
        this.componentContext = context;
        this.synchronous = PropertiesUtil.toBoolean(context.getProperties()
                .get(PN_SYNCHRONOUS), DEFAULT_SYNCHRONOUS);
        this.agentsByTopic = Maps.newHashMap();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        this.agentsByTopic.clear();
    }
    
    /**
     * Replicate action against a path with the given type with agents based on the available topic
     * 
     * @param type type of replication, convenience methods exist
     * @param path sling resource
     * @param topic topic of replication
     * @return true if replication is successful, false otherwise
     */
    public boolean replicate(final ReplicationActionType type, final String path, final String topic) {
        if (meetsReplicationPreconditions(type, path, StringUtils.lowerCase(topic, Locale.getDefault()))) {
            try {
                replicator.replicate(getSession(resourceResolverFactory), 
                        type, 
                        path, 
                        getReplicationOptions(StringUtils.lowerCase(topic, Locale.getDefault())));
                closeResolver();
                return true;
            } catch (ReplicationException e) {
                LOG.trace(MessageFormat.format("Unable to replicate {0} for topic {1}: ", path, topic), e.getMessage());
            }
        }
        // clean up any admin sessions lingering after activity
        closeResolver();
        return false;
    }

    /**
     * Activate action against a content path with agents based on the available topic
     * 
     * @param path sling resource
     * @param topic topic of replication
     * @return true if replication is successful, false otherwise
     */
    public boolean activate(final String path, final String topic) {
        return replicate(ReplicationActionType.ACTIVATE, path, topic);
    }

    /**
     * Deactivate action against a content path with agents based on the available topic
     * 
     * @param path sling resource
     * @param topic topic of replication
     * @return true if replication is successful, false otherwise
     */
    public boolean deactivate(final String path, final String topic) {
        return replicate(ReplicationActionType.DEACTIVATE, path, topic);
    }

    /**
     * Delete action against content path with agents based on the available topic
     * 
     * @param path sling resource
     * @param topic topic of replication
     * @return true if replication is successful, false otherwise
     */
    public boolean delete(final String path, final String topic) {
        return replicate(ReplicationActionType.DELETE, path, topic);
    }
    
    /**
     * Retrieve replication status of a given content path
     * 
     * @param path sling resource
     * @return ReplicationStatus or null if unavailable
     */
    public ReplicationStatus getStatus(final String path) {
        return replicator.getReplicationStatus(getSession(resourceResolverFactory), path);
    }
    
    /**
     * Retrieve configured topics
     * 
     * @return configured topics
     */
    public String[] getTopics() {
        return this.agentsByTopic.keySet().toArray(new String[this.agentsByTopic.size()]);
    }
    
    /**
     * Retrieve configured AgentIds per Topic
     * 
     * @param topic
     * @return agentIds per topic
     */
    public String[] getAgents(final String topic) {
        if (this.agentsByTopic.containsKey(topic)) {
            return this.agentsByTopic.get(topic)
                       .toArray(new String[this.agentsByTopic.get(topic).size()]);
        } else {
            return null;
        }
    }

    /**
     * "Fail fast" validation of replication input parameters
     * 
     * @param type replication action
     * @param path sling resource
     * @param topic
     * @return
     */
    private boolean meetsReplicationPreconditions(final ReplicationActionType type, 
            final String path,
            final String topic) {
        if (null == type) {
            // no replication type
            LOG.trace("no replication type provided");
            return false;
        }
        if (StringUtils.isBlank(path)) {
            // no path to replicate
            LOG.trace("no replication valid path provided");
            return false;
        } else {
            if (null == getResourceResolver(resourceResolverFactory).resolve(path)) {
                LOG.trace("could not resolve {}: ", path);
                closeResolver();
                return false;
            }
            closeResolver();
        }
        if (StringUtils.isBlank(topic) // no topic to consider
                || !agentsByTopic.containsKey(topic)) { // no agent ids for a given topic
            LOG.trace("no valid topic or agents for a given topic");
            return false;
        }
        try { // replication not permitted
            replicator.checkPermission(getSession(resourceResolverFactory), type, path);
        } catch (ReplicationException e) {
            LOG.trace("service mapper user for TopicReplicator does not have permission to replicate {}: ",path, e.getMessage());
            return false;
        }
        return true;
    }
    
    /**
     * Retrieve ReplicationOptions inclusive of an AgentFilter per topic
     * 
     * @param topic
     * @return ReplicationOptions
     */
    private ReplicationOptions getReplicationOptions(final String topic) {
        ReplicationOptions options = new ReplicationOptions();
        options.setFilter(getTopicAgentFilter(topic));
        options.setSynchronous(this.synchronous);
        return options;
    }
    
    /**
     * Returns an AgentFilter based on agentIds configured per topic
     * 
     * @param topic
     * @return
     */
    private AgentFilter getTopicAgentFilter(final String topic) {
        Set<String> agents = agentsByTopic.get(topic);
        if (agents.isEmpty()) {
            return new AgentIdFilter(ArrayUtils.EMPTY_STRING_ARRAY);
        }
        return new AgentIdFilter(agents.toArray(new String[agents.size()]));
    }

    @Override
    public String getServiceMapperIdentity() {
        return IDENTITY_SERVICEMAPPER;
    }
    
    protected void bindConfigurations(ServiceReference ref) {
        Object svc = this.componentContext.locateService("TopicReplicationConfiguration", ref);
        TopicReplicationConfiguration config;
        if (svc instanceof TopicReplicationConfiguration) {
            config = (TopicReplicationConfiguration)svc;
            if (StringUtils.isNotBlank(config.getTopic())) {
                agentsByTopic.put(config.getTopic(), config.getAgents());
            }
        }
    }

    protected void unbindConfigurations(ServiceReference ref) {
        agentsByTopic.clear();
    }

}
