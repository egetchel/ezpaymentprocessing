package com.ezpaymentprocessing.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Class to listed for the start event from the servlet container.  This is used to signal the
 * static configuration management class to determine where we are running (locally or on OpenShift) and
 * to generate endpoint URLs
 * @author egetchel
 *
 */
public class PaymentProcessingStartupListener implements ServletContextListener
{
	ServletContext context;
	public void contextInitialized(ServletContextEvent contextEvent) 
	{
		System.out.println("Context Created Event");
		context = contextEvent.getServletContext();
		String contextPath = context.getContextPath();
		
		PaymentProcessingConfigManager.generateRestUrls(contextPath);
	}
	public void contextDestroyed(ServletContextEvent contextEvent) 
	{
		context = contextEvent.getServletContext();
		System.out.println("Context Destroyed");
	}
} 

