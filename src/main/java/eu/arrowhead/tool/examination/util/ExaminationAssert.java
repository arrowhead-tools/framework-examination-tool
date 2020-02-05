package eu.arrowhead.tool.examination.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
			reportSet.add(new String[] { this.getClass().getSimpleName(), expected, actual, STATUS_OK, remark });
		} else {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expected, actual, STATUS_NOT_OK, remark });
		}
		report(reportSet);
	}
	
	//-------------------------------------------------------------------------------------------------
	public void assertExpectException(final Class<?> expected, final Exception actual, final String remark) {
		Assert.notNull(expected, "assert expected exception cannot be null");
		Assert.notNull(remark, "assert remark cannot be null");
		
		final List<String[]> reportSet = new ArrayList<>();
		if (actual == null) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expected.getTypeName(), "no exception", STATUS_NOT_OK, remark });
		} else if (!expected.getTypeName().equalsIgnoreCase(actual.getClass().getTypeName())) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expected.getTypeName(), actual.getClass().getTypeName(), STATUS_NOT_OK, remark });
		} else {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expected.getTypeName(), actual.getClass().getTypeName(), STATUS_OK, remark });
		}
		report(reportSet);
	}
	
	//-------------------------------------------------------------------------------------------------
	public void assertEqualsStringLists(final List<String> expected, final List<String> actual, final String remark) {
		Assert.notNull(expected, "assert expected list cannot be null");
		Assert.notNull(actual, "assert actual list cannot be null");
		Assert.notNull(remark, "assert remark cannot be null");
		
		final List<String> expectedList = new ArrayList<>();
		for ( final String s : expected) {
			expectedList.add(s.toLowerCase().trim());
		}
		final List<String> actualList = new ArrayList<>();
		for ( final String s : actual) {
			actualList.add(s.toLowerCase().trim());
		}
		expectedList.sort((final String s1, final String s2) -> s1.compareTo(s2));
		actualList.sort((final String s1, final String s2) -> s1.compareTo(s2));
		
		final StringBuilder expectedSB = new StringBuilder("[");
		for (final String s : expectedList) {
			expectedSB.append(s);
			expectedSB.append(",");
		}
		expectedSB.deleteCharAt(expectedSB.length()-1);
		expectedSB.append("]");
		final String expectedString = expectedSB.toString();
		
		final StringBuilder actualSB = new StringBuilder("[");
		for (final String s : actualList) {
			actualSB.append(s);
			actualSB.append(",");
		}
		actualSB.deleteCharAt(actualSB.length()-1);
		actualSB.append("]");
		final String actualString = actualSB.toString();
		
		final List<String[]> reportSet = new ArrayList<>();
		if (expectedString.equalsIgnoreCase(actualString)) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expectedString, actualString, STATUS_OK, remark });
		} else {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expectedString, actualString, STATUS_NOT_OK, remark });
		}
		report(reportSet);		
		
	}
	
	//-------------------------------------------------------------------------------------------------
	public void assertEqualsStringStringMap(final Map<String, String> expected, final Map<String, String> actual, final String remark) {
		Assert.notNull(expected, "assert expected list cannot be null");
		Assert.notNull(actual, "assert actual list cannot be null");
		Assert.notNull(remark, "assert remark cannot be null");
		
		final List<String> expectedList = new ArrayList<>();
		for ( final Entry<String, String> e : expected.entrySet()) {
			expectedList.add(e.getKey().toLowerCase().trim() + "=" + e.getValue().toLowerCase().trim());
		}
		final List<String> actualList = new ArrayList<>();
		for ( final Entry<String, String> e : actual.entrySet()) {
			actualList.add(e.getKey().toLowerCase().trim() + "=" + e.getValue().toLowerCase().trim());
		}
		expectedList.sort((final String s1, final String s2) -> s1.compareTo(s2));
		actualList.sort((final String s1, final String s2) -> s1.compareTo(s2));
		
		final StringBuilder expectedSB = new StringBuilder("[");
		for (final String s : expectedList) {
			expectedSB.append(s);
			expectedSB.append(",");
		}
		expectedSB.deleteCharAt(expectedSB.length()-1);
		expectedSB.append("]");
		final String expectedString = expectedSB.toString();
		
		final StringBuilder actualSB = new StringBuilder("[");
		for (final String s : actualList) {
			actualSB.append(s);
			actualSB.append(",");
		}
		actualSB.deleteCharAt(actualSB.length()-1);
		actualSB.append("]");
		final String actualString = actualSB.toString();
		
		final List<String[]> reportSet = new ArrayList<>();
		if (expectedString.equalsIgnoreCase(actualString)) {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expectedString, actualString, STATUS_OK, remark });
		} else {
			reportSet.add(new String[] { this.getClass().getSimpleName(), expectedString, actualString, STATUS_NOT_OK, remark });
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
