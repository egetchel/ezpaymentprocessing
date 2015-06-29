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

/** Endpoint allowing remote services (in this case, the per-merchant promotion service) to register with 
 * the EZPaymentProcessing application. Once registered, if an incoming purchase request matches the dynamic endpoints
 * based on merchant id, the purchase information will be pass along to the remote service for promotion qualification 
 * will be fired
 * 
 */
@Path ("/")
@Produces("application/json")
public class GearRegistrationEndpoint {
	
	/**
	 * GET representation for a remote registration.
	 * TODO: this should be removed as a GET should not modify application state.  However, I left it in for ease of testing
	 * Example: http://localhost:8080/ezpaymentprocessing/rest/registerGear?merchantId=monetizationservice&promotionUrl=http://localhost:8081/monetizationservice/rest/qualifyPromotion/
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
