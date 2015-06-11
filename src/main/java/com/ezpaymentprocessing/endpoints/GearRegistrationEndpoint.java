package com.ezpaymentprocessing.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.ezpaymentprocessing.model.GearRegistrationRequest;
import com.ezpaymentprocessing.model.GenericResponse;
import com.ezpaymentprocessing.utils.PaymentProcessingConfigManager;

/** Class to allow remote services to register with the EZPaymentProcessing application
 * Input is a GET method with parameters of merchant id and the promotion processing URL 
 * Example: http://localhost:8080/ezpaymentprocessing/rest/registerGear?merchantId=monetizationservice&promotionUrl=http://localhost:8081/monetizationservice/rest/qualifyPromotion/
  */
@Path ("/")
@Produces("application/json")
public class GearRegistrationEndpoint {
	
	/**
	 * GET representation for a remote registration.
	 * TODO: this should be a post.
	 * @param merchantId
	 * @param promotionUrl
	 * @return
	 */
	@GET
	@Path ("/registerGear")
	public Response registerGear(@QueryParam("merchantId") String merchantId, @QueryParam("promotionUrl") String promotionUrl)
	{
		System.out.println("[GET] Registering Gear: " + merchantId + " on host "+ promotionUrl);
		PaymentProcessingConfigManager.addMonetizationServer(merchantId, promotionUrl);
		return Response.status(200).entity(new GenericResponse()).build();
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path ("/registerGear")
	public Response registerGear(GearRegistrationRequest gearRegistrationRequest) 
	{
		System.out.println("[POST] Registering Gear: " + gearRegistrationRequest);
		PaymentProcessingConfigManager.addMonetizationServer(gearRegistrationRequest.getMerchantId(), gearRegistrationRequest.getPromotionUrl());
		return Response.status(200).entity(new GenericResponse()).build();
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path ("/unregisterGear")
	public Response unregisterGear(GearRegistrationRequest gearRegistrationRequest) 
	{
		System.out.println("[POST] Unregistering Gear: " + gearRegistrationRequest);
		PaymentProcessingConfigManager.removeMonetizationServer(gearRegistrationRequest.getMerchantId());
		return Response.status(200).entity(new GenericResponse()).build();
	}

}
