package org.example.springbootauthentication.manager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CustomRequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);
    private final Log logger = LogFactory.getLog(this.getClass());

    private final List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest request) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.format("Authorizing %s", request));
        }

        Iterator var3 = this.mappings.iterator();

        RequestMatcherEntry mapping;
        RequestMatcher.MatchResult matchResult;
        do {
            if (!var3.hasNext()) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace(LogMessage.of(() -> {
                        return "Denying request since did not find matching RequestMatcher";
                    }));
                }

                return DENY;
            }

            mapping = (RequestMatcherEntry)var3.next();
            RequestMatcher matcher = mapping.getRequestMatcher();
            matchResult = matcher.matcher(request);
        } while(!matchResult.isMatch());

        AuthorizationManager<RequestAuthorizationContext> manager = (AuthorizationManager)mapping.getEntry();
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.format("Checking authorization on %s using %s", request, manager));
        }

        return manager.check(authentication, new RequestAuthorizationContext(request, matchResult.getVariables()));
    }// check

    public List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> getMappings() {
        return mappings;
    }// getMappings


}// CustomRequestMatcherDelegatingAuthorizationManager
