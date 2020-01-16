package eu.arrowhead.tool.examination.use_case;

import java.util.ArrayList;
import java.util.List;

public class UseCasesToRun {

	//=================================================================================================
	// members
	
	private static final List<SystemOperatorUseCase> SYSTEM_OPERATOR = new ArrayList<>();
	private static final List<ApplicationSystemUseCase> APPLICATION_SYSTEM = new ArrayList<>();
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static List<SystemOperatorUseCase> getSystemOperator() { return SYSTEM_OPERATOR; }
	public static List<ApplicationSystemUseCase> getApplicationSystem() { return APPLICATION_SYSTEM; }	
	
}
