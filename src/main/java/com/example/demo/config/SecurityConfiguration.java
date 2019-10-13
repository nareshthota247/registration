package com.example.demo.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.repository.UserRepository;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private WebApplicationContext applicationContext;
//	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private AuthenticationSuccessHandlerImpl successHandler;
	@Autowired
	private DataSource dataSource;
	
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//			.antMatchers("/register").permitAll()
//			.antMatchers("/login").permitAll()
//			.antMatchers("/confirm").permitAll()
//			.antMatchers("/h2-console/**").permitAll();
////			.antMatchers("/h2-console/**").hasAuthority("ROLE_ADMIN").anyRequest().authenticated();
//		
//			http.csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
//            .and().headers().frameOptions().sameOrigin();//allow use of frame to same origin urls
//	}
	



    @PostConstruct
    public void completeSetup() {
        userDetailsService = applicationContext.getBean(CustomUserDetailsService.class);
    }

//    @Autowired
//	public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
//		authenticationMgr.inMemoryAuthentication()
//			.withUser("jduser").password("jdu@123").authorities("ROLE_USER")
//			.and()
//			.withUser("jdadmin").password("jda@123").authorities("ROLE_USER","ROLE_ADMIN");
//	}
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
//        .userDetailsService(userDetailsService)
//            .passwordEncoder(encoder())
//            .and()
            .authenticationProvider(authenticationProvider())
//            .jdbcAuthentication()
//            .dataSource(dataSource)
            ;

//        auth.userDetailsService(userDetailsService)
//        .passwordEncoder(encoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/resources/**","/h2-console/**","/api/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//        .antMatchers("/login").permitAll()
//        .antMatchers("/register").permitAll()
//        .antMatchers("/confirm").permitAll()
//			.antMatchers("/h2-console/**").hasAuthority("ROLE_ADMIN").anyRequest().authenticated()
//            .and()
//            .formLogin()
//            .permitAll()
//            .successHandler(successHandler)
//            .and()
//            .csrf()
//            .disable()
//        ;
        
        http.csrf().disable();
        http.authorizeRequests()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/api/**").permitAll()
//                .antMatchers("/confirm").permitAll()
                .antMatchers("/secure/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().permitAll()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                .and()
                .logout().logoutUrl("/doLogout").logoutSuccessUrl("/logout").permitAll()
                .and()
                .csrf().disable();
                ;
//                .and()
//                .formLogin().loginPage("/login").permitAll();
        
//		http.csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
//        .and().headers().frameOptions().sameOrigin();//allow use of frame to same origin urls
//		http.csrf().ignoringAntMatchers("/api/**")//don't apply CSRF protection to /h2-console
//		.and().headers().frameOptions().sameOrigin();//allow use of frame to same origin urls
        
//		.antMatchers("/register").permitAll()
//		.antMatchers("/login").permitAll()
//		.antMatchers("/confirm").permitAll()
//		.antMatchers("/h2-console/**").permitAll();
//	

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		  auth.inMemoryAuthentication().withUser("ram").password("ram123").roles("ADMIN");
//	} 
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/rest/**").hasAnyRole("ADMIN","USER")
//		.anyRequest().permitAll()
//		.and().formLogin();
//	}
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public ModelMapper modelMapper() {
    	return new ModelMapper();
    }
//    @Bean
//    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
//        return new SecurityEvaluationContextExtension();
//    }

}
