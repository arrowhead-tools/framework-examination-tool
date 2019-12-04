package eu.arrowhead.tool.examination;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.http.HttpService;
import eu.arrowhead.tool.examination.util.ExminationUtil;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE})
public class ExaminationMain implements ApplicationRunner {
	
	@Autowired
	private HttpService httpService;
	
	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;
	
	@Value(CommonConstants.$SERVICE_REGISTRY_ADDRESS_WD)
	private String serviceReqistryAddress;
	
	@Value(CommonConstants.$SERVICE_REGISTRY_PORT_WD)
	private int serviceRegistryPort;
	
	private final Logger logger = LogManager.getLogger(ExaminationMain.class);

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(ExaminationMain.class, args);
	}	
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void run(final ApplicationArguments args) throws Exception {
		checkCoreSystems();
	}
	
	//=================================================================================================
	// assistant methods
	
	private void checkCoreSystems() {
		try {
			httpService.sendRequest(Utilities.createURI(ExminationUtil.getUriScheme(sslEnabled), serviceReqistryAddress, serviceRegistryPort, CommonConstants.SERVICE_REGISTRY_URI + CommonConstants.ECHO_URI), HttpMethod.GET, String.class);
			logger.info("Service Registry Core System is reachable on: " + serviceReqistryAddress + ":" + serviceRegistryPort);
		} catch (Exception ex) {
			logger.info("Service Registry Core System is not reachable on: " + serviceReqistryAddress + ":" + serviceRegistryPort);
			logger.debug(ex.getMessage());
		}
		
	}
}
