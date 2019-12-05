package eu.arrowhead.tool.examination.use_case.system_operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.dto.shared.SystemRequestDTO;
import eu.arrowhead.common.dto.shared.SystemResponseDTO;
import eu.arrowhead.tool.examination.config.CoreSystems;
import eu.arrowhead.tool.examination.config.ExaminationHttpService;
import eu.arrowhead.tool.examination.config.HttpActor;
import eu.arrowhead.tool.examination.use_case.SystemOperatorUseCase;
import eu.arrowhead.tool.examination.util.MgmtUri;

@Component
public class RegisterSytems extends SystemOperatorUseCase {
	
	@Autowired
	private ExaminationHttpService httpService;

	@Override
	public void start() {
		SystemRequestDTO systemRequestDTO = new SystemRequestDTO();
		systemRequestDTO.setSystemName("examination-test");
		systemRequestDTO.setAddress("10.10.10.10");
		systemRequestDTO.setPort(8888);
		
		ResponseEntity<SystemResponseDTO> sendRequest = httpService.sendRequest(HttpActor.SYSTEM_OPERATOR, CoreSystems.gerServiceRegistryUri(MgmtUri.REGISTER_SYSTEM), HttpMethod.POST, SystemResponseDTO.class, systemRequestDTO);
		
		System.out.println(sendRequest.getBody().getId());
	}
}
