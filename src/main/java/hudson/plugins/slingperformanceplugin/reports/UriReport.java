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
public class UriReport extends AbstractReport implements ModelObject,
    Comparable<UriReport> {

  public final static String END_PERFORMANCE_PARAMETER = ".endperformanceparameter";

  private SlingReportSample reportSample;

  /**
   * The parent object to which this object belongs.
   */
  private final PerformanceReport performanceReport;

  /**
   * Escaped {@link #uri} that doesn't contain any letters that cannot be used
   * as a token in URL.
   */
  private final String staplerUri;
  
  private UriReport lastBuildUriReport;

  private String uri;

  UriReport(PerformanceReport performanceReport, String staplerUri, String uri) {
    this.performanceReport = performanceReport;
    this.staplerUri = staplerUri;
    this.uri = uri;
  }

  public void addReportSample(SlingReportSample reportSample) {
    this.reportSample = reportSample;
  }

  public int compareTo(UriReport uriReport) {
    if (uriReport == this) {
      return 0;
    }
    return uriReport.getUri().compareTo(this.getUri());
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
    return getUri();
  }

  public SlingReportSample getSample() {
    return reportSample;
  }

  public PerformanceReport getPerformanceReport() {
    return performanceReport;
  }

  public String getStaplerUri() {
    return staplerUri;
  }

  public String getUri() {
    return uri;
  }

  public String getShortUri() {
    if ( uri.length() > 130 ) {
        return uri.substring( 0, 129 );
    }
    return uri;
  }
  
  

  public void setUri(String uri) {
    this.uri = uri;
  }

  
  public String encodeUriReport() throws UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder(120);
    sb.append(performanceReport.getReportFileName()).append(
        GraphConfigurationDetail.SEPARATOR).append(getStaplerUri()).append(
        END_PERFORMANCE_PARAMETER);
    return URLEncoder.encode(sb.toString(), "UTF-8");
  }

  public void addLastBuildUriReport( UriReport lastBuildUriReport ) {
      this.lastBuildUriReport = lastBuildUriReport;
  }
  
  public long getMedianDiff() {
      if ( lastBuildUriReport == null ) {
          return 0;
      }
      return getMedian() - lastBuildUriReport.getMedian();
  }
  
}
