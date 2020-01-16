package eu.arrowhead.tool.examination.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.util.Assert;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.core.CoreSystem;
import eu.arrowhead.common.dto.shared.ServiceRegistryRequestDTO;
import eu.arrowhead.common.dto.shared.ServiceSecurityType;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
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

	//-------------------------------------------------------------------------------------------------
	public static SystemRequestDTO generateSystemRequestDTO() {
		final SystemRequestDTO systemRequestDTO = new SystemRequestDTO();
		systemRequestDTO.setSystemName("examination" + new Random(System.currentTimeMillis()).nextInt());
		systemRequestDTO.setAddress( getRandomNumber(0, 100) + "." + getRandomNumber(0, 100) + "." + getRandomNumber(0, 100) + "." + getRandomNumber(0, 100));
		systemRequestDTO.setPort(getRandomNumber(1000, 65535));
		systemRequestDTO.setAuthenticationInfo("EXAMINATION" + getRandomNumber(1000000, 100000000));
		return systemRequestDTO;
	}
	
	//-------------------------------------------------------------------------------------------------
	public static ServiceRegistryRequestDTO generateServiceRegistryRequestDTO(final SystemRequestDTO system) {
		final ServiceRegistryRequestDTO serviceRegistryRequestDTO = new ServiceRegistryRequestDTO();
		serviceRegistryRequestDTO.setServiceDefinition("examination-service-" + getRandomNumber(1000000, 100000000));
		serviceRegistryRequestDTO.setProviderSystem(system);
		serviceRegistryRequestDTO.setServiceUri("/examination/" + getRandomNumber(1000000, 100000000));
		serviceRegistryRequestDTO.setSecure(ServiceSecurityType.CERTIFICATE);
		final Map<String, String> metadata = new HashMap<>();
		metadata.put("examination-key", String.valueOf(getRandomNumber(1000, 100000)));
		serviceRegistryRequestDTO.setMetadata(metadata);
		final List<String> interfaces = new ArrayList<>();
		interfaces.add("HTTPS-SECURE-JSON");
		serviceRegistryRequestDTO.setInterfaces(interfaces);
		return serviceRegistryRequestDTO;
	}
	
	//-------------------------------------------------------------------------------------------------
	public static int getRandomNumber(final int min, final int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		final Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
