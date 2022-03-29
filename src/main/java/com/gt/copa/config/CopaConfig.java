package com.gt.copa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@PropertySources({
		@PropertySource("classpath:copa.properties"),
		@PropertySource(value = "file:${app.home}/copa.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:copa.properties", ignoreResourceNotFound = true)
})
@Component
public class CopaConfig {

	@Value("${default.folder}")
    String defaultFolder;

	@Value("${autosave}")
    Boolean autosave;
}
