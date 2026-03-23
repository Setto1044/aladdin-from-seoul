package com.aladdin.core_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aladdin From Seoul 아파트 매매 정보 API")
                        .version("v1.0.0")
                        .description("아파트 매물 검색 및 실거래가 조회 서비스"));
    }
}
