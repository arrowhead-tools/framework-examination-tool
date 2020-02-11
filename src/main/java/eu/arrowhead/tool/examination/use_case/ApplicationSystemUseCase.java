package eu.arrowhead.tool.examination.use_case;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;

import eu.arrowhead.common.core.CoreSystem;
import eu.arrowhead.tool.examination.config.ExaminationHttpService;
import eu.arrowhead.tool.examination.config.HttpActor;
import eu.arrowhead.tool.examination.config.Reporter;
import eu.arrowhead.tool.examination.config.ReporterType;
import eu.arrowhead.tool.examination.util.ExaminationAssert;

public class ApplicationSystemUseCase extends ExaminationAssert implements UseCase {
	
	//=================================================================================================
	// members
	
	private final Set<CoreSystem> necesarryCoreSystems = new HashSet<>();
	private final boolean isActive;
	
	@Autowired
	protected ExaminationHttpService httpService;
	private final Logger logger = LogManager.getLogger(this.getClass());

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public ApplicationSystemUseCase(final boolean isActive, final CoreSystem... coreSystems) {
		this.isActive = isActive;
		for (final CoreSystem cs : coreSystems) {
			necesarryCoreSystems.add(cs);
		}
		UseCasesToRun.getApplicationSystem().add(this);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void start() {
		try {
			logger.info("APP use case started: " + this.getClass().getSimpleName());
			runUseCase();
			logger.info("APP use case finished: " + this.getClass().getSimpleName());
		} catch (final Throwable ex) {
			logger.error("APP use case failed: " + this.getClass().getSimpleName());
			logger.error(ex.getMessage());
			final List<String[]> reportSet = new ArrayList<>();
			reportSet.add(new String[] { this.getClass().getSimpleName(), "no error", "error", STATUS_NOT_OK, ex.getMessage() });
			try {
				Reporter.report(reportSet, ReporterType.ASSERT);
			} catch (IOException | URISyntaxException e) {
				logger.error("CSV reporting error occured");
			}
		}		
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean isUseCaseActive() {
		return isActive;
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public Set<CoreSystem> getNecesarryCoreSystems() {
		return necesarryCoreSystems;
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
