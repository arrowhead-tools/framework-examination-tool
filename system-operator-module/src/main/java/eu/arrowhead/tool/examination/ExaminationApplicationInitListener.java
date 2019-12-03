package eu.arrowhead.tool.examination;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import eu.arrowhead.client.library.config.ApplicationInitListener;
import eu.arrowhead.common.CommonConstants;

@Component
public class ExaminationApplicationInitListener extends ApplicationInitListener {
	
	//=================================================================================================
	// members
	
	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;
	
	@Value(CommonConstants.$SERVICE_REGISTRY_ADDRESS_WD)
	private String serviceReqistryAddress;
	
	@Value(CommonConstants.$SERVICE_REGISTRY_PORT_WD)
	private int serviceRegistryPort;
	
	private final Logger logger = LogManager.getLogger(ExaminationApplicationInitListener.class);
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Override
	protected void customInit(final ContextRefreshedEvent event) {
		logger.info("Examination Tool System Operator Module has been started");
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void customDestroy() {
		logger.info("Shutting down Examination Tool System Operator Module");
	}
}
