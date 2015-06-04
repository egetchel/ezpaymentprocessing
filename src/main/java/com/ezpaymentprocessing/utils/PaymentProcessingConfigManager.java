package com.ezpaymentprocessing.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;

/**
 * Class to manage programatically defined configuration information, such as REST URLs 
 * @author E. Getchell
 *
 */
public class PaymentProcessingConfigManager 
{

	public static String PURCHASE_RESOURCE_ID = "/rest/purchase";
	public static String GEAR_REGISTRATION_RESOURCE_ID = "/rest/registerGear";
	
	private static String PAYMENT_SERVER_NAME = null;
	private static String GEAR_REGISTRATION_SERVER_NAME = null;
	
	private static String paymentProcessingURL = null;
	private static String gearRegistrationURL = null;
	private static boolean isLocal = false;
	
	private static Map <String, String> motenizationServerURLs = new HashMap<String, String>();
	
	private static Object lock = new Object();

	/**
	 * Adds a remote service to the list of available (registred) services.
	 * @param merchantId The unique id of the merchant
	 * @param promotionUrl The URL that will be called when interfacing with this merchant
	 */
	public static void addMonetizationServer(String merchantId, String promotionUrl)
	{
		System.out.println("Registering remote service endpoint: " + promotionUrl);
		synchronized (lock)
		{
			motenizationServerURLs.put(merchantId, promotionUrl);
		}
	}
	
	
	public static void generateRestUrls(String contextPath)
	{
		System.out.println("Local ContextPath: "+ contextPath);
		
		// Flags if this is running as a payment processor or as a on-demand merchant application
		boolean isPaymentProcessing = false;
		
		// Working locally has the context path start with a slash
		if (contextPath.startsWith("/"))
		{
			isLocal = true;
		}
		else
		{
			isLocal = false;
		}
		if (contextPath.contains("ezpaymentprocessing"))
		{
			isPaymentProcessing = true;
		}
		
		if (isLocal)
		{
			// Payment Server is always hard coded
			PAYMENT_SERVER_NAME = "http://localhost:8080/ezpaymentprocessing"; 
			paymentProcessingURL = PAYMENT_SERVER_NAME + PURCHASE_RESOURCE_ID;
/*			
			// If this is being started as a on-demand montenization service, then we need to build out the callback URLs
			if (! isPaymentProcessing)
			{
				PROMOTION_SERVER_NAME = "http://localhost:8081" + contextPath;
				promotionURL = PROMOTION_SERVER_NAME + PROMOTION_RESOURCE_ID;
			}
*/			
		}
		else
		{
			// Payment Server is always hard coded
			PAYMENT_SERVER_NAME = "http://ezpaymentprocessing-egetchel.rhcloud.com"; 
			paymentProcessingURL = PAYMENT_SERVER_NAME + PURCHASE_RESOURCE_ID;
/*			
			PROMOTION_SERVER_NAME = "http://monetizationservice-egetchel.rhcloud.com";
			promotionURL = PROMOTION_SERVER_NAME + PROMOTION_RESOURCE_ID;
			
			isLocal = false;
*/			
		}
		
		// now compute what the gear registration URL is.
		// for the ezpaymentprocessing application, this will be null as everyone is calling into it.
		// for all others, it will be the ezpaymentprocessing application
		if (isPaymentProcessing)
		{
			gearRegistrationURL = null;
		}
		else
		{
			// this is a monetization service being spun up, so it needs to register itself
			if (isLocal)
			{
				GEAR_REGISTRATION_SERVER_NAME = "http://localhost:8080/ezpaymentprocessing"; 
				gearRegistrationURL = GEAR_REGISTRATION_SERVER_NAME + GEAR_REGISTRATION_RESOURCE_ID;
			}
			else
			{
				GEAR_REGISTRATION_SERVER_NAME = "http://ezpaymentprocessing-egetchel.rhcloud.com";
				gearRegistrationURL = GEAR_REGISTRATION_SERVER_NAME  + GEAR_REGISTRATION_RESOURCE_ID;
			}
		}
	}

	/**
	 * Returns the fully qualified URL for the payment service.
	 * @return
	 */
	public static String getPaymentProcessingURL() {
		return paymentProcessingURL;
	}

	/**
	 * Returns the fully qualified URL for the promotion service
	 * @return
	 */
	public static String getPromotionURL(String merchantId) {
		return motenizationServerURLs.get(merchantId);
	}
	
	/**
	 * Returns the URL where applications need to register themselves to.
	 * For runnign in the ezpaymentprocessing application, this will be null (as all applications register themselves with it
	 * All others applications will point to the ezpaymentprocessing application
	 * @return
	 */
	public static String getGearRegistrationURL()
	{
		return gearRegistrationURL;
	}
	
	
	
	
	public static Map<String, String> getMotenizationServerURLs() {
		return motenizationServerURLs;
	}


	public static List<String> getLocalIPandPort() throws MalformedObjectNameException,
	NullPointerException, UnknownHostException,
	AttributeNotFoundException, InstanceNotFoundException,
	MBeanException, ReflectionException 
	{
MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"),	Query.value("HTTP/1.1")));
String hostname = InetAddress.getLocalHost().getHostName();
InetAddress[] addresses = InetAddress.getAllByName(hostname);
ArrayList<String> endPoints = new ArrayList<String>();
for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
	ObjectName obj = i.next();
	String scheme = mbs.getAttribute(obj, "scheme").toString();
	String port = obj.getKeyProperty("port");
	for (InetAddress addr : addresses) 
	{
		String host = addr.getHostAddress();
		String ep = scheme + "://" + host + ":" + port;
		endPoints.add(ep);
	}
}
return endPoints;
}
}
