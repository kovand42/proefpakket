package be.vdab.proefpakket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMINISTRATOR = "administrator";
/*    @Bean
    JdbcDaoImpl jdbcDaoImpl(DataSource dataSource){
        JdbcDaoImpl impl = new JdbcDaoImpl();
        impl.setDataSource(dataSource);
        return impl;
    }*/
    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.builder().username("administrator").password("{noop}theboss")
                        .authorities(ADMINISTRATOR).build());
    }
    @Override
    public void configure (WebSecurity web) throws Exception{
        web.ignoring()
                .mvcMatchers("/images/**")
                .mvcMatchers("/css/**")
                .mvcMatchers("/scripts/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and().authorizeRequests().mvcMatchers("/brouwers/*/ondernemingsnr")
                .hasAuthority(ADMINISTRATOR);
    }
}
