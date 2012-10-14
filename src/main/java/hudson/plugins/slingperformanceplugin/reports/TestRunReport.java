package hudson.plugins.slingperformanceplugin.reports;

import hudson.model.ModelObject;
import hudson.model.AbstractBuild;
import hudson.plugins.slingperformanceplugin.GraphConfigurationDetail;
import hudson.plugins.slingperformanceplugin.SlingReportSample;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A report about a particular tested URI.
 * 
 * This object belongs under {@link PerformanceReport}.
 */
public class TestRunReport extends AbstractReport implements ModelObject,
    Comparable<TestRunReport> {

  public final static String END_PERFORMANCE_PARAMETER = ".endperformanceparameter";

  private SlingReportSample reportSample;

  /**
   * The parent object to which this object belongs.
   */
  private final PerformanceReport performanceReport;

  TestRunReport(PerformanceReport performanceReport) {
    this.performanceReport = performanceReport;
    
  }

  public void addReportSample(SlingReportSample reportSample) {
    this.reportSample = reportSample;
  }

  public int compareTo(TestRunReport uriReport) {
    if (uriReport == this) {
      return 0;
    }
    return uriReport.reportSample.getTestName().compareTo(this.reportSample.getTestName());
  }

  public long get90Line() {
    long result = 0;
    
    return result;
  }
  

  public long getMedian() {    
    return reportSample.getMedian();    
  }
  
  public long getMin(){
	  return reportSample.getMin();
  }

  public long getMax(){
	  return reportSample.getMax();
  }
  
  public AbstractBuild<?, ?> getBuild() {
    return performanceReport.getBuild();
  }

  public String getDisplayName() {
    return reportSample.getTestName();
  }

  public SlingReportSample getSample() {
    return reportSample;
  }

  public PerformanceReport getPerformanceReport() {
    return performanceReport;
  }
}
