package eu.arrowhead.tool.examination.use_case.system_operator;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import eu.arrowhead.common.dto.shared.CloudRequestDTO;
import eu.arrowhead.tool.examination.config.CoreSystems;
import eu.arrowhead.tool.examination.config.HttpActor;
import eu.arrowhead.tool.examination.controller.dto.CloudWithRelaysListResponseDTO;
import eu.arrowhead.tool.examination.controller.dto.CloudWithRelaysResponseDTO;
import eu.arrowhead.tool.examination.controller.dto.RelayListResponseDTO;
import eu.arrowhead.tool.examination.controller.dto.RelayRequestDTO;
import eu.arrowhead.tool.examination.controller.dto.RelayResponseDTO;
import eu.arrowhead.tool.examination.controller.dto.RelayType;
import eu.arrowhead.tool.examination.use_case.SystemOperatorUseCase;
import eu.arrowhead.tool.examination.util.ExaminationUtil;
import eu.arrowhead.tool.examination.util.MgmtUri;

@Component
public class RegisterCloudWithRelaysThenDeleteCloud extends SystemOperatorUseCase {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void runUseCase() {
		RelayRequestDTO gatekeeperRelay = ExaminationUtil.generateRelayRequestDTO(RelayType.GATEKEEPER_RELAY, false, true);
		RelayRequestDTO gatewayRelay = ExaminationUtil.generateRelayRequestDTO(RelayType.GATEWAY_RELAY, true, true);
		CloudRequestDTO neighborCloud = ExaminationUtil.generateCloudRequestDTOWithoutRelays(true, true);
		
		ResponseEntity<RelayListResponseDTO> gatekeeperRelayResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getGatekeeperUri(MgmtUri.GATEKEEPER_RELAYS), HttpMethod.POST, RelayListResponseDTO.class, List.of(gatekeeperRelay));
		ResponseEntity<RelayListResponseDTO> gatewayRelayResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getGatekeeperUri(MgmtUri.GATEKEEPER_RELAYS), HttpMethod.POST, RelayListResponseDTO.class, List.of(gatewayRelay));
		verifyRelayResponse(gatekeeperRelayResponse.getBody().getData().get(0), gatekeeperRelay);
		verifyRelayResponse(gatewayRelayResponse.getBody().getData().get(0), gatewayRelay);
		
		neighborCloud.setGatekeeperRelayIds(List.of(gatekeeperRelayResponse.getBody().getData().get(0).getId()));
		neighborCloud.setGatewayRelayIds(List.of(gatewayRelayResponse.getBody().getData().get(0).getId()));
		ResponseEntity<CloudWithRelaysListResponseDTO> neighborCloudResponse = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getGatekeeperUri(MgmtUri.GATEKEEPER_CLOUDS), HttpMethod.POST, CloudWithRelaysListResponseDTO.class, List.of(neighborCloud));
		verifyCloudResponse(neighborCloudResponse.getBody().getData().get(0), neighborCloud);
		
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getGatekeeperUri(MgmtUri.GATEKEEPER_CLOUDS + "/" + String.valueOf(neighborCloudResponse.getBody().getData().get(0).getId())), HttpMethod.DELETE, Void.class);
		ResponseEntity<RelayResponseDTO> gkRelayResp = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getGatekeeperUri(MgmtUri.GATEKEEPER_RELAYS + "/" + String.valueOf(gatekeeperRelayResponse.getBody().getData().get(0).getId())), HttpMethod.GET, RelayResponseDTO.class);
		assertNotNull(gkRelayResp.getBody().getId(), "Gatekeeper relay must exists still after deleting cloud");
		ResponseEntity<RelayResponseDTO> gwRelayResp = request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getGatekeeperUri(MgmtUri.GATEKEEPER_RELAYS + "/" + String.valueOf(gatewayRelayResponse.getBody().getData().get(0).getId())), HttpMethod.GET, RelayResponseDTO.class);
		assertNotNull(gwRelayResp.getBody().getId(), "Gateway relay must exists still after deleting cloud");
		
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getGatekeeperUri(MgmtUri.GATEKEEPER_RELAYS + "/" + String.valueOf(gatekeeperRelayResponse.getBody().getData().get(0).getId())), HttpMethod.DELETE, Void.class);
		request(HttpActor.SYSTEM_OPERATOR, CoreSystems.getGatekeeperUri(MgmtUri.GATEKEEPER_RELAYS + "/" + String.valueOf(gatewayRelayResponse.getBody().getData().get(0).getId())), HttpMethod.DELETE, Void.class);
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void verifyRelayResponse(final RelayResponseDTO response, final RelayRequestDTO request) {
		assertNotNull(response, "Relay cannot be null");
		assertNotNull(response.getId(), "Relay id have to be defined in the reponse");
		assertEqualsIgnoreCaseWithTrim(request.getAddress(), response.getAddress(), "Relay address in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getPort()), String.valueOf(response.getPort()), "Relay port in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(request.getType(), response.getType().name(), "Relay type in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.isExclusive()), String.valueOf(response.isExclusive()), "Relay exclusivity in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.isSecure()), String.valueOf(response.isSecure()), "Relay security in request and in response must be the same");
	}
	
	//-------------------------------------------------------------------------------------------------
	private void verifyCloudResponse(final CloudWithRelaysResponseDTO response, final CloudRequestDTO request) {
		assertNotNull(response, "Cloud cannot be null");
		assertNotNull(response.getId(), "Cloud id have to be defined in the reponse");
		assertEqualsIgnoreCaseWithTrim(request.getOperator(), response.getOperator(), "Cloud operator in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(request.getName(), response.getName(), "Cloud name in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getNeighbor()), String.valueOf(response.getNeighbor()), "Cloud neighbor flag in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getSecure()), String.valueOf(response.getSecure()), "Cloud security in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(false), String.valueOf(response.getOwnCloud()), "Cloud in response can't be owncloud");
		if (request.getAuthenticationInfo() != null && !request.getAuthenticationInfo().isBlank()) {
			assertEqualsIgnoreCaseWithTrim(request.getAuthenticationInfo(), response.getAuthenticationInfo(), "Cloud auth info in request and in response must be the same");
		}
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getGatekeeperRelayIds().get(0)), String.valueOf(response.getGatekeeperRelays().get(0).getId()), "Cloud gatekeeper id in request and in response must be the same");
		assertEqualsIgnoreCaseWithTrim(String.valueOf(request.getGatewayRelayIds().get(0)), String.valueOf(response.getGatewayRelays().get(0).getId()), "Cloud gateway id in request and in response must be the same");
	}
}
