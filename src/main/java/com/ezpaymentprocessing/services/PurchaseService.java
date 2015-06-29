package com.ezpaymentprocessing.services;

import com.ezpaymentprocessing.model.PurchaseResponse;

/** 
 * VERY Simple service class for qualifying a purchase
 * 
 * 
 * @author E. Getchell
 *
 */
public class PurchaseService {
	
	/**
	 * Hard coded logic - anything over 2000 will be rejected.
	 * @param amount
	 * @return PurchaseResponse indicating if the purchase was approved and an error message if deined
	 */
	public static PurchaseResponse execute(int amount)
	{
		PurchaseResponse purchaseResponse = new PurchaseResponse();
		
		if (amount > 2000)
		{
			purchaseResponse.setApproved(false);
			purchaseResponse.setMessage("Purchase Denied - Limit of $2,000 exceeded");
		}
		else
		{
			purchaseResponse.setApproved(true);
		}
		
		return purchaseResponse;
		
		
	}

}
