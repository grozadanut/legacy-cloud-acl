package ro.linic.cloud.service;

import ro.linic.cloud.pojo.Invoice;

public interface BillingService {
	public Invoice getInvoice(final Long invoiceId);
}
