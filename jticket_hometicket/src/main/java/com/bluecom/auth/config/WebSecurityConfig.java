package com.bluecom.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	// 주소가 추가될 때마다 인증 추가
        
		http
    		.headers()
				.httpStrictTransportSecurity();
//    			.contentSecurityPolicy("default-src 'self';")
//    			.and()
//    			.contentSecurityPolicy("font-src 'self' https://jbimweb.s3.ap-northeast-2.amazonaws.com/;")
			
				
		http
//			.cors()
//			.and()
        	.csrf()
        		.ignoringAntMatchers("/ticketing/payResult")
        		.ignoringAntMatchers("/reserverAuthentication/add")
        		.ignoringAntMatchers("/reserverAuthentication/success")
        		.ignoringAntMatchers("/reserverAuthentication/fail")        
        		.ignoringAntMatchers("/reserverAuthentication/noSchedule/checkReservationSuccess")    
        		.ignoringAntMatchers("/reserverAuthentication/noSchedule/checkReservationFail") 
        	.and()        	
        	.authorizeRequests()
        	.antMatchers("/").permitAll()
        	.antMatchers("/**").permitAll()
        	.and()
//        	.formLogin().loginPage("/ticketadm/login")
//        				.loginProcessingUrl("/ticketadm/authenticateMember")
//        				.successHandler(loginAuthenticationSuccessHandler)
//        				.failureHandler(loginAuthenticationFailureHandler)
//        				.permitAll()
//        	.and()
//        	.logout()
//        		.logoutUrl("/ticketadm/logout")
//        		.clearAuthentication(true)
//        		.logoutSuccessUrl("/ticketadm/login")        		
//        		.invalidateHttpSession(true)
//        		.deleteCookies("JSESSIONID")
//        		.permitAll()
//        	.and()
//            .exceptionHandling().accessDeniedPage("/ticketadm/accessDenied")
//        	.and()
        	.sessionManagement()        		
//    			.invalidSessionUrl("/sessionError")
        		.sessionFixation().changeSessionId()
        		.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        		.maximumSessions(100)
        		.maxSessionsPreventsLogin(false);
//        		.expiredUrl("/sessionError");
//        		.sessionRegistry(sessionRegistry());
        
    }
	
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("https://jbimweb.s3.ap-northeast-2.amazonaws.com/"));
//		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    source.registerCorsConfiguration("/**", configuration);
//	    return source;
//	}
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TaskScheduler taskScheduler() {
    	return new ConcurrentTaskScheduler();
    }

}
