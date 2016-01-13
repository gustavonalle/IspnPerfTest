package org.cache.impl;

import org.cache.Cache;
import org.cache.CacheFactory;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.Transport;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.jgroups.Channel;
import org.jgroups.View;

/**
 * @author Bela Ban
 * @since x.y
 */
@Listener
public class InfinispanCacheFactory<K,V> implements CacheFactory<K,V> {
    protected EmbeddedCacheManager mgr;

    /** Empty constructor needed for an instance to be created via reflection */
    public InfinispanCacheFactory() {
    }

    public void init(String config) throws Exception {
        mgr=new DefaultCacheManager(config);
        mgr.addListener(this);
    }

    public void destroy() {
        mgr.stop();
    }

    public Cache<K,V> create(String cache_name) {
        return new InfinispanCache(mgr.getCache(cache_name));
    }

    @ViewChanged
    public static void viewChanged(ViewChangedEvent evt) {
        Transport transport=evt.getCacheManager().getTransport();
        if(transport instanceof JGroupsTransport) {
            Channel ch=((JGroupsTransport)transport).getChannel();
            View view=ch.getView();
            System.out.println("** view: " + view);
        }
        else
            System.out.println("** view: " + evt);
    }
}
