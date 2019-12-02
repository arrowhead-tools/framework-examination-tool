package eu.arrowhead.tool.examination.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import eu.arrowhead.client.library.config.DefaultSecurityConfig;

@Configuration
@EnableWebSecurity
public class ExaminationSecurityConfig extends DefaultSecurityConfig {
	
	//=================================================================================================
	// methods

    //-------------------------------------------------------------------------------------------------
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
	}
}
