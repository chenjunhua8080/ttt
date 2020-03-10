package com.cjh.ttt.toutiao;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 头条配置
 *
 * @author cjh
 * @date 2020/3/10
 */
@ConfigurationProperties(prefix = "toutiao")
@Component
@RefreshScope
@Data
public class TouTiaoConfig {

    private String appid;
    private String secret;

}
