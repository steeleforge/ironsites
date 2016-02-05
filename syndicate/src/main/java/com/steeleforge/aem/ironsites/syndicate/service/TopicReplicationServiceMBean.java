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

import com.adobe.granite.jmx.annotation.Description;

import com.day.cq.replication.ReplicationStatus;
import com.day.cq.replication.ReplicationActionType;

@Description("Topic Replication Service MBean")
public interface TopicReplicationServiceMBean {
    @Description("Replicate Topic")
    public boolean replicate(final ReplicationActionType type, final String path, final String topic);
    @Description("Activate Topic")
    public boolean activate(final String path, final String topic);
    @Description("Deactivate Topic")
    public boolean deactivate(final String path, final String topic);
    @Description("Delete Topic")
    public boolean delete(final String path, final String topic);
    @Description("Replication Status")
    public ReplicationStatus getStatus(final String path);
    @Description("Replication Topics")
    public String[] getTopics();
    @Description("Replication Topic AgentIds")
    public String[] getAgents(final String topic);
}
