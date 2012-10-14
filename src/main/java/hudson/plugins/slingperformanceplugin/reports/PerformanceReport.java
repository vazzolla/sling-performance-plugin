package hudson.plugins.slingperformanceplugin.reports;

import hudson.model.AbstractBuild;
import hudson.plugins.slingperformanceplugin.Messages;
import hudson.plugins.slingperformanceplugin.PerformanceBuildAction;
import hudson.plugins.slingperformanceplugin.SlingPerformanceReportMap;
import hudson.plugins.slingperformanceplugin.SlingReportSample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single performance report, which consists of multiple {@link TestRunReport}s for
 * different URLs that was tested.
 *
 * This object belongs under {@link SlingPerformanceReportMap}.
 */
public class PerformanceReport extends AbstractReport implements
    Comparable<PerformanceReport> {

  private PerformanceBuildAction buildAction;

  private SlingReportSample reportSample;

  private String reportFileName = null;
 

  /**
   * {@link TestRunReport}s keyed by their {@link TestRunReport#getStaplerUri()}.
   */
  private final Map<String, TestRunReport> testReportMap = new LinkedHashMap<String, TestRunReport>();
  
  private PerformanceReport lastBuildReport;

  public void addSample(SlingReportSample pReportSample) {
    String testName = pReportSample.getTestName();
    if (testName == null) {
      return;
    }
    
    TestRunReport uriReport = testReportMap.get(testName);
    
    if (uriReport == null) {
      uriReport = new TestRunReport(this);
      testReportMap.put(testName, uriReport);
    }
    uriReport.addReportSample(pReportSample);
  }

  public int compareTo(PerformanceReport jmReport) {
    if (this == jmReport) {
      return 0;
    }
    return getReportFileName().compareTo(jmReport.getReportFileName());
  }

  public long getMedian() {
    return this.reportSample.getMedian();
  }
  
  public AbstractBuild<?, ?> getBuild() {
    return buildAction.getBuild();
  }

  PerformanceBuildAction getBuildAction() {
    return buildAction;
  }

  public String getDisplayName() {
    return Messages.Report_DisplayName();
  }

  public TestRunReport getDynamic(String token) throws IOException {
    return getUriReportMap().get(token);
  }

  public SlingReportSample getReportSample() {
    return reportSample;
  }

  public long getMax() {
	  return this.reportSample.getMax();
  }

  public long getMin() {
    return this.reportSample.getMin();
  }

  public String getReportFileName() {
    return reportFileName;
  }

  public List<TestRunReport> getUriListOrdered() {
    Collection<TestRunReport> uriCollection = getUriReportMap().values();
    List<TestRunReport> UriReportList = new ArrayList<TestRunReport>(uriCollection);
    return UriReportList;
  }

  public Map<String, TestRunReport> getUriReportMap() {
    return testReportMap;
  }

  public void setBuildAction(PerformanceBuildAction buildAction) {
	  this.buildAction = buildAction;
  }

  public void setReportSample(SlingReportSample reportSample) {
    this.reportSample = reportSample;
  }

  public void setReportFileName(String reportFileName) {
    this.reportFileName = reportFileName;
  }

  
  
  /*public void setLastBuildReport( PerformanceReport lastBuildReport ) {
    Map<String, TestRunReport> lastBuildUriReportMap = lastBuildReport.getUriReportMap();
    for (Map.Entry<String, TestRunReport> item : testReportMap.entrySet()) {
        TestRunReport lastBuildUri = lastBuildUriReportMap.get( item.getKey() );
        if ( lastBuildUri != null ) {
            item.getValue().addLastBuildUriReport( lastBuildUri );
        } else {
        }
    }
    this.lastBuildReport = lastBuildReport;
  }
  */
 
  
  public long getMedianDiff() {
      if ( lastBuildReport == null ) {
          return 0;
      }
      return getMedian() - lastBuildReport.getMedian();
  }

  
  public String getLastBuildHttpCodeIfChanged() {
      return "";
  }
  
}
