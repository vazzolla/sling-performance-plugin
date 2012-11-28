package hudson.plugins.slingperformanceplugin.parsers;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.plugins.slingperformanceplugin.SlingReportSample;
import hudson.plugins.slingperformanceplugin.reports.PerformanceReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Parser for Sling Performance test reports.
 * 
 */
public class SlingTextFormatReportParser extends GenericReportParser {
	
	@Extension
	public static final class DescriptorImpl extends PerformanceReportParserDescriptor {
	
		@Override
	    public String getDisplayName() {
	      return "Sling text format performance report";
	    }
	}
	
	@DataBoundConstructor
	public SlingTextFormatReportParser(String glob) {
		super(glob);
	}
	
	@Override
	public String getDefaultGlobPattern() {
		return "**/*.txt";
	}
	
	@Override
	public Collection<PerformanceReport> parse(AbstractBuild<?, ?> build,
			Collection<File> reports, TaskListener listener) throws IOException {
		
		List<PerformanceReport> result = new ArrayList<PerformanceReport>();
		for (File f : reports) {
						
			PrintStream logger = listener.getLogger();
			logger.println("Performance: Parsing Sling Performance report file " + f.getName());
			logger.println("Report path:" + f.getAbsolutePath());
			
			final PerformanceReport r = new PerformanceReport();
			r.setReportFileName(f.getName());
			SlingReportSample sample = new SlingReportSample();
			
			BufferedReader fis = new BufferedReader(new FileReader(f)) ;
		
			String resultRow;
		
			boolean firstRow = true;
			boolean parsingFailed = false;
			while((resultRow = fis.readLine()) != null){
					if (firstRow){
						if (!resultRow.contains("Test Suite")){
							parsingFailed = true;
							break;	
						}
						
						firstRow = false;
						continue;
					}
						
					String[] results = resultRow.split("\\|");
					if (results.length == 9){
						sample.setMedian(Long.valueOf(results[6].trim()));
						sample.setMin(Long.valueOf(results[4].trim()));
						sample.setMax(Long.valueOf(results[8].trim()));
						sample.set10Percentile(Long.valueOf(results[5].trim()));
						sample.set90Percentile(Long.valueOf(results[7].trim()));
						
						sample.setSuccessful(true);
						sample.setTestName(results[1].trim());
						sample.setTestSuite(results[0].trim());
					}
					else
					{
						sample.setSuccessful(false);
						parsingFailed = true;
					}
			}	
			if (!parsingFailed){
				r.setReportSample(sample);
				r.addSample(sample);
				result.add(r);
				
				logger.println("Performance: Parsing Sling Performance report file " + f.getName() + " succedded");
			}
			else
			{
				r.addSample(sample);
				logger.println("Performance: Parsing Sling Performance report file " + f.getName() + " failed");
			}
		}
	    return result;
	}
}
