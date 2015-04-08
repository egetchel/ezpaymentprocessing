package com.ezpaymentprocessing.endpoints;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;



//import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import javax.ws.rs.core.UriBuilder;

import com.ezpaymentprocessing.model.PurchaseRequest;
import com.ezpaymentprocessing.model.PurchaseResponse;

@Path ("/purchase")
public class PurchaseRestService {
	
	@GET 
	@Path("/{param}")
	@Produces("application/json")
	public Response purchase(@PathParam("param") String amountParam, @Context HttpServletRequest request ) {
		System.out.println("[GET] PurchaseRequest: " + amountParam + " for contextPath: " + request.getContextPath());
		int purchaseAmount = Integer.parseInt(amountParam);
		PurchaseResponse purchaseResponse = PurchaseService.execute(purchaseAmount);
		
		return Response.status(200).entity(purchaseResponse).build();
 
	}
	
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response purchase(PurchaseRequest purchaseRequest, @Context HttpServletRequest request) 
	{
		System.out.println("[POST] PurchaseRequest: " + purchaseRequest + " for contextPath: " + request.getContextPath());
		String contextPath = request.getContextPath();
		PurchaseResponse purchaseResponse = PurchaseService.execute(purchaseRequest.getAmount());
		process(purchaseRequest.getAmount(), contextPath);
		
		return Response.status(200).entity(purchaseResponse).build();
	}
	
	private void process(int purchaseAmount, String contextPath)
	{
		System.out.println("Qualifying Promotion...\n");
		  try {
			  	if (contextPath.startsWith("/ezpaymentprocessing"))
			  	{
			  		// local deployment
			  		contextPath = "http://localhost:8080/merchantservices";
			  	}
			  	else
			  	{
			  		contextPath = "http://merchantservices-egetchel.rhcloud.com";
			  	}
			  			
				ClientRequest request = new ClientRequest(
						contextPath + "/rest/processPromotion/10");
				request.accept("application/json");
				ClientResponse<String> response = request.get(String.class);
		 
				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
				}
		 
				BufferedReader br = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(response.getEntity().getBytes())));
		 
				String output;
				System.out.println("Output from Server .... ");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
		 
			  } 
		  catch (Exception E)
		  {
			  E.printStackTrace();
		  }
		 
		  /*
		  catch (ClientProtocolException e) {
		 
				e.printStackTrace();
		 
			  } catch (IOException e) {
		 
				e.printStackTrace();
		 
			  } catch (Exception e) {
		 
				e.printStackTrace();
		 
			  }
*/		 
	}

	


}
