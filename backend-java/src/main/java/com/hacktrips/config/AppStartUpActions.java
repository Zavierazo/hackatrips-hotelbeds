package com.hacktrips.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.Cache;
import com.hacktrips.config.contamination.ContaminationData;
import com.hacktrips.config.contamination.ContaminationReader;
import com.hacktrips.controller.MinubeController;
import com.hacktrips.enums.CacheEnum;
import com.hacktrips.util.Utils;
import lombok.AllArgsConstructor;

@Configuration
public class AppStartUpActions {
    @Autowired
    private MinubeController minubeController;
    @Autowired
    private CacheManager cacheManager;

    // Any startup sync action
    @PostConstruct
    public void startUpActionsSync() throws FileNotFoundException, IOException {
        Utils.ignoreSSL(); //Used to ignore SSL when atack to a https url
        // Start async tasks thread
        StartUpActionsAsync startActions = new StartUpActionsAsync();
        startActions.start();
        Map<String, ContaminationData> contamination = ContaminationReader.readCSV();
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.CONTAMINATION_CACHE);
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        cacheMap.putAll(contamination);
    }

    @AllArgsConstructor
    private class StartUpActionsAsync extends Thread {

        @Override
        public void run() {
            minubeController.byLatitude(40.4137859, -3.6943158);
        }

    }
}
