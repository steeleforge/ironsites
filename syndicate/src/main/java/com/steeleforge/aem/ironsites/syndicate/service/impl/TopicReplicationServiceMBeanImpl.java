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
package com.steeleforge.aem.ironsites.syndicate.service.impl;

import javax.management.DynamicMBean;
import javax.management.NotCompliantMBeanException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import com.adobe.granite.jmx.annotation.AnnotatedStandardMBean;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationStatus;
import com.steeleforge.aem.ironsites.syndicate.service.TopicReplicationService;
import com.steeleforge.aem.ironsites.syndicate.service.TopicReplicationServiceMBean;

@Service(value = DynamicMBean.class)
@Component(name = "ironsites TopicReplicationService JMX", immediate = true)
@Property(name = "jmx.objectname", value = "com.steeleforge.aem.ironsites.syndicate:type=TopicReplicationServiceMBean")
public class TopicReplicationServiceMBeanImpl extends AnnotatedStandardMBean implements TopicReplicationServiceMBean {

    @Reference
    private TopicReplicationService service;

    public TopicReplicationServiceMBeanImpl() throws NotCompliantMBeanException {
        super(TopicReplicationServiceMBean.class);
    }

    @Override
    public boolean replicate(final ReplicationActionType type, final String path,
            String topic) {
        return service.replicate(type, path, topic);
    }

    @Override
    public boolean activate(final String path, final String topic) {
        return service.activate(path, topic);
    }

    @Override
    public boolean deactivate(final String path, final String topic) {
       return service.deactivate(path, topic);
    }

    @Override
    public boolean delete(final String path, final String topic) {
        return service.delete(path, topic);
    }

    @Override
    public ReplicationStatus getStatus(final String path) {
        return service.getStatus(path);
    }

    @Override
    public String[] getTopics() {
        return service.getTopics();
    }

    @Override
    public String[] getAgents(final String topic) {
        return service.getAgents(topic);
    }
}
