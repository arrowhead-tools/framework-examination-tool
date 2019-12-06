package eu.arrowhead.tool.examination.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import eu.arrowhead.tool.examination.config.Reporter;
import eu.arrowhead.tool.examination.config.ReporterType;

public class ExaminationAssert {

	//=================================================================================================
	// members
	
	private static final String STATUS_OK = "OK";
	private static final String STATUS_NOT_OK = "NOT_OK";
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public void notNull(final Object object, final String message) {
		Assert.notNull(message, "assert message cannot be null");
		final List<String[]> reportSet = new ArrayList<>();
		try {
			Assert.notNull(object, "");
			reportSet.add(new String[] { this.getClass().getSimpleName(), "not null", "not null", STATUS_OK, message });
		} catch (final IllegalArgumentException ex) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), "not null", "null", STATUS_NOT_OK, message });
		} finally {
			try {
				Reporter.report(reportSet, ReporterType.ASSERT);
			} catch (IOException | URISyntaxException e) {
				logger.error("CSV reporting error occured");
			}
		}
	}
	
}
