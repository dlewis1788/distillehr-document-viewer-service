package com.projectivesoftware.viewer.configuration;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("discovery")
@Configuration
@EnableDiscoveryClient
public class DiscoveryConfiguration {

}
