package eu.arrowhead.tool.examination.util;

import eu.arrowhead.common.CommonConstants;

public class ExminationUtil {
	
	//=================================================================================================
	// members
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static String getUriScheme(final boolean sslEnabled) {
		return sslEnabled ? CommonConstants.HTTPS : CommonConstants.HTTP;
	}

}
