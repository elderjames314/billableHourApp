package com.techustle.billablehour.v1.backend.docs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


//Swagger documenation
@Configuration
@EnableSwagger2
class Swagger {

    @Bean
    fun swaggerConfiguration(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.techustle"))
                .build()
                .apiInfo(AppDocInfo())
    }

    private fun AppDocInfo(): ApiInfo? {
        return ApiInfo(
                "Employee Billable Hour App", "Billable hour’s app is all about management of bill allocated to" +
                " company lawyers for the job done, for every lawyer, there is billable rate as per their grade and work did. \n" +
                "For every project they worked on, they have to send the total hours spent " +
                "to the finance team and in turn, finance can generate receipts to the client accordingly.\n",
                "1.0",
                "https://techustle.com",
                "Techustle Inc",
                "Api License- MIT",
                "https://techustle.com")
    }


}