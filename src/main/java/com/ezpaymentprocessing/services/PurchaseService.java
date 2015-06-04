package com.ezpaymentprocessing.services;

import com.ezpaymentprocessing.model.PurchaseResponse;

/** Simple service class for qualifying a purchase
 * 
 * @author E. Getchell
 *
 */
public class PurchaseService {
	
	/**
	 * Rule.
	 * @param amount
	 * @return
	 */
	public static PurchaseResponse execute(int amount)
	{
		PurchaseResponse purchaseResponse = new PurchaseResponse();
		
		if (amount > 20)
		{
			purchaseResponse.setApproved(false);
			purchaseResponse.setMessage("Purchase Limit of $20 exceeded");
		}
		else
		{
			purchaseResponse.setApproved(true);
		}
		
		return purchaseResponse;
		
		
	}

}
