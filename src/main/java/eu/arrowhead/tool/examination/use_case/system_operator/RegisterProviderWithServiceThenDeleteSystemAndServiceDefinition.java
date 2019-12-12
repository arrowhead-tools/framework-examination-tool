package eu.arrowhead.tool.examination.use_case.system_operator;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.dto.shared.ServiceDefinitionResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceRegistryRequestDTO;
import eu.arrowhead.common.dto.shared.ServiceRegistryResponseDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.dto.shared.SystemResponseDTO;
import eu.arrowhead.common.exception.InvalidParameterException;
import eu.arrowhead.tool.examination.config.CoreSystems;
import eu.arrowhead.tool.examination.config.HttpActor;
import eu.arrowhead.tool.examination.use_case.SystemOperatorUseCase;
import eu.arrowhead.tool.examination.util.ExminationUtil;
import eu.arrowhead.tool.examination.util.MgmtUri;

@Component
public class RegisterProviderWithServiceThenDeleteSystemAndServiceDefinition extends SystemOperatorUseCase {
	
	//=================================================================================================
	// members

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void runUseCase() {
		final SystemRequestDTO providerDTO = ExminationUtil.generateSystemRequestDTO();
		final ServiceRegistryRequestDTO serviceRegistryRequestDTO = ExminationUtil.generateServiceRegistryRequestDTO(providerDTO);
		final ResponseEntity<SystemResponseDTO> provider = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_SYSTEMS), HttpMethod.POST, SystemResponseDTO.class, providerDTO);
		final ResponseEntity<ServiceRegistryResponseDTO> srEntry = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY), HttpMethod.POST, ServiceRegistryResponseDTO.class, serviceRegistryRequestDTO);
		
		
		assertNotNull(provider, "SystemResponse cannot be null");
		assertNotNull(provider.getBody().getId(), "Provider id have to be defined in the reponse");
		assertEqualsIgnoreCaseWithTrim(providerDTO.getSystemName(), provider.getBody().getSystemName(), "Provider name in DTO and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(providerDTO.getAddress(), provider.getBody().getAddress(), "Provider address in DTO and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(providerDTO.getPort()), String.valueOf(provider.getBody().getPort()), "Provider port in DTO and in response must be the same");
		if (providerDTO.getAuthenticationInfo() != null && !providerDTO.getAuthenticationInfo().isBlank()) {
			assertEqualsIgnoreCaseWithTrim(providerDTO.getAuthenticationInfo(), provider.getBody().getAuthenticationInfo(), "Provider auth info in DTO and in response must be the same");
		}
		
		assertNotNull(srEntry, "ServiceRegistryResponse cannot be null");
		assertNotNull(srEntry.getBody().getId(), "SREntry id have to be defined in the reponse");
		assertEqualsIgnoreCaseWithTrim(serviceRegistryRequestDTO.getProviderSystem().getSystemName(), srEntry.getBody().getProvider().getSystemName(), "Provider name in serviceRegistryRequestDTO and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(serviceRegistryRequestDTO.getProviderSystem().getAddress(), srEntry.getBody().getProvider().getAddress(), "Provider address in serviceRegistryRequestDTO and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(serviceRegistryRequestDTO.getProviderSystem().getPort()), String.valueOf(srEntry.getBody().getProvider().getPort()), "Provider port in serviceRegistryRequestDTO and in response must be the same");
		if (serviceRegistryRequestDTO.getProviderSystem().getAuthenticationInfo() != null && !serviceRegistryRequestDTO.getProviderSystem().getAuthenticationInfo().isBlank()) {
			assertEqualsIgnoreCaseWithTrim(serviceRegistryRequestDTO.getProviderSystem().getAuthenticationInfo(), srEntry.getBody().getProvider().getAuthenticationInfo(), "Provider auth info in serviceRegistryRequestDTO and in response must be the same");
		}
		assertEqualsIgnoreCaseWithTrim(serviceRegistryRequestDTO.getServiceDefinition(), srEntry.getBody().getServiceDefinition().getServiceDefinition(), "ServiceDefinition in serviceRegistryRequestDTO and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(serviceRegistryRequestDTO.getServiceUri(), srEntry.getBody().getServiceUri(), "ServiceURI in serviceRegistryRequestDTO and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(serviceRegistryRequestDTO.getSecure().name(), srEntry.getBody().getSecure().name(), "Security level in serviceRegistryRequestDTO and in response must be the same");
		
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_SYSTEMS + "/" + String.valueOf(provider.getBody().getId())), HttpMethod.DELETE, Void.class);
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_SERVICES + "/" + String.valueOf(srEntry.getBody().getServiceDefinition().getId())), HttpMethod.DELETE, Void.class);
		
		try {			
			final ResponseEntity<SystemResponseDTO> systemResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_SYSTEMS + "/" + String.valueOf(provider.getBody().getId())), HttpMethod.GET, SystemResponseDTO.class);
		} catch (final Exception ex) {
			assertExpectException(InvalidParameterException.class, ex, "No system id should exists after delete the provider");
		}
		
		try {			
			final ResponseEntity<ServiceRegistryResponseDTO> serviceRegistryResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY + "/" + String.valueOf(srEntry.getBody().getId())), HttpMethod.GET, ServiceRegistryResponseDTO.class);
		} catch (final Exception ex) {
			assertExpectException(InvalidParameterException.class, ex, "No srEntry id shloud exists after delete the provider system");
		}
		
		try {			
			final ResponseEntity<ServiceDefinitionResponseDTO> serviceDefResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_SERVICES + "/" + String.valueOf(srEntry.getBody().getServiceDefinition().getId())), HttpMethod.GET, ServiceDefinitionResponseDTO.class);
		} catch (final Exception ex) {
			assertExpectException(InvalidParameterException.class, ex, "No service definition id should exists after delete the service definition");
		}
	}
}