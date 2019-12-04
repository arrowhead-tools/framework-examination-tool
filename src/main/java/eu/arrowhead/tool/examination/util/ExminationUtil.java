package eu.arrowhead.tool.examination.util;

import org.springframework.util.Assert;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.core.CoreSystem;
import eu.arrowhead.common.exception.InvalidParameterException;

public class ExminationUtil {
	
	//=================================================================================================
	// members
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static String getUriScheme(final boolean sslEnabled) {
		return sslEnabled ? CommonConstants.HTTPS : CommonConstants.HTTP;
	}
	
	//-------------------------------------------------------------------------------------------------
	public static String getCoreSystemUri(final CoreSystem coreSystem) {
		Assert.notNull(coreSystem, "coreSystem is null");
		switch (coreSystem) {
		case SERVICE_REGISTRY:
			return CommonConstants.SERVICE_REGISTRY_URI;
		case AUTHORIZATION:
			return CommonConstants.AUTHORIZATION_URI;
		case ORCHESTRATOR:
			return CommonConstants.ORCHESTRATOR_URI;
		case GATEKEEPER:
			return CommonConstants.GATEKEEPER_URI;
		case GATEWAY:
			return CommonConstants.GATEWAY_URI;
		case EVENT_HANDLER:
			return CommonConstants.EVENT_HANDLER_URI;
		case CHOREOGRAPHER:
			return CommonConstants.CHOREOGRAPHER_URI;
		default:
			throw new InvalidParameterException(coreSystem + " not known");
		}
	}

}
