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
	
	protected static final String STATUS_OK = "OK";
	protected static final String STATUS_NOT_OK = "NOT_OK";
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public void assertNotNull(final Object object, final String remark) {
		Assert.notNull(remark, "assert remark cannot be null");
		final List<String[]> reportSet = new ArrayList<>();
		try {
			Assert.notNull(object, "");
			reportSet.add(new String[] { this.getClass().getSimpleName(), "not null", "not null", STATUS_OK, remark });
		} catch (final IllegalArgumentException ex) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), "not null", "null", STATUS_NOT_OK, remark });
		} finally {
			report(reportSet);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public void assertEqualsIgnoreCaseWithTrim(final String expected, final String actual, final String remark) {
		Assert.notNull(remark, "assert remark cannot be null");
		
		final List<String[]> reportSet = new ArrayList<>();
		if (expected == null && actual != null) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), "expected and actual strings are not null", "expected string is null", STATUS_NOT_OK, remark });
		} else if (expected != null && actual == null) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), "expected and actual strings are not null", "actual string is null", STATUS_NOT_OK, remark });
		} else if (expected == null && actual == null) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), "expected and actual strings are not null", "both string is null", STATUS_NOT_OK, remark });
		}
		if (!reportSet.isEmpty()) {
			report(reportSet);
			return;
		}
		
		if (expected.trim().equalsIgnoreCase(actual.trim())) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), "equals", "equals", STATUS_OK, remark });
		} else {
			reportSet.add(new String[] { this.getClass().getSimpleName(), "equals", "not equals", STATUS_NOT_OK, remark });
		}
		report(reportSet);
	}
	
	//-------------------------------------------------------------------------------------------------
	public void assertExpectException(final Class<?> expected, final Exception actual, final String remark) {
		Assert.notNull(expected, "assert expected exception cannot be null");
		Assert.notNull(actual, "assert actual exception cannot be null");
		Assert.notNull(remark, "assert remark cannot be null");
		
		final List<String[]> reportSet = new ArrayList<>();
		if (expected.getTypeName().equalsIgnoreCase(actual.getClass().getTypeName())) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expected.getTypeName(), actual.getClass().getTypeName(), STATUS_OK, remark });
		} else {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expected.getTypeName(), actual.getClass().getTypeName(), STATUS_NOT_OK, remark });
		}
		report(reportSet);
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void report(final List<String[]> reportSet) {
		Assert.notNull(reportSet, "reportSet cannot be null");
		try {
			Reporter.report(reportSet, ReporterType.ASSERT);
		} catch (IOException | URISyntaxException e) {
			logger.error("CSV reporting error occured");
		}
	}	
	
}
