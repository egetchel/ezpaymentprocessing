package com.ezpaymentprocessing.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.ezpaymentprocessing.utils.PaymentProcessingConfigManager;

/** Class to allow remote gears to register with this service 

 http://localhost:8080/ezpaymentprocessing/rest/registerGear?gearName=asdf
 
 */
@Path ("/registerGear")
@Produces("application/json")

public class GearRegistrationEndpoint {
	
	@GET
	public Response registerGear(@QueryParam("merchantId") String merchantId, @QueryParam("promotionUrl") String promotionUrl)
	{
		System.out.println("Registering Merchant: " + merchantId + " on host "+ promotionUrl);
		PaymentProcessingConfigManager.addMonetizationServer(merchantId, promotionUrl);
		return Response.status(200).entity("OK").build();
	}

}
