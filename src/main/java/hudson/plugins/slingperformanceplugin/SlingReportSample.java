package hudson.plugins.slingperformanceplugin;

import java.util.Date;

/**
 * Information about a particular test run and how that went.
 */
public class SlingReportSample implements Comparable<SlingReportSample> {
	
	private long percentile10;
	
	private long percentile90;
	
	private long median;
	
	private long min;
	
	private long max;
	
	private boolean successful;
  
	private Date date;

	private String testName;
	
	private String testSuite;
  
	public long getMedian() {
		return median;
	}

	public long getMin(){
		return min;
	}

	public long getMax(){
		return max;
	}
	
	public long get10Percentile(){
		return percentile10;
	}

	public long get90Percentile(){
		return percentile90;
	}
	
	public Date getDate() {
		return date;
	}

	public String getTestName() {
		return testName;
	}
	
	public String getTestSuite() {
		return testSuite;
	}
	
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	public void setTestSuite(String testSuiteName) {
		this.testSuite = testSuiteName;
	}
  
	public boolean isFailed() {
		return !isSuccessful();
	}

	public boolean isSuccessful() {
		return successful;
	}
	
	public void setMedian(long median) {
		this.median = median;
	}
	
	public void setMin(long min){
		this.min = min;
	}
	
	public void setMax(long max){
		this.max = max;
	}
	
	public void set10Percentile(long percentile10) {
		this.percentile10 = percentile10;
	}
	
	public void set90Percentile(long percentile90) {
		this.percentile90 = percentile90;
	}
	

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
  
    public void setDate(Date time) {
    	this.date = time;
    }
  
    public int compareTo(SlingReportSample o) {
    	return (int) (getMedian() - o.getMedian());
    }
}
