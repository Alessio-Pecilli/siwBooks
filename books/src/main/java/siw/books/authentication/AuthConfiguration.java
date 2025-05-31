package siw.books.authentication;

import static siw.books.model.Credentials.ADMIN_ROLE;
import static siw.books.model.Credentials.DEFAULT_ROLE;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import siw.books.services.CredentialsService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
				.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}


	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().and().cors().disable().authorizeHttpRequests()
				// Consentiti a tutti (occasionali)
				.requestMatchers(HttpMethod.GET,
    "/", "/index",
    "/libri", "/libri/**",
    "/autori", "/autori/**",
    "/login", "/register",
	"/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/error"
)
				.permitAll().requestMatchers(HttpMethod.POST, "/register", "/login")
				.permitAll()

				// Solo ADMIN_ROLE
				.requestMatchers("/amministratori/**").hasAuthority(ADMIN_ROLE)

				// Solo DEFAULT_ROLE
				.requestMatchers("/utenti/**").hasAuthority(DEFAULT_ROLE)

				// Qualunque altra richiesta: autenticazione
				.anyRequest().authenticated().and().formLogin().loginPage("/login") // Pagina di login di default
																							// per tutti
				.loginProcessingUrl("/login") // URL di submit form login user
				.usernameParameter("username").passwordParameter("pwd")
				.successHandler((request, response, authentication) -> {
	var principal = authentication.getPrincipal();
	Long idUtente = null;
	String username = null;
	if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
		username = userDetails.getUsername();
		idUtente = this.credentialsService.getCredentialsByUsername(username).getUtente().getId();
	}
	boolean isAdmin = this.credentialsService.getCredentialsByUsername(username).getRole().equals(ADMIN_ROLE);
	if (isAdmin) {
		response.sendRedirect(idUtente != null ? "/amministratori/" + idUtente : "/");
	} else {
		response.sendRedirect(idUtente != null ? "/utente/" + idUtente : "/");
	}
}).failureUrl("/login?error=true").permitAll().and().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true).permitAll();
		return httpSecurity.build();
	}

}