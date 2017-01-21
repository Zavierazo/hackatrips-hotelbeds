package com.hacktrips.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.hacktrips.config.contamination.ContaminationData;
import com.hacktrips.enums.CacheEnum;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContaminationService {
    @Autowired
    private CacheManager cacheManager;

    public ContaminationData getContamination(Double latitude, Double longitude) {
        ContaminationData contamination = new ContaminationData(latitude, longitude);
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.CONTAMINATION_CACHE);
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        //Map<String, ContaminationData>

        return contamination;
    }
}
