package hudson.plugins.slingperformanceplugin;

import hudson.ExtensionList;
import hudson.model.Hudson;

import org.jvnet.hudson.test.HudsonTestCase;

public class SlingPerformancePublisherTest extends HudsonTestCase {

	
	public void testExtensions(){
		ExtensionList<SlingPerformancePublisher> exts = Hudson.getInstance().getExtensionList(SlingPerformancePublisher.class);
		for (SlingPerformancePublisher publisher : exts){
			System.err.print(publisher.getDescriptor().getDisplayName());
		}
		
	}
	
}
