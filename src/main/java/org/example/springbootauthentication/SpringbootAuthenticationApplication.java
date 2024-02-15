package org.example.springbootauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringbootAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAuthenticationApplication.class, args);
    }

}
