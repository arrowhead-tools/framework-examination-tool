package eu.arrowhead.tool.examination.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.opencsv.CSVWriter;

import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.InvalidParameterException;

public class Reporter {
	
	//=================================================================================================
	// members
	
	private static final String DATA_SET_HEADER_LATENCY = "use_case,request_sent_ms,endpoint,latency_ms\n";
	private static final String DATA_SET_HEADER_ASSERT = "use_case,expected,result,status,remark\n";
	
	private static File latencyFile;
	private static File assertFile;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public Reporter(final ReporterType type) throws IOException {
		switch (type) {
		case LATENCY:
			latencyFile = createCSV(type, DATA_SET_HEADER_LATENCY);			
			break;
		case ASSERT:
			assertFile = createCSV(type, DATA_SET_HEADER_ASSERT);			
			break;
		default:
			throw new InvalidParameterException("Reporter type not known: " + type.name());
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void report(final List<String[]> newData, final ReporterType type) throws IOException, URISyntaxException {
		CSVWriter writer;
		switch (type) {
		case LATENCY:
			writer = new CSVWriter(new FileWriter(latencyFile, true));
			break;
		case ASSERT:
			writer = new CSVWriter(new FileWriter(assertFile, true));			
			break;
		default:
			throw new InvalidParameterException("Reporter type not known: " + type.name());
		}
		writer.writeAll(newData);
		writer.close();
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private File createCSV(final ReporterType type, final String header) throws IOException {
		final String filePath = getPathAndFileName(type);
		final File file = new File(filePath);
		file.getParentFile().mkdirs();
		if (file.createNewFile()) {
			final FileWriter writer = new FileWriter(file);
			writer.write(header);
			writer.flush();
			writer.close();
		}
		return file;
	}
	
	//-------------------------------------------------------------------------------------------------
	private static String getPathAndFileName(final ReporterType type) {
		String currentDir = System.getProperty("user.dir");
		while (!currentDir.endsWith("framework-examination-tool")) {
			try {				
				currentDir = Paths.get(currentDir).getParent().toAbsolutePath().toString();
			} catch (final Exception ex) {
				throw new ArrowheadException("Creating path for report files failed: No 'framework-examination-tool' directory found");
			}
		}
		
		String fileName = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + "_report_" + type.name().toLowerCase() + ".csv";
		fileName = fileName.replace("-", "");
		fileName = fileName.replace(":", "");
		return currentDir + File.separator + "report" + File.separator + fileName;
	}
}
