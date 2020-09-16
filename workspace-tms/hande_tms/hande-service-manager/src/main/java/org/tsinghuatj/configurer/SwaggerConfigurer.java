package org.tsinghuatj.configurer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnClass(springfox.documentation.spring.web.plugins.Docket.class)
public class SwaggerConfigurer {

	private static final String TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMi0wMTAxMDEsMDEwMTAxMDEsMDEwMTAxMDIsMDEwMTAxMDMsIiwiZXhwIjoxNTY1NTA5Mjc2fQ.lYY2hJmLQBrl20o68v-hTR_K_AbAv68rGBi-eT7WDw4SGVjVFNm8oRMpviDAkXLT2C3Mvl0s6VIrrbL1_tmrRg";

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Spring Boot中使用Swagger2构建RESTFUL APIS").description("Mos项目后台API接口文档")
				.version("1.0").build();
	}

	@Bean("docket1")
	public Docket docket() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<>();
		tokenPar.name("Authorization").defaultValue(TOKEN).description("令牌").modelRef(new ModelRef("string"))
				.parameterType("header").required(false).build();
		pars.add(tokenPar.build());

		// return new
		// Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build().globalOperationParameters(pars).apiInfo(apiInfo());
		return new Docket(DocumentationType.SWAGGER_2).groupName("user").select().paths(PathSelectors.regex("/user/.*"))
				.build().globalOperationParameters(pars).apiInfo(apiInfo());
	}

	@Bean("docket2")
	public Docket docket1() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<>();
		tokenPar.name("Authorization").defaultValue(TOKEN).description("令牌").modelRef(new ModelRef("string"))
				.parameterType("header").required(false).build();
		pars.add(tokenPar.build());
		return new Docket(DocumentationType.SWAGGER_2).groupName("tool").select().paths(PathSelectors.regex("/tool/.*"))
				.build().globalOperationParameters(pars).apiInfo(apiInfo());
	}

	@Bean("docket3")
	public Docket docket3() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("auth").select().paths(PathSelectors.regex("/auth/.*"))
				.build();
	}
	
	@Bean("docket4")
	public Docket docket4() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("base").select().paths(PathSelectors.regex("/base/.*"))
				.build();
	}

}
