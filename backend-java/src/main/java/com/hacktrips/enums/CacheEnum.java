package com.hacktrips.enums;

import lombok.Getter;

@Getter
public enum CacheEnum {

    GENERIC,//Cache without expiration & entry limit
    POIS_BY_NAME,
    POIS_BY_ID,
    POIS_MINUBE,
    LIMITED_CACHE(-1, -1, 10),//Cache with limit of 10 elements & no expiration
    EXAMPLE(-1, 10, -1);//Cache with expiration on 10 seconds & no entry limit

    public static final String EXAMPLE_CACHE = "EXAMPLE";
    public static final String POIS_MINUBE_CACHE = "POIS_MINUBE";
    public static final String PLACE_GOOGLE_CACHE = "PLACE_GOOGLE";

    private long readExpiration = -1;// By default without expiration
    private long writeExpiration = -1;// By default without expiration
    private long limit = -1;// By default without expiration

    CacheEnum() {

    }

    CacheEnum(long readExpiration, long writeExpiration, long limit) {
        this.readExpiration = readExpiration;
        this.writeExpiration = writeExpiration;
        this.limit = limit;
    }
}
