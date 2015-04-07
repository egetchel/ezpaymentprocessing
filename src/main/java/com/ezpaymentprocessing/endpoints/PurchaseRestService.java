package com.ezpaymentprocessing.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.ezpaymentprocessing.model.PurchaseRequest;
import com.ezpaymentprocessing.model.PurchaseResponse;

@Path ("/purchase")
public class PurchaseRestService {
	
	@GET 
	@Path("/{param}")
	@Produces("application/json")
	public Response purchase(@PathParam("param") String amountParam) {
		
		int purchaseAmount = Integer.parseInt(amountParam);
		PurchaseResponse purchaseResponse = PurchaseService.execute(purchaseAmount);
		return Response.status(200).entity(purchaseResponse).build();
 
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response purchase(PurchaseRequest purchaseRequest) 
	{
		System.out.println("PurchaseRequest: " + purchaseRequest);
		PurchaseResponse purchaseResponse = PurchaseService.execute(purchaseRequest.getAmount());
		
		
		return Response.status(200).entity(purchaseResponse).build();
	}
	


}
