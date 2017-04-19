package ru.pavelyurkin.musiccomposer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value( "${build.version}" )
	private String buildVersion;

	@Bean
	public Docket api() {
		return new Docket( DocumentationType.SWAGGER_2 )
				.apiInfo( apiInfo() )
				.select()
				.apis( RequestHandlerSelectors.withClassAnnotation( RestController.class ) )
				.paths( PathSelectors.any() )
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfo( "Music Composer",
				"Composing service",
				buildVersion,
				"",
				new Contact( "Pavel Yurkin", "http://yurkins-workshop.ru", "woodythegreat@yandex.ru" ),
				"",
				"" );
	}

}
