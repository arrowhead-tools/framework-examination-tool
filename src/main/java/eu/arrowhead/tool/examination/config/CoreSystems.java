package eu.arrowhead.tool.examination.config;

import org.springframework.web.util.UriComponents;

import eu.arrowhead.common.Utilities;
import eu.arrowhead.tool.examination.util.ExaminationUtil;

public class CoreSystems {
	
	//=================================================================================================
	// members

	public static boolean sslEnabled;
	
	public static String serviceRegistryAddress;
	public static int serviceRegistryPort;
	
	public static String authorizationAddress;
	public static int authorizationPort;
	
	public static String orchestratorAddress;
	public static int orchestratorPort;
	
	public static String gatekeeperAddress;
	public static int gatekeeperPort;
	
	public static String gatewayAddress;
	public static int gatewayPort;
	
	public static String eventhandlerAddress;
	public static int eventhandlerPort;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public static UriComponents getServiceRegistryUri(final String path) {
		return Utilities.createURI(ExaminationUtil.getUriScheme(sslEnabled), serviceRegistryAddress, serviceRegistryPort, path);
	}
	
	//-------------------------------------------------------------------------------------------------
	public static UriComponents getServiceRegistryUri(final String path, final String... queryParams) {
		return Utilities.createURI(ExaminationUtil.getUriScheme(sslEnabled), serviceRegistryAddress, serviceRegistryPort, path, queryParams);
	}
	
	//-------------------------------------------------------------------------------------------------
	public static UriComponents getAuthorizationUri(final String path) {
		return Utilities.createURI(ExaminationUtil.getUriScheme(sslEnabled), authorizationAddress, authorizationPort, path);
	}
	
	//-------------------------------------------------------------------------------------------------
	public static UriComponents getOrchestratorUri(final String path) {
		return Utilities.createURI(ExaminationUtil.getUriScheme(sslEnabled), orchestratorAddress, orchestratorPort, path);
	}
	
	//-------------------------------------------------------------------------------------------------
	public static UriComponents getGatekeeperUri(final String path) {
		return Utilities.createURI(ExaminationUtil.getUriScheme(sslEnabled), gatekeeperAddress, gatekeeperPort, path);
	}
}
