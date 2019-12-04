package eu.arrowhead.tool.examination.config;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.exception.AuthException;

@Component
public class ExaminationApplicationListener {

	//=================================================================================================
	// members
	
	@Autowired
	protected SSLProperties sslProperties;
	
	@Autowired
	protected SysopSSLProperties sysopSSLProperties;
	
	protected final Logger logger = LogManager.getLogger(ExaminationApplicationListener.class);
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Bean(CommonConstants.ARROWHEAD_CONTEXT)
	public Map<String,Object> getArrowheadContext() {
		return new ConcurrentHashMap<>();
	}
	
	//-------------------------------------------------------------------------------------------------
	@EventListener
	@Order(10)
	public void onApplicationEvent(final ContextRefreshedEvent event) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, InterruptedException {
		logger.info("Security mode: {}", getModeString());
		
		if (sslProperties.isSslEnabled()) {
			final KeyStore keyStore = initializeKeyStore();
			checkServerCertificate(keyStore, event.getApplicationContext(), false);
			obtainKeys(keyStore, event.getApplicationContext(), false);
			final KeyStore sysopKeyStore = initializeKeyStoreSysop();
			checkServerCertificate(sysopKeyStore, event.getApplicationContext(), true);
			obtainKeys(sysopKeyStore, event.getApplicationContext(), true);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	@PreDestroy
	public void destroy() throws InterruptedException {
		
	}	

	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	protected String getModeString() {
		return sslProperties.isSslEnabled() ? "SECURED" : "NOT SECURED";
	}
	
	//-------------------------------------------------------------------------------------------------
	private KeyStore initializeKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		logger.debug("initializeKeyStore started...");
		Assert.isTrue(sslProperties.isSslEnabled(), "SSL is not enabled.");
		final String messageNotDefined = " is not defined.";
		Assert.isTrue(!Utilities.isEmpty(sslProperties.getKeyStoreType()), CommonConstants.KEYSTORE_TYPE + messageNotDefined);
		Assert.notNull(sslProperties.getKeyStore(), CommonConstants.KEYSTORE_PATH + messageNotDefined);
		Assert.isTrue(sslProperties.getKeyStore().exists(), CommonConstants.KEYSTORE_PATH + " file is not found.");
		Assert.notNull(sslProperties.getKeyStorePassword(), CommonConstants.KEYSTORE_PASSWORD + messageNotDefined);
		
		final KeyStore keystore = KeyStore.getInstance(sslProperties.getKeyStoreType());
		keystore.load(sslProperties.getKeyStore().getInputStream(), sslProperties.getKeyStorePassword().toCharArray());

		return keystore;
	}
	
	//-------------------------------------------------------------------------------------------------
	private KeyStore initializeKeyStoreSysop() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		logger.debug("initializeKeyStore started...");
		Assert.isTrue(sysopSSLProperties.isSslEnabled(), "SSL is not enabled.");
		final String messageNotDefined = " is not defined.";
		Assert.isTrue(!Utilities.isEmpty(sysopSSLProperties.getKeyStoreType()), "Sysop" + CommonConstants.KEYSTORE_TYPE + messageNotDefined);
		Assert.notNull(sysopSSLProperties.getKeyStore(), "sysop." + CommonConstants.KEYSTORE_PATH + messageNotDefined);
		Assert.isTrue(sysopSSLProperties.getKeyStore().exists(), "sysop." + CommonConstants.KEYSTORE_PATH + " file is not found.");
		Assert.notNull(sysopSSLProperties.getKeyStorePassword(), "sysop." + CommonConstants.KEYSTORE_PASSWORD + messageNotDefined);
		
		final KeyStore keystore = KeyStore.getInstance(sysopSSLProperties.getKeyStoreType());
		keystore.load(sysopSSLProperties.getKeyStore().getInputStream(), sysopSSLProperties.getKeyStorePassword().toCharArray());

		return keystore;
	}
	
	//-------------------------------------------------------------------------------------------------
	private void checkServerCertificate(final KeyStore keyStore, final ApplicationContext appContext, final boolean isSysop) {
		logger.debug("checkServerCertificate started...");
		final X509Certificate serverCertificate = Utilities.getFirstCertFromKeyStore(keyStore);
		final String serverCN = Utilities.getCertCNFromSubject(serverCertificate.getSubjectDN().getName());
		if (!Utilities.isKeyStoreCNArrowheadValid(serverCN)) {
			logger.info("Client CN ({}) is not compliant with the Arrowhead certificate structure, since it does not have 5 parts, or does not end with \"arrowhead.eu\".", serverCN);
			throw new AuthException("Server CN (" + serverCN + ") is not compliant with the Arrowhead certificate structure, since it does not have 5 parts, or does not end with \"arrowhead.eu\".");
		}
		logger.info("Client CN: {}", serverCN);
		
		@SuppressWarnings("unchecked")
		final Map<String,Object> context = appContext.getBean(CommonConstants.ARROWHEAD_CONTEXT, Map.class);
		if (isSysop) {
			context.put("sysop." + CommonConstants.SERVER_COMMON_NAME, serverCN);
		} else {
			context.put(CommonConstants.SERVER_COMMON_NAME, serverCN);			
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private void obtainKeys(final KeyStore keyStore, final ApplicationContext appContext, final boolean isSysop) {
		logger.debug("obtainKeys started...");
		@SuppressWarnings("unchecked")
		final Map<String,Object> context = appContext.getBean(CommonConstants.ARROWHEAD_CONTEXT, Map.class);
		
		if (isSysop) {
			context.put("sysop." + CommonConstants.SERVER_PUBLIC_KEY, Utilities.getFirstCertFromKeyStore(keyStore).getPublicKey());		
			final PrivateKey privateKey = Utilities.getPrivateKey(keyStore, sysopSSLProperties.getKeyPassword());
			context.put("sysop." + CommonConstants.SERVER_PRIVATE_KEY, privateKey);
		} else {
			context.put(CommonConstants.SERVER_PUBLIC_KEY, Utilities.getFirstCertFromKeyStore(keyStore).getPublicKey());		
			final PrivateKey privateKey = Utilities.getPrivateKey(keyStore, sslProperties.getKeyPassword());
			context.put(CommonConstants.SERVER_PRIVATE_KEY, privateKey);
		}		
	}
	
}
