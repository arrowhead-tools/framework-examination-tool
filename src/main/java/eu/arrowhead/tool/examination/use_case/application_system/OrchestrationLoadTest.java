package eu.arrowhead.tool.examination.use_case.application_system;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.dto.shared.ServiceRegistryRequestDTO;
import eu.arrowhead.common.dto.shared.ServiceRegistryResponseDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.dto.shared.SystemResponseDTO;
import eu.arrowhead.tool.examination.config.CoreSystems;
import eu.arrowhead.tool.examination.config.HttpActor;
import eu.arrowhead.tool.examination.controller.dto.AuthorizationIntraCloudCheckResponseDTO;
import eu.arrowhead.tool.examination.controller.dto.AuthorizationIntraCloudRequestDTO;
import eu.arrowhead.tool.examination.use_case.ApplicationSystemUseCase;
import eu.arrowhead.tool.examination.util.ExminationUtil;
import eu.arrowhead.tool.examination.util.MgmtUri;

@Component
public class OrchestrationLoadTest extends ApplicationSystemUseCase {
	
	//=================================================================================================
	// members
	
	@Value("${client_system_name}")
	private String conumerSystemName;
	
	private final int numberOfProviderWithSameService = 100;
	private final int numberOfOrchestrationRequestToBeSent = 1000;
	private final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfOrchestrationRequestToBeSent);
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void runUseCase() {
		
		//Register consumer
		final SystemRequestDTO consumerRequest = ExminationUtil.generateSystemRequestDTO();
		consumerRequest.setSystemName(conumerSystemName);
		final ResponseEntity<SystemResponseDTO> consumerResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SYSTEMS), HttpMethod.POST, SystemResponseDTO.class, consumerRequest);
		
		//Register providers
		final ServiceRegistryRequestDTO serviceRegistryRequest = ExminationUtil.generateServiceRegistryRequestDTO(ExminationUtil.generateSystemRequestDTO());
		final List<Long> registeredProviderIDs = new ArrayList<>();
		long serviceDefinitionID = 0;
		long interfaceID = 0;
		for (int i = 0; i < numberOfProviderWithSameService; ++i) {
			final SystemRequestDTO systemRequest = ExminationUtil.generateSystemRequestDTO();
			serviceRegistryRequest.setProviderSystem(systemRequest);
			final ResponseEntity<SystemResponseDTO> systemResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SYSTEMS), HttpMethod.POST, SystemResponseDTO.class, systemRequest);
			final ResponseEntity<ServiceRegistryResponseDTO> serviceRegistryResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT), HttpMethod.POST, ServiceRegistryResponseDTO.class, serviceRegistryRequest);
			registeredProviderIDs.add(systemResponse.getBody().getId());
			serviceDefinitionID = serviceRegistryResponse.getBody().getServiceDefinition().getId();	
			interfaceID = serviceRegistryResponse.getBody().getInterfaces().iterator().next().getId();
		}
		
		//Create authorization rules
		final AuthorizationIntraCloudRequestDTO authRequest = new AuthorizationIntraCloudRequestDTO(consumerResponse.getBody().getId(), registeredProviderIDs, List.of(serviceDefinitionID), List.of(interfaceID));
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getAuthorizationUri(MgmtUri.AUTHORIZATION_MGMT_INTRACLOUD), HttpMethod.POST, AuthorizationIntraCloudCheckResponseDTO.class, authRequest);
		
		//Run load test
		final ServiceQueryFormDTO serviceQueryFormDTO = new ServiceQueryFormDTO.Builder(serviceRegistryRequest.getServiceDefinition()).interfaces(serviceRegistryRequest.getInterfaces().get(0)).build();
		final OrchestrationFormRequestDTO orchestrationFormRequestDTO = new OrchestrationFormRequestDTO.Builder(consumerRequest).requestedService(serviceQueryFormDTO).build();
		for (int i = 0; i < numberOfOrchestrationRequestToBeSent; i++) {
			threadPool.execute(new AsyncRequest(orchestrationFormRequestDTO));
		}
		
		//TODO wait for all the orchestration done
		
		//Delete consumer
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SYSTEMS + "/" + String.valueOf(consumerResponse.getBody().getId())), HttpMethod.DELETE, Void.class);
		
		//Delete providers
		for (final Long id : registeredProviderIDs) {
			request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SYSTEMS + "/" + String.valueOf(id)), HttpMethod.DELETE, Void.class);
		}
		
		//Delete service definition
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_MGMT_SERVICES + "/" + String.valueOf(serviceDefinitionID)), HttpMethod.DELETE, Void.class);
		
	}
	
	//=================================================================================================
	// nested class
	
	private class AsyncRequest implements Runnable {
		
		//=================================================================================================
		// members
		
		private final OrchestrationFormRequestDTO orchestrationRequest;
		
		private AsyncRequest(final OrchestrationFormRequestDTO orchestrationRequest) {
			this.orchestrationRequest = orchestrationRequest;
		}
		
		//=================================================================================================
		// methods
	
		//-------------------------------------------------------------------------------------------------
		@Override
		public void run() {
			request(HttpActor.APPLICATION_SYSTEM, CoreSystems.getOrchestratorUri(MgmtUri.ORCHESTRATOR_ORCHESTRATION), HttpMethod.POST, OrchestrationResponseDTO.class, orchestrationRequest);			
		}		
	}

}
