package eu.arrowhead.tool.examination.util;

public class MgmtUri {
	
	//=================================================================================================
	// members

	public static final String SERVICE_REGISTRY_MGMT = "/serviceregistry/mgmt";
	public static final String SERVICE_REGISTRY_MGMT_SYSTEMS = "/serviceregistry/mgmt/systems";
	public static final String SERVICE_REGISTRY_MGMT_SERVICES = "/serviceregistry/mgmt/services";
	public static final String AUTHORIZATION_MGMT_INTRACLOUD = "/authorization/mgmt/intracloud";
	public static final String ORCHESTRATOR_ORCHESTRATION = "/orchestrator/orchestration";
	public static final String GATEKEEPER_CLOUDS = "/gatekeeper/mgmt/clouds";
	public static final String GATEKEEPER_RELAYS = "/gatekeeper/mgmt/relays";
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public MgmtUri() {
		throw new UnsupportedOperationException();
	}
}
