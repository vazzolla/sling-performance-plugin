package hudson.plugins.slingperformanceplugin.reports;


/**
 * Abstract class for classes with median, min and max attributes
 */
public abstract class AbstractReport {

  
  public AbstractReport() {
  
  }

  abstract public long getMedian();

  abstract public long getMax();
  
  abstract public long getMin();

}
