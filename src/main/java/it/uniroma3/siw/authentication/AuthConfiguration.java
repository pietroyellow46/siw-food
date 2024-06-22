package it.uniroma3.siw.authentication;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.model.Credentials.DEFAULT_ROLE;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
	@Autowired
	private DataSource dataSource;

	//specifichi come ricavare le credenziali dell utente
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
		.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}

	//cripta password
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	//configuri permessi sulle richieste
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) 
			throws Exception{
		httpSecurity.csrf().and().cors().disable()
		.authorizeHttpRequests()
		// .requestMatchers("/**").permitAll()

		//cosa accede chi non loggato
		.requestMatchers(HttpMethod.GET,"/","static/images/logowhite.png","static/images/logoblack.png","static/images/logofull.png","/static/images/account_circle_24dp_FILL0_wght400_GRAD0_opsz24.png","/static/images/search1.png","/index","/register","/css/**", "/images/**", "favicon.ico", "/recipe/**", "/ingredient/**","/allChef/**","/searchRecipe/**","/searchIngredient/**", "/searchChef/**", "homepage", "/login").permitAll()
		.requestMatchers(HttpMethod.POST,"/register", "/login","/searchRecipe","/searchIngredient", "/searchChef").permitAll()

		//cosa accede chi loggato o admin o chef normale
		.requestMatchers(HttpMethod.GET,"/chef/**").hasAnyAuthority(DEFAULT_ROLE, ADMIN_ROLE)
		.requestMatchers(HttpMethod.POST,"/chef/**").hasAnyAuthority(DEFAULT_ROLE, ADMIN_ROLE)

		//cosa accede admin
		.requestMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority(ADMIN_ROLE)
		.requestMatchers(HttpMethod.POST,"/admin/**").hasAnyAuthority(ADMIN_ROLE)

		//tutte le altre richieste basta che sei autenticato 
		.anyRequest().authenticated()

		// login a /login, permesso a tutti, se va bene /success senno /login?error=true
		.and().formLogin()
		.loginPage("/login")
		.permitAll()
		.defaultSuccessUrl("/success", true)
		.failureUrl("/login?error=true")

		//per fare logout fai get a /logout, ti ritorna a / se va bene
		.and()
		.logout()
		.logoutUrl("/logout")
		// in caso di successo, si viene reindirizzati alla home
		.logoutSuccessUrl("/")
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID")
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.clearAuthentication(true).permitAll();
		return httpSecurity.build();
	}
}