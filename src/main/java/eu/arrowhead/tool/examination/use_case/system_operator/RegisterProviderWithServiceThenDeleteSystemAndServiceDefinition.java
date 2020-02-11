package eu.arrowhead.tool.examination.use_case.system_operator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.core.CoreSystem;
import eu.arrowhead.common.dto.shared.ServiceDefinitionResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceRegistryRequestDTO;
import eu.arrowhead.common.dto.shared.ServiceRegistryResponseDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.dto.shared.SystemResponseDTO;
import eu.arrowhead.common.exception.InvalidParameterException;
import eu.arrowhead.tool.examination.config.CoreSystems;
import eu.arrowhead.tool.examination.config.HttpActor;
import eu.arrowhead.tool.examination.use_case.SystemOperatorUseCase;
import eu.arrowhead.tool.examination.util.ExaminationUtil;
import eu.arrowhead.tool.examination.util.MgmtUri;

@Component
public class RegisterProviderWithServiceThenDeleteSystemAndServiceDefinition extends SystemOperatorUseCase {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public RegisterProviderWithServiceThenDeleteSystemAndServiceDefinition() {
		super(true, CoreSystem.SERVICE_REGISTRY);
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void runUseCase() {
		final SystemRequestDTO providerRequest = ExaminationUtil.generateSystemRequestDTO();
		final ServiceRegistryRequestDTO serviceRegistryRequest = ExaminationUtil.generateServiceRegistryRequestDTO(providerRequest);
		
		final ResponseEntity<SystemResponseDTO> providerResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SYSTEMS), HttpMethod.POST, SystemResponseDTO.class, providerRequest);
		final ResponseEntity<ServiceRegistryResponseDTO> serviceRegistryResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT), HttpMethod.POST, ServiceRegistryResponseDTO.class, serviceRegistryRequest);
		
		verifySystemResponse(providerRequest, providerResponse.getBody());
		verifyServiceRegistryResponse(serviceRegistryRequest, serviceRegistryResponse.getBody());		
		
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SYSTEMS + "/" + String.valueOf(providerResponse.getBody().getId())), HttpMethod.DELETE, Void.class);
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SERVICES + "/" + String.valueOf(serviceRegistryResponse.getBody().getServiceDefinition().getId())), HttpMethod.DELETE, Void.class);

		verifyDeleteRequests(providerResponse.getBody(), serviceRegistryResponse.getBody());
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void verifySystemResponse(final SystemRequestDTO request, final SystemResponseDTO response) {
		assertNotNull(response, "SystemResponse cannot be null");
		assertNotNull(response.getId(), "Provider id have to be defined in the reponse");
		assertEqualsIgnoreCaseWithTrim(request.getSystemName(), response.getSystemName(), "Provider name in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(request.getAddress(), response.getAddress(), "Provider address in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getPort()), String.valueOf(response.getPort()), "Provider port in request and in response must be the same");
		if (request.getAuthenticationInfo() != null && !request.getAuthenticationInfo().isBlank()) {
			assertEqualsIgnoreCaseWithTrim(request.getAuthenticationInfo(), response.getAuthenticationInfo(), "Provider auth info in request and in response must be the same");
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private void verifyServiceRegistryResponse(final ServiceRegistryRequestDTO request, final ServiceRegistryResponseDTO response) {
		assertNotNull(response, "ServiceRegistryResponse cannot be null");
		assertNotNull(response.getId(), "SREntry id have to be defined in the reponse");
		
		assertEqualsIgnoreCaseWithTrim(request.getProviderSystem().getSystemName(), response.getProvider().getSystemName(), "Provider name in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(request.getProviderSystem().getAddress(), response.getProvider().getAddress(), "Provider address in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getProviderSystem().getPort()), String.valueOf(response.getProvider().getPort()), "Provider port in request and in response must be the same");
		if (request.getProviderSystem().getAuthenticationInfo() != null && !request.getProviderSystem().getAuthenticationInfo().isBlank()) {
			assertEqualsIgnoreCaseWithTrim(request.getProviderSystem().getAuthenticationInfo(), response.getProvider().getAuthenticationInfo(), "Provider auth info in request and in response must be the same");
		}
		
		assertEqualsIgnoreCaseWithTrim(request.getServiceDefinition(), response.getServiceDefinition().getServiceDefinition(), "ServiceDefinition in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(request.getServiceUri(), response.getServiceUri(), "ServiceURI in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(request.getSecure().name(), response.getSecure().name(), "Security level in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getVersion() == null ? 1 : request.getVersion()), String.valueOf(response.getVersion()), "Version of interface list in request and in response must be the same or 1 if it was not defined");
		if (request.getEndOfValidity() != null && !request.getEndOfValidity().isBlank()) {
			assertEqualsIgnoreCaseWithTrim(request.getEndOfValidity(), response.getEndOfValidity(), "EndOfValidity in request and in response must be the same");
		}
		
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getInterfaces().size()), String.valueOf(response.getInterfaces().size()), "Size of interface list in request and in response must be the same");		
		final List<String> responseInterfaceNames = new ArrayList<>();
		for (final ServiceInterfaceResponseDTO interface_ : response.getInterfaces()) {
			responseInterfaceNames.add(interface_.getInterfaceName().toUpperCase().trim());
		}
		assertEqualsStringLists(request.getInterfaces(), responseInterfaceNames, "Interface list content in request and in response must be the same");
		
		if (request.getMetadata() != null) {			
			assertEqualsStringStringMap(request.getMetadata(), response.getMetadata(), "Metadata content in request and in response must be the same");
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private void verifyDeleteRequests(final SystemResponseDTO systemResponse, final ServiceRegistryResponseDTO serviceRegistryResponse) {
		Exception actualException1 = null;
		try {			
			request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SYSTEMS + "/" + String.valueOf(systemResponse.getId())), HttpMethod.GET, SystemResponseDTO.class);
		} catch (final Exception ex) {
			actualException1 = ex;
		} finally {
			assertExpectException(InvalidParameterException.class, actualException1, "No system id should exists after delete the provider");			
		}
		
		Exception actualException2 = null;
		try {			
			request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT + "/" + String.valueOf(serviceRegistryResponse.getId())), HttpMethod.GET, ServiceRegistryResponseDTO.class);
		} catch (final Exception ex) {
			actualException2 = ex;
		} finally {
			assertExpectException(InvalidParameterException.class, actualException2, "No srEntry id shloud exists after delete the provider system");
		}
		
		Exception actualException3 = null;
		try {			
			request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SERVICES + "/" + String.valueOf(serviceRegistryResponse.getServiceDefinition().getId())), HttpMethod.GET, ServiceDefinitionResponseDTO.class);
		} catch (final Exception ex) {
			actualException3 = ex;
		} finally {
			assertExpectException(InvalidParameterException.class, actualException3, "No service definition id should exists after delete the service definition");
		}
	}
}