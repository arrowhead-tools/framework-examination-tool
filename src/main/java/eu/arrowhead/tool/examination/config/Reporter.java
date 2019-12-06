package eu.arrowhead.tool.examination.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.opencsv.CSVWriter;

public class Reporter {
	
	//=================================================================================================
	// members
	
	private static String filePath;
	private static final String DATA_SET_HEADER = "use_case, request_sent_ms, endpoint, latency_ms\n";
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public Reporter() throws IOException {
		createCSV();
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void report(final List<String[]> newData) throws IOException, URISyntaxException {
		final CSVWriter writer = new CSVWriter(new FileWriter(new File(filePath), true));
		writer.writeAll(newData);
		writer.close();
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void createCSV() throws IOException {
		String fileName= LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + "_report.csv";
		fileName = fileName.replace("-", "");
		fileName = fileName.replace(":", "");
		filePath = "report/" + fileName;
		final File file = new File(filePath);
		file.getParentFile().mkdirs();
		if (file.createNewFile()) {
			final FileWriter writer = new FileWriter(new File(filePath));
			writer.write(DATA_SET_HEADER);
			writer.flush();
			writer.close();
		}
	}
}
