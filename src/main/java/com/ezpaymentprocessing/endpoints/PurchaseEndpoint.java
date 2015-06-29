package com.ezpaymentprocessing.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ezpaymentprocessing.model.PurchaseRequest;
import com.ezpaymentprocessing.model.PurchaseResponse;
import com.ezpaymentprocessing.model.GenericResponse;
import com.ezpaymentprocessing.services.PurchaseService;
import com.ezpaymentprocessing.utils.PaymentProcessingConfigManager;
import com.ezpaymentprocessing.utils.RestClient;

/**
 * Rest endpoint for executing a purchase.
 * 
 * Supports both GET and POST
 * 
 * @author E. Getchell
 */

@Path ("/purchase")
public class PurchaseEndpoint {
	
	/**
	 * GET endpoint for purchase.
	 * 
     * Sample request: http://localhost:8080/ezpaymentprocessing/rest/purchase?merchantId=monetizationservice&amount=10&mobileNumber=5556667777

	 * @param merchantId
	 * @param amount
	 * @param mobileNumber
	 * @param request
	 * @return PurchaseResponse representing the status of the purchase as a JSON structure
	 */
	@GET  
	@Produces("application/json")
	public Response purchase(
			@QueryParam("merchantId") String merchantId, 
			@QueryParam("amount") String amount,
			@QueryParam("mobileNumber") String mobileNumber,
			@Context HttpServletRequest request ) {
		System.out.println("[GET] PurchaseRequest: MerchantId[" + merchantId + "] Amount: [ " + amount + "] Mobile Phone: [" + mobileNumber +"] for contextPath: " + request.getContextPath());

		PurchaseRequest purchaseRequest = new PurchaseRequest();
		purchaseRequest.setMerchantId(merchantId);
		purchaseRequest.setAmount(Integer.parseInt(amount));
		purchaseRequest.setMobileNumber(mobileNumber);
		String contextPath = request.getContextPath();
		
		PurchaseResponse purchaseResponse = process(purchaseRequest, contextPath);
		return Response.status(200).entity(purchaseResponse).build();
 
	}
	
	/**
	 * POST endpoint for purhcase
	 * @param purchaseRequest JSON representation of a PurchaseRequest
	 * @param request
	 * @return PurchaseResponse representing the status of the purchase as a JSON structure
	 */
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response purchase(PurchaseRequest purchaseRequest, @Context HttpServletRequest request) 
	{
		System.out.println("[POST] PurchaseRequest: " + purchaseRequest + " for contextPath: " + request.getContextPath());
		String contextPath = request.getContextPath();
		
		PurchaseResponse purchaseResponse = process(purchaseRequest, contextPath);
		
		return Response.status(200).entity(purchaseResponse).build();
	}
	
	/**
	 * Process the purcase.  Rough flow:
	 * 1 - Execute the purchase
	 * 2 - If purchase is approved
	 *     - If the merchant id on the incoming request has a registered endpoint
	 *         - Pass along the purchase information for promotion qualification
	 * @param purchaseRequest
	 * @param contextPath
	 * @return
	 */
	private PurchaseResponse process(PurchaseRequest purchaseRequest, String contextPath)
	{
		Integer purchaseAmount = purchaseRequest.getAmount();
		if (purchaseAmount == null)
		{
			PurchaseResponse purchaseResponse = new PurchaseResponse();
			purchaseResponse.setApproved(false);
			purchaseResponse.setMessage("No amount specified");
			return purchaseResponse;
		}
		
		
		PurchaseResponse purchaseResponse = PurchaseService.execute(purchaseRequest.getAmount());
		if (purchaseResponse.isApproved() && purchaseRequest.getMerchantId() != null && purchaseRequest.getMerchantId().length() > 0)
		{
			System.out.println("Invoking remote promotion qualification service...\n");
			try 
			{
				String url = PaymentProcessingConfigManager.getPromotionURL(purchaseRequest.getMerchantId());
				RestClient client = new RestClient();
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("merchantId", purchaseRequest.getMerchantId()));
				parameters.add(new BasicNameValuePair("mobileNumber", purchaseRequest.getMobileNumber()));
				parameters.add(new BasicNameValuePair("amount", purchaseRequest.getAmount().toString()));
				client.sendRequest(url, parameters, HttpMethod.POST, GenericResponse.class); 
		 
		  } 
		  catch (Exception E)
		  {
			  E.printStackTrace();
		  }
		}
		return purchaseResponse;

	}

}
