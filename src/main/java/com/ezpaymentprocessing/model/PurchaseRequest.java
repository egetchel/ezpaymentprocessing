package com.ezpaymentprocessing.model;

public class PurchaseRequest {
	private String merchantId;
	private Integer amount;
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("Merchant Id: [");
		b.append(merchantId);
		b.append("]\nAmount: [");
		b.append(amount);
		b.append("]");
		return b.toString();
	}

}
