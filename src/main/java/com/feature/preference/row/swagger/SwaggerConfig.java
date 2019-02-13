package com.feature.preference.row.swagger;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api(){
		return new Docket(DocumentationType.SWAGGER_2);
	}
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
	    StringHttpMessageConverter converter = new StringHttpMessageConverter();
	    converter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "plain", Charset.forName("UTF-8"))));
	    return converter;
	}
}
