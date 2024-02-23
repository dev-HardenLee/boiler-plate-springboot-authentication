package org.example.springbootauthentication.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.springbootauthentication.context.ResourceRoleContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 모든 자원과 각 자원에 허용되는 권한을 조회 후 보안 설정.
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class ResourceRoleInitializerRunner implements ApplicationRunner {

    private final ResourceRoleContext resourceRoleContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        resourceRoleContext.initialize();
    }// run

}// RoleHierarchyRunner


















