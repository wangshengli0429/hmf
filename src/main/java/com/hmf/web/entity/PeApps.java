package com.hmf.web.entity;

import lombok.Data;

@Data
public class PeApps {
    private Integer id;

    private Integer appId;

    private String appSecret;

    private String appName;

    private String createdTime;

    private String updatedTime;
}