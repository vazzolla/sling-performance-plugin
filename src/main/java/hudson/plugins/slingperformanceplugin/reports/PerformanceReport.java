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
 * Represents a single performance report, which consists of multiple {@link UriReport}s for
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
   * {@link UriReport}s keyed by their {@link UriReport#getStaplerUri()}.
   */
  private final Map<String, UriReport> testReportMap = new LinkedHashMap<String, UriReport>();
  
  private PerformanceReport lastBuildReport;

  public void addSample(SlingReportSample pReportSample) {
    String testName = pReportSample.getTestName();
    if (testName == null) {
      return;
    }
    
    
    
    UriReport uriReport = testReportMap.get(testName);
    
    if (uriReport == null) {
      uriReport = new UriReport(this, testName, uri);
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
    
	  
	long result = 0;
	
	/*
	
    int size = size();
    if (size != 0) {
      long average = 0;
      List<SlingReportSample> allSamples = new ArrayList<SlingReportSample>();
      for (UriReport currentReport : uriReportMap.values()) {
        allSamples.addAll(currentReport.getHttpSampleList());
      }
      Collections.sort(allSamples);
      result = allSamples.get((int) (allSamples.size() * .5)).getMedian();
    }*/
    
    return result;
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

  public UriReport getDynamic(String token) throws IOException {
    return getUriReportMap().get(token);
  }

  public SlingReportSample getReportSample() {
    return reportSample;
  }

  public long getMax() {
    long max = Long.MIN_VALUE;
    for (UriReport currentReport : testReportMap.values()) {
      max = Math.max(currentReport.getMax(), max);
    }
    return max;
  }

  public long getMin() {
    long min = Long.MAX_VALUE;
    for (UriReport currentReport : testReportMap.values()) {
      min = Math.min(currentReport.getMin(), min);
    }
    return min;
  }

  public String getReportFileName() {
    return reportFileName;
  }

  public List<UriReport> getUriListOrdered() {
    Collection<UriReport> uriCollection = getUriReportMap().values();
    List<UriReport> UriReportList = new ArrayList<UriReport>(uriCollection);
    return UriReportList;
  }

  public Map<String, UriReport> getUriReportMap() {
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

  
  
  public void setLastBuildReport( PerformanceReport lastBuildReport ) {
    Map<String, UriReport> lastBuildUriReportMap = lastBuildReport.getUriReportMap();
    for (Map.Entry<String, UriReport> item : testReportMap.entrySet()) {
        UriReport lastBuildUri = lastBuildUriReportMap.get( item.getKey() );
        if ( lastBuildUri != null ) {
            item.getValue().addLastBuildUriReport( lastBuildUri );
        } else {
        }
    }
    this.lastBuildReport = lastBuildReport;
  }
  
 
  
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
