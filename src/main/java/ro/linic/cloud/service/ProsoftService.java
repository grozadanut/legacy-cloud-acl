package ro.linic.cloud.service;

import java.time.LocalDate;

public interface ProsoftService {
	public byte[] invoices(LocalDate from, LocalDate to);
	public String outgInvoices_Xml(LocalDate from, LocalDate to);
	public String incInvoices_Xml(LocalDate from, LocalDate to);
}
