package eu.arrowhead.tool.examination.config;

import org.springframework.web.util.UriComponents;

import eu.arrowhead.common.Utilities;
import eu.arrowhead.tool.examination.util.ExminationUtil;

public class CoreSystems {
	
	//=================================================================================================
	// members

	public static boolean sslEnabled;
	
	public static String serviceRegistryAddress;
	public static int serviceRegistryPort;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public static UriComponents gerServiceRegistryUri(final String path) {
		return Utilities.createURI(ExminationUtil.getUriScheme(sslEnabled), serviceRegistryAddress, serviceRegistryPort, path);
	}
}
