package hudson.plugins.slingperformanceplugin;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.slingperformanceplugin.parsers.GenericReportParser;
import hudson.plugins.slingperformanceplugin.parsers.PerformanceReportParserDescriptor;
import hudson.plugins.slingperformanceplugin.reports.PerformanceReport;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kohsuke.stapler.DataBoundConstructor;

public class SlingPerformancePublisher extends Recorder {
  
	@Extension
	public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
		
		public DescriptorImpl() {
			super(SlingPerformancePublisher.class);
		}
		
		@Override
		public String getDisplayName() {
			return Messages.Publisher_DisplayName();
		}

		@Override
		public String getHelpFile() {
			return "/plugin/slingperformance/help.html";
		}

		public List<PerformanceReportParserDescriptor> getParserDescriptors() {
			return PerformanceReportParserDescriptor.all();
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}
	}

  
	/**
	 * Configured performance report parsers.
	 */
	private List<GenericReportParser> parsers;
  
  
	@DataBoundConstructor
	public SlingPerformancePublisher(List<? extends GenericReportParser> parsers){
		if (parsers == null)
			parsers = Collections.emptyList();
		this.parsers = new ArrayList<GenericReportParser>(parsers);
	}
	
	// Get the parsed configured for performance report parsing
	public List<GenericReportParser> getParsers() {
		return parsers;
	}
	
	public static File getPerformanceReport(AbstractBuild<?, ?> build,
			String parserDisplayName, String performanceReportName) {
		return new File(build.getRootDir(),
				SlingPerformanceReportMap.getPerformanceReportFileRelativePath(
						parserDisplayName,
						performanceReportName));
	}

	@Override
	public Action getProjectAction(AbstractProject<?, ?> project) {
		return new SlingPerformanceProjectAction(project);
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}

  
  /**
   * look for performance reports based in the configured parameter includes.
   * 'includes' is - an Ant-style pattern - a list of files and folders
   * separated by the characters ;:,
   */
  protected static List<FilePath> locatePerformanceReports(FilePath workspace,
      String includes) throws IOException, InterruptedException {
   
    try {
      String parts[] = includes.split("\\s*[;:,]+\\s*");
      List<FilePath> files = new ArrayList<FilePath>();
        for (String path : parts) {
          FilePath[] ret = workspace.list(path);
          if (ret.length > 0) {
             files.addAll(Arrays.asList(ret));
          }
      }
     if (!files.isEmpty()) return files;

    } catch (IOException e) {
    }

    ArrayList<FilePath> files = new ArrayList<FilePath>();
    String parts[] = includes.split("\\s*[;:,]+\\s*");
    for (String path : parts) {
      FilePath src = workspace.child(path);
      if (src.exists()) {
        if (src.isDirectory()) {
          files.addAll(Arrays.asList(src.list("**/*")));
        } else {
          files.add(src);
        }
      }
    }
    return files;
  }

  @Override
  public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
      BuildListener listener) throws InterruptedException, IOException {
    
	  if (build.getResult().isWorseThan(Result.UNSTABLE)){
		  return true;
	  }
	  
	  PrintStream logger = listener.getLogger();
	  
	  logger.println("Performance: No condition configured for making the build " + Result.FAILURE.toString().toLowerCase());
   
	  // add the report to the build object.
	  PerformanceBuildAction a = new PerformanceBuildAction(build, logger, parsers);
	  build.addAction(a);
    
    for (GenericReportParser parser : parsers) {
      String glob = parser.glob;
      
      logger.println("Performance: Recording " + parser.getReportName() + " reports '" + glob + "'");

      List<FilePath> files = locatePerformanceReports(build.getWorkspace(), glob);

      if (files.isEmpty()) 
      {
    	  if (build.getResult().isWorseThan(Result.UNSTABLE)) {
    		  return true;
    	  }
    	  
    	  build.setResult(Result.FAILURE);
    	  logger.println("Performance: no " + parser.getReportName()
    			  + " files matching '" + glob
    			  + "' have been found. Has the report been generated?. Setting Build to "
    			  + build.getResult());
    	  return true;
      }

      List<File> localReports = copyReportsToMaster(build, logger, files, parser.getDescriptor().getDisplayName());
      Collection<PerformanceReport> parsedReports = parser.parse(build, localReports, listener);
      
      for (PerformanceReport r : parsedReports) {
        r.setBuildAction(a);
      }
    }

    return true;
  }

  private List<File> copyReportsToMaster(AbstractBuild<?, ?> build,
      PrintStream logger, List<FilePath> files, String parserDisplayName)
      throws IOException, InterruptedException {
    List<File> localReports = new ArrayList<File>();
    for (FilePath src : files) {
      final File localReport = getPerformanceReport(build, parserDisplayName,
          src.getName());
      if (src.isDirectory()) {
        logger.println("Performance: File '" + src.getName()
            + "' is a directory, not a Performance Report");
        continue;
      }
      src.copyTo(new FilePath(localReport));
      localReports.add(localReport);
    }
    return localReports;
  }

  public Object readResolve() {
    // data format migration
    if (parsers == null)
      parsers = new ArrayList<GenericReportParser>();
    /*if (filename != null) {
      parsers.add(new SlingPerformanceParser(filename));
      filename = null;
    }*/
    return this;
  }

}
