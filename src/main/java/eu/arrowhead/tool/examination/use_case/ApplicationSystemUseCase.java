package eu.arrowhead.tool.examination.use_case;

public class ApplicationSystemUseCase implements UseCase {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public ApplicationSystemUseCase() {
		UseCasesToRun.getApplicationSystem().add(this);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void start() {
		throw new UnsupportedOperationException("start method must be override");		
	}
}
