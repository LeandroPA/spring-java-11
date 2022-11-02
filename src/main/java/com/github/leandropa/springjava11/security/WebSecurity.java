package com.github.leandropa.springjava11.security;

import com.github.leandropa.springjava11.filter.JWTAuthorizationFilter;
import com.github.leandropa.springjava11.util.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

//	@Resource
//	private UserDetailsService userDetailsService;

	@Resource
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("Leandro")
				.password(bCryptPasswordEncoder.encode("123456789"))
				.roles("admin_role")
				.and()
				.passwordEncoder(bCryptPasswordEncoder);
//		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		System.out.println("WebSecurity::configure::HttpSecurity");

		http.cors()
				.and()
				.csrf().disable()
				.authorizeRequests().antMatchers(SecurityConstants.SIGN_UP_URL).permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(new JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().authenticationEntryPoint( (req, res, ex) ->
						res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage())
				);

//		http.csrf().disable()
//				.authorizeRequests().antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
//				.anyRequest().authenticated()
//				.and()
////				.addFilter(new JWTAuthenticationFilter(authenticationManager()))
//				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		http.cors().and().authorizeRequests()
//				.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
//				.anyRequest().authenticated()
//				.and()
////				.addFilter(new JWTAuthenticationFilter(authenticationManager()))
//				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
//		source.registerCorsConfiguration("/**", corsConfiguration);
//
//		return source;
//	}

	@Bean
	public CorsFilter corsFilter() {

		UrlBasedCorsConfigurationSource source =
				new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}


}
