package com.hacktrips.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hacktrips.controller.MinubeController;
import com.hacktrips.util.Utils;
import lombok.AllArgsConstructor;

@Configuration
public class AppStartUpActions {
    @Autowired
    private MinubeController minubeController;

    // Any startup sync action
    @PostConstruct
    public void startUpActionsSync() {
        Utils.ignoreSSL(); //Used to ignore SSL when atack to a https url

        // Start async tasks thread
        StartUpActionsAsync startActions = new StartUpActionsAsync();
        startActions.start();
    }

    @AllArgsConstructor
    private class StartUpActionsAsync extends Thread {

        @Override
        public void run() {
            minubeController.byLatitude(40.4137859, -3.6943158);
        }

    }
}
