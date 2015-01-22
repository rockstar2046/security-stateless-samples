import com.rockagen.gnext.service.spring.security.extension.*
import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.security.access.vote.AffirmativeBased
import org.springframework.security.access.vote.RoleVoter
import org.springframework.security.authentication.AuthenticationDetailsSource
import org.springframework.security.config.http.HttpSecurityBeanDefinitionParser
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.util.matcher.RequestMatcher

import javax.servlet.http.HttpServletRequest

// authorization url
authorizeUrl={
    it.getRequestURI().endsWith("/authorizations")
} as RequestMatcher

// Authentication Details Source (Get real ip)
exAuthenticationDetailsSource={
    new ExWebAuthenticationDetails(it)
} as AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>

beans{

    xmlns([
            sec : 'http://www.springframework.org/schema/security',
    ])

    sec{
        // Open api
        http("use-expressions":true,pattern:"/public/*",security:"none")
       
        http("use-expressions":true,"create-session":"stateless","entry-point-ref":"exAuthenticationEntryPoint"){
            anonymous(enabled:false)

            // Token authentication filter
            "custom-filter"(ref:"tokenAuthenticationFilter",before:"PRE_AUTH_FILTER")

            // Form login filter
            "custom-filter"(ref:"formLoginFilter",position:"FORM_LOGIN_FILTER")

            // Before default authentication intercepter
            "custom-filter"(ref:"filterSecurityInterceptor",before:"FILTER_SECURITY_INTERCEPTOR")

        }

        // Authentication manager,provide to filterSecurityInterceptor filter
        "authentication-manager"(alias:"authenticationManager"){
            "authentication-provider"(ref:"exAuthenticationProvider")
        }

    }

    // Entry point
    exAuthenticationEntryPoint(ExAuthenticationEntryPoint)

    // FORM_LOGIN_FILTER implementation
    formLoginFilter(UsernamePasswordAuthenticationFilter){
        authenticationManager=ref("authenticationManager")
        authenticationDetailsSource=exAuthenticationDetailsSource
        authenticationSuccessHandler=ref("exAuthenticationSuccessHandler")
        authenticationFailureHandler=ref("exAuthenticationFailureHandler")
        usernameParameter="uid"
        passwordParameter="passwd"
        requiresAuthenticationRequestMatcher=authorizeUrl
        postOnly=true
        allowSessionCreation=false
    }
    

    // FILTER_SECURITY_INTERCEPTOR implementation
    filterSecurityInterceptor(FilterSecurityInterceptor){
        securityMetadataSource=ref("exSecurityMetadataSource")
        accessDecisionManager=ref("accessDecisionManager")
        authenticationManager=ref("authenticationManager")
    }


    // Authentication provider
    exAuthenticationProvider(ExAuthenticationProvider){
        authUserServ=ref("authUserServ")
        lockedTime=10800000
    }

    // TokenAuthentication bean
    exTokenAuthentication(ExTokenAuthentication){
        // one week
        expiredTime=7 * 24 * 60 * 60 * 1000
        //Conf.get("xxx")
        keyName="spring.security.token.key"
        authUserServ=ref("authUserServ")
    }

    // Token authentication filter
    tokenAuthenticationFilter(TokenAuthenticationFilter) {
        tokenName = "X-AUTH-TOKEN"
        exTokenAuthentication = ref("exTokenAuthentication")
    }


    // Authentication handler
    exAuthenticationHandler(ExAuthenticationHandler){
        exTokenAuthentication=ref("exTokenAuthentication")
        authUserServ=ref("authUserServ")
        tokenName="X-AUTH-TOKEN"
        userReferer="X-Referer"
        username="uid"
        maxFailedAttempts=6
        lockedTime=10800000
        
    }
    // Authentication success handler
    exAuthenticationSuccessHandler(ExAuthenticationSuccessHandler){
        exAuthenticationHandler=ref("exAuthenticationHandler")
    }

    // Authentication failure handler
    exAuthenticationFailureHandler(ExAuthenticationFailureHandler){
        exAuthenticationHandler=ref("exAuthenticationHandler")

    }

    // Using RoleVoter to voter , NOTE: "spring security" has a specific prefix
    roleVoter(RoleVoter){
        // Role prefix, here use "ROLE_"
        rolePrefix="ROLE_"
    }

    // Implementation of the access Decision, use "AffirmativeBased",as long
    // as you meet one of the permissions,grant the privileges
    // See org.springframework.security.access.vote.AuthenticatedVoter
    accessDecisionManager(AffirmativeBased,decisionVoters:[ref("roleVoter")])

    // Define resource privileges
    exSecurityMetadataSource(ExFilterInvocationSecurityMetadataSource){
        authResourceServ=ref("authResourceServ")
        cache=ref("exSecurityMetadataSourceCache")
    }
    // Cache manager
    cacheManager(EhCacheManagerFactoryBean){
        shared=true
    }
    exSecurityMetadataSourceCache(EhCacheFactoryBean){
        cacheManager=ref("cacheManager")
        cacheName="ex.SecurityMetadataSourceCache"
    }

    // Define spring exception International tips
    messageSource(ReloadableResourceBundleMessageSource) {
        basename = "classpath:security/messages"
    }
}