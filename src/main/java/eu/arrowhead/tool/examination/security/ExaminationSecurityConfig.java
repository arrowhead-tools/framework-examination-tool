package eu.arrowhead.tool.examination.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import eu.arrowhead.common.CommonConstants;

@Configuration
@EnableWebSecurity
public class ExaminationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	//=================================================================================================
	// members

	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;
	
	//=================================================================================================
	// assistant methods

    //-------------------------------------------------------------------------------------------------
	@Override
    protected void configure(final HttpSecurity http) throws Exception {
    	http.httpBasic().disable()
    	    .csrf().disable()
    	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and();
    	if (sslEnabled) {
    		http.requiresChannel().anyRequest().requiresSecure();
    	}    	
    }
}
