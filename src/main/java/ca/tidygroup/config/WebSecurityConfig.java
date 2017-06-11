package ca.tidygroup.config;


import ca.tidygroup.model.Role;
import ca.tidygroup.service.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private SecurityUserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(SecurityUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.toString())
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?error")
                .usernameParameter("login").passwordParameter("password")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .csrf();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
