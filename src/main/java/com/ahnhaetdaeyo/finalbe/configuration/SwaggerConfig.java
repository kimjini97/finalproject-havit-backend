package com.ahnhaetdaeyo.finalbe.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Let's Do It 안했대요 API명세서",
                description = "회원가입, 로그인 API 명세서",
                version = "v1",
                contact = @Contact(
                        name = "이노베이션 캠프 8조",
                        url = "https://www.notion.so/692e427036ff4f168a3e933d1c9f2343"
                )
        )
)
//todo: SecuritySchemes
@Configuration
public class SwaggerConfig {

    //
//    @Bean
//    public GroupedOpenApi sampleGroupOpenApi() {
//        String[] paths = {"/member/**"};
//
//        return GroupedOpenApi.builder().group("샘플 멤버 API").pathsToMatch(paths)
//            .build();
//    }

}
