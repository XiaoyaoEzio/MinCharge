package com.min.charge.listener;

import com.min.charge.buffer.DeviceBuffer;
import com.min.charge.config.Config;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ConfigInitListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            Config.Instance.loadConfig();
            DeviceBuffer.Instance.init();
            //ThreadOffline.Instance.start();
        }
    }
}
