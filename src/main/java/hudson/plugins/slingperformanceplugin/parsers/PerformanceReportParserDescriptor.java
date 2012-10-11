package hudson.plugins.slingperformanceplugin.parsers;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import hudson.model.Hudson;

/**
 * 
 * @author vazzolla
 *
 */
public abstract class PerformanceReportParserDescriptor extends
    Descriptor<GenericReportParser> {

  /**
   * Internal unique ID that distinguishes a parser.
   */
  public final String getId() {
    return getClass().getName();
  }

  /**
   * Returns all the registered {@link PerformanceReportParserDescriptor}s.
   */
  public static DescriptorExtensionList<GenericReportParser, PerformanceReportParserDescriptor> all() {
    return Hudson.getInstance().<GenericReportParser, PerformanceReportParserDescriptor>getDescriptorList(GenericReportParser.class);
  }

  /**
   * Return parser descriptor by id
   * @param id the id of the parser to be returned
   * @return the parser descriptor to be returned
   */
  public static PerformanceReportParserDescriptor getById(String id) {
    for (PerformanceReportParserDescriptor d : all())
      if (d.getId().equals(id))
        return d;
    return null;
  }
}
