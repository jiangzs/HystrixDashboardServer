package com.jzs.server;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.turbine.discovery.Instance;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.cloud.netflix.turbine.EurekaInstanceDiscovery;
import org.springframework.cloud.netflix.turbine.TurbineProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jiangzs@gmail.com on 2017/3/22.
 */
@CommonsLog
public class MyEurekaInstanceDiscovery extends EurekaInstanceDiscovery {

    private final EurekaClient eurekaClient;

    public MyEurekaInstanceDiscovery(TurbineProperties turbineProperties, EurekaClient eurekaClient) {
        super(turbineProperties, eurekaClient);
        this.eurekaClient = eurekaClient;
    }

    @Override
    public Collection<Instance> getInstanceList() throws Exception {
        List<Instance> instances = new ArrayList<>();
        List<Application> appList = eurekaClient.getApplications().getRegisteredApplications();
        appList.stream().forEach(s->{
            try {
                instances.addAll(getInstancesForApp(s.getName()));
            }
            catch (Exception ex) {
                log.error("Failed to fetch instances for app: " + s.getName()
                        + ", retrying once more", ex);
                try {
                    instances.addAll(getInstancesForApp(s.getName()));
                }
                catch (Exception retryException) {
                    log.error("Failed again to fetch instances for app: " + s.getName()
                            + ", giving up", ex);
                }
            }
        });
        return instances;
    }
}
