package eu.arrowhead.tool.examination.use_case;

public class SystemOperatorUseCase implements UseCase {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public SystemOperatorUseCase() {
		UseCasesToRun.getSystemOperator().add(this);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void start() {
		throw new UnsupportedOperationException("start method must be override");		
	}
}
