package com.project.everycloud.common.config;

import com.project.everycloud.service.InstallService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        return InstallService.encrytConfig();
    }

}
