package com.hacktrips.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.hacktrips.config.contamination.ContaminationData;
import com.hacktrips.enums.CacheEnum;
import com.hacktrips.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContaminationService {
    @Autowired
    private CacheManager cacheManager;

    public ContaminationData getContamination(Double latitude, Double longitude) {
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.CONTAMINATION_CACHE);
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        //Map<String, ContaminationData>
        ContaminationData lessDistanceData = null;
        Double lessDistance = Double.MAX_VALUE;
        for (Object data : cacheMap.values()) {
            ContaminationData cachedData = (ContaminationData) data;
            Double distance = Utils.distance(latitude, cachedData.getLatitude(), longitude, cachedData.getLongitude());
            if (distance < lessDistance) {
                lessDistance = distance;
                lessDistanceData = cachedData;
            }
        }
        ContaminationData contamination = new ContaminationData(latitude, longitude);
        if (lessDistanceData != null) {
            contamination.setContaminationByHour(lessDistanceData.getContaminationByHour());
        }
        return contamination;
    }

    public ContaminationData getContaminationInterpolation(Double latitude, Double longitude) {
        ContaminationData contamination = new ContaminationData(latitude, longitude);
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.CONTAMINATION_CACHE);
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        //Map<String, ContaminationData>
        List<ContaminationData> cerca = new ArrayList<>();
        double minDistance = Double.MAX_VALUE;
        double maxDistance = Double.MIN_VALUE;
        int radioBusqueda = 5000;
        while (cerca.isEmpty()) {
            for (Object data : cacheMap.values()) {
                ContaminationData cachedData = (ContaminationData) data;
                Double distance = Utils.distance(latitude, cachedData.getLatitude(), longitude, cachedData.getLongitude());
                if (distance < radioBusqueda) {
                    cerca.add(cachedData);
                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                    if (distance > maxDistance) {
                        maxDistance = distance;
                    }
                }
            }
            //si no se encuentra nada en 5km, vuelve a repetir la busqueda con el doble de distancia
            radioBusqueda = radioBusqueda * 2;
        }
        for (int hora = 1; hora <= 24; hora++) {
            double weight = 0.0;
            double size = 0.0;
            for (ContaminationData data : cerca) {
                Double distance = Utils.distance(latitude, data.getLatitude(), longitude, data.getLongitude());
                double normal = normalizedValue(distance, maxDistance, minDistance);
                size += normal;
                weight += data.getContaminationByHour().get(hora) * normal;
            }
            contamination.getContaminationByHour().put(hora, weight / size);
        }
        return contamination;
    }

    private static double normalizedValue(double value, double max, double min) {
        if (max == min) {
            return 0.5;
        }
        return 1 - ((value - min) / (max - min));
    }

    public ContaminationData getContaminationMean(Double latitude, Double longitude) {
        ContaminationData contamination = new ContaminationData(latitude, longitude);
        GuavaCache guavaCache = (GuavaCache) cacheManager.getCache(CacheEnum.CONTAMINATION_CACHE);
        Cache<Object, Object> cache = guavaCache.getNativeCache();
        Map<Object, Object> cacheMap = cache.asMap();
        //Map<String, ContaminationData>
        List<ContaminationData> cerca = new ArrayList<>();
        double minDistance = Double.MAX_VALUE;
        double maxDistance = Double.MIN_VALUE;
        for (Object data : cacheMap.values()) {
            ContaminationData cachedData = (ContaminationData) data;
            Double distance = Utils.distance(latitude, cachedData.getLatitude(), longitude, cachedData.getLongitude());
            if (distance < 5000) {
                cerca.add(cachedData);
                if (distance < minDistance) {
                    minDistance = distance;
                }
                if (distance > maxDistance) {
                    maxDistance = distance;
                }
            }
        }
        for (int hora = 1; hora <= 24; hora++) {
            double weight = 0.0;
            for (ContaminationData data : cerca) {
                weight += data.getContaminationByHour().get(hora);
            }
            contamination.getContaminationByHour().put(hora, weight / cerca.size());
        }
        return contamination;
    }
}
