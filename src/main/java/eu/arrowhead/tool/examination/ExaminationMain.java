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
import org.springframework.http.ResponseEntity;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.core.CoreSystem;
import eu.arrowhead.common.dto.shared.SystemResponseDTO;
import eu.arrowhead.tool.examination.config.ExaminationHttpService;
import eu.arrowhead.tool.examination.config.HttpActor;
import eu.arrowhead.tool.examination.controller.sysop_dto.SystemListResponseDTO;
import eu.arrowhead.tool.examination.util.ExminationUtil;
import eu.arrowhead.tool.examination.util.MgmtUri;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE})
public class ExaminationMain implements ApplicationRunner {
	
	@Autowired
	private ExaminationHttpService httpService;
	
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
			httpService.sendRequest(HttpActor.SYSTEM_OPERATOR, Utilities.createURI(ExminationUtil.getUriScheme(sslEnabled), serviceReqistryAddress, serviceRegistryPort, CommonConstants.SERVICE_REGISTRY_URI + CommonConstants.ECHO_URI), HttpMethod.GET, String.class);
			logger.info(CoreSystem.SERVICE_REGISTRY.name() + " Core System is reachable on: " + serviceReqistryAddress + ":" + serviceRegistryPort);
		} catch (final Exception ex) {
			logger.info(CoreSystem.SERVICE_REGISTRY.name() + " Core System is not reachable on: " + serviceReqistryAddress + ":" + serviceRegistryPort);
			logger.debug(ex.getMessage());
		}
		
		final ResponseEntity<SystemListResponseDTO> systemListDTO = httpService.sendRequest(HttpActor.SYSTEM_OPERATOR, Utilities.createURI(ExminationUtil.getUriScheme(sslEnabled), serviceReqistryAddress, serviceRegistryPort, MgmtUri.GET_SYSTEMS), HttpMethod.GET, SystemListResponseDTO.class);
		for (final SystemResponseDTO system : systemListDTO.getBody().getData()) {
			for (final CoreSystem	coreSystem : CoreSystem.values()) {
				if (system.getSystemName().equalsIgnoreCase(coreSystem.name()) && !system.getSystemName().equalsIgnoreCase(CoreSystem.SERVICE_REGISTRY.name())) {
					final String address = system.getAddress();
					final int port = system.getPort();
					logger.info(coreSystem.name() + " is registered");
					try {
						httpService.sendRequest(HttpActor.SYSTEM_OPERATOR, Utilities.createURI(ExminationUtil.getUriScheme(sslEnabled), address, port, ExminationUtil.getCoreSystemUri(coreSystem) + CommonConstants.ECHO_URI), HttpMethod.GET, String.class);
						logger.info(coreSystem.name() + " Core System is reachable on: " + address + ":" + port);
					} catch (final Exception ex) {
						logger.info(coreSystem.name() + " Core System is not reachable on: " + address + ":" + port);
						logger.debug(ex.getMessage());
					}
				}
			}
		}
	}
}