package com.yunqiao.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class RestApplication extends ResourceConfig {
    public RestApplication(){
        this.packages("com.yunqiao.rest.service");
        this.register(CorsFilter.class);
    }
}