package com.ezpaymentprocessing.utils;

import java.util.HashMap;
import java.util.Map;

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
	private static String paymentProcessingURL = null;

	private static boolean isLocal = false;
	
	private static Map <String, String> motenizationServerURLs = new HashMap<String, String>();
	
	private static Object lock = new Object();

	/**
	 * Adds a remote service to the list of available (registered) services.
	 * @param merchantId The unique id of the merchant
	 * @param promotionUrl The URL that will be called when interfacing with this merchant
	 */
	public static void addMonetizationServer(String merchantId, String promotionUrl)
	{
		System.out.println("Registering remote service endpoint: " + promotionUrl);
		if (promotionUrl == null || merchantId == null)
		{
			return;
		}
		synchronized (lock)
		{
			motenizationServerURLs.put(merchantId, promotionUrl);
		}
	}
	
	public static void removeMonetizationServer(String merchantId)
	{
		System.out.println("Removing gear: " + merchantId);
		synchronized (lock)
		{
			motenizationServerURLs.remove(merchantId);
		}
	}
	
	
	public static void generateRestUrls(String contextPath)
	{
		System.out.println("Local ContextPath: "+ contextPath);
		
		// Flags if this is running as a payment processor or as a on-demand merchant application
		
		// Working locally has the context path starting with a slash (note, if you deploy this as the default application,
		// then this check will not work.
		if (contextPath.startsWith("/"))
		{
			isLocal = true;
		}
		else
		{
			isLocal = false;
		}
		
		if (isLocal)
		{
			// Payment Server is always hard coded
			PAYMENT_SERVER_NAME = "http://localhost:8080/ezpaymentprocessing"; 
			paymentProcessingURL = PAYMENT_SERVER_NAME + PURCHASE_RESOURCE_ID;
	
		}
		else
		{
			// Payment Server is always hard coded
			PAYMENT_SERVER_NAME = "http://ezpaymentprocessing-egetchel.rhcloud.com"; 
			paymentProcessingURL = PAYMENT_SERVER_NAME + PURCHASE_RESOURCE_ID;
			
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
	 * Returns the fully qualified URL for the remote promotion service
	 * @return
	 */
	public static String getPromotionURL(String merchantId) {
		return motenizationServerURLs.get(merchantId);
	}
	
	/**
	 * Returns all registered remote montenization (promotion) services
	 * @return
	 */
	public static Map<String, String> getMotenizationServerURLs() {
		return motenizationServerURLs;
	}

}

