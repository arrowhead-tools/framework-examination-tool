package eu.arrowhead.tool.examination.use_case;

import javax.net.ssl.SSLContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;

import eu.arrowhead.tool.examination.config.ExaminationHttpService;
import eu.arrowhead.tool.examination.config.HttpActor;

public class SystemOperatorUseCase implements UseCase {
	
	//=================================================================================================
	// members

	@Autowired
	protected ExaminationHttpService httpService;
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public SystemOperatorUseCase() {
		UseCasesToRun.getSystemOperator().add(this);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void start() {
		try {
			logger.info("SYSOP use case started: " + this.getClass().getSimpleName());
			runUseCase();
			logger.info("SYSOP use case finished: " + this.getClass().getSimpleName());
		} catch (final Throwable ex) {
			logger.error("SYSOP use case failed: " + this.getClass().getSimpleName());
			logger.error(ex.getMessage());
		}		
	}
	
	//-------------------------------------------------------------------------------------------------
	public void runUseCase() {
		throw new UnsupportedOperationException("runUseCase() method must be override");		
	}
	
	//-------------------------------------------------------------------------------------------------
	protected <T,P> ResponseEntity<T> request(final HttpActor actor, final UriComponents uri, final HttpMethod method, final Class<T> responseType, final P payload) {
		return httpService.sendRequest(actor, uri, method, responseType, payload, null, this.getClass().getSimpleName());
	}
	
	//-------------------------------------------------------------------------------------------------
	protected <T> ResponseEntity<T> request(final HttpActor actor, final UriComponents uri, final HttpMethod method, final Class<T> responseType, final SSLContext givenContext) {
		return httpService.sendRequest(actor, uri, method, responseType, null, givenContext, this.getClass().getSimpleName());
	}
	
	//-------------------------------------------------------------------------------------------------
	protected <T> ResponseEntity<T> request(final HttpActor actor, final UriComponents uri, final HttpMethod method, final Class<T> responseType) {
		return httpService.sendRequest(actor, uri, method, responseType, null, null, this.getClass().getSimpleName());
	}
}
