package eu.arrowhead.tool.examination.use_case.system_operator;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.dto.shared.ServiceRegistryRequestDTO;
import eu.arrowhead.common.dto.shared.ServiceRegistryResponseDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.dto.shared.SystemResponseDTO;
import eu.arrowhead.tool.examination.config.CoreSystems;
import eu.arrowhead.tool.examination.config.HttpActor;
import eu.arrowhead.tool.examination.use_case.SystemOperatorUseCase;
import eu.arrowhead.tool.examination.util.ExminationUtil;
import eu.arrowhead.tool.examination.util.MgmtUri;

@Component
public class RegisterProviderWithService extends SystemOperatorUseCase {
	
	//=================================================================================================
	// members

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void start() {
		SystemRequestDTO providerDTO = ExminationUtil.generateSystemRequestDTO();
		ServiceRegistryRequestDTO serviceRegistryRequestDTO = ExminationUtil.generateServiceRegistryRequestDTO(providerDTO);
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY_SYSTEMS), HttpMethod.POST, SystemResponseDTO.class, providerDTO);
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getServiceRegistryUri(MgmtUri.SERVICE_REGISTRY), HttpMethod.POST, ServiceRegistryResponseDTO.class, serviceRegistryRequestDTO);
	}
}