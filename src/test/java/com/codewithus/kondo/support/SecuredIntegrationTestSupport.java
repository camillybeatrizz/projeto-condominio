package com.codewithus.kondo.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "kondo.security.enabled=true",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://issuer.test/kondo",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://issuer.test/.well-known/jwks.json"
})
@AutoConfigureMockMvc
public abstract class SecuredIntegrationTestSupport extends DatabaseCleanupSupport {

    @Autowired
    protected MockMvc mockMvc;
}
