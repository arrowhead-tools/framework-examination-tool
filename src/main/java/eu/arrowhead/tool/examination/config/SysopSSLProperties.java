package eu.arrowhead.tool.examination.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.CommonConstants;

@Component
public class SysopSSLProperties {
	
	//=================================================================================================
	// members
	
	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;
	
	@Value("${sysop.server.ssl.key-store-type}")
	private String keyStoreType;
	
	@Value("${sysop.server.ssl.key-store}")
	private Resource keyStore;
	
	@Value("${sysop.server.ssl.key-store-password}")
	private String keyStorePassword;
	
	@Value("${sysop.server.ssl.key-password}")
	private String keyPassword;
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public boolean isSslEnabled() { return sslEnabled; }
	public String getKeyStoreType() { return keyStoreType; }
	public Resource getKeyStore() { return keyStore; }
	public String getKeyStorePassword() { return keyStorePassword; }
	public String getKeyPassword() { return keyPassword; }
}