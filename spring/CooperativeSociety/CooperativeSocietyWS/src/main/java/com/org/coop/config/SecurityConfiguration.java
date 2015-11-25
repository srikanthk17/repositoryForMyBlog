package com.org.coop.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.org.coop.service.CustomUserDetailsService;

//public class WebAppInitializer implements WebApplicationInitializer {
//	public void onStartup(ServletContext servletContext) throws ServletException {  
//        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();  
//        ctx.register(WebConfig.class);  
//        ctx.setServletContext(servletContext);    
//        Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));  
//        dynamic.addMapping("/");  
//        dynamic.setLoadOnStartup(1);  
//   }  
//} 

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
	@Autowired
	@Qualifier("customUserDetailsService")
	UserDetailsService customUserDetailsService;
	
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("bill").password("abc123").roles("USER");
//        auth.inMemoryAuthentication().withUser(userName).password("root123").roles(loginService.getRole("ashish", "ashish").toUpperCase());
//        auth.inMemoryAuthentication().withUser("dba").password("root123").roles("ADMIN","DBA");//dba have two roles.
    	
    	auth.userDetailsService(customUserDetailsService);
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
  
      http.authorizeRequests()
        .antMatchers("/", "/home").permitAll() 
        .antMatchers("/admin/**").access("hasRole('ADMIN')")
        .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
        .and().formLogin()
        .and().exceptionHandling().accessDeniedPage("/Access_Denied");
  
    }
}