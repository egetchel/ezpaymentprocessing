package com.ezpaymentprocessing.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.ezpaymentprocessing.model.PurchaseRequest;
import com.ezpaymentprocessing.model.PurchaseResponse;
import com.ezpaymentprocessing.services.PurchaseService;
import com.ezpaymentprocessing.utils.PaymentProcessingConfigManager;
import com.ezpaymentprocessing.utils.RestClient;


@Path ("/purchase")
public class PurchaseEndpoint {
	
	// Sample URI: /purchase/query?amount=10&merchantId=xyz&mobilePhone=5556667777
	// http://localhost:8080/ezpaymentprocessing/rest/purchase?merchantId=monetizationservice&amount=10&mobileNumber=5556667777
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
	
	private PurchaseResponse process(PurchaseRequest purchaseRequest, String contextPath)
	{
		PurchaseResponse purchaseResponse = PurchaseService.execute(purchaseRequest.getAmount());
		if (purchaseResponse.isApproved())
		{
			System.out.println("Invoking remote promotion qualification service...\n");
			try 
			{
				String url = PaymentProcessingConfigManager.getPromotionURL(purchaseRequest.getMerchantId());
				RestClient client = new RestClient();
				client.sendGet(url, "merchantId="+purchaseRequest.getMerchantId()+"&mobileNumber="+purchaseRequest.getMobileNumber()+"&amount="+purchaseRequest.getAmount());
		 
		  } 
		  catch (Exception E)
		  {
			  E.printStackTrace();
		  }
		}
		return purchaseResponse;

	}

}
