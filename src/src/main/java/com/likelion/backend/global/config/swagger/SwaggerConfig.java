package com.likelion.backend.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "멋사 충북대 백엔드 세션 API", description = "멋쟁이사자처럼 충북대 백엔드 세션 베이스 API입니다.",
                version = "1.0.0"),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 서버"),
                @Server(url = "추후 변경", description = "배포 서버")
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI();
    }
}
