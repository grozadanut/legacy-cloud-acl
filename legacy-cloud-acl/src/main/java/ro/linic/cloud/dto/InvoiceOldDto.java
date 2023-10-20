package ro.linic.cloud.dto;

import lombok.Data;
import ro.linic.cloud.entity.AccountingDocument;

@Data
public class InvoiceOldDto {
	final private AccountingDocument accDoc;
	final private String seriaFactura;
	final private String firmaName;
	final private String firmaCui;
	final private String firmaRegCom;
	final private String firmaCapSocial;
	final private String firmaBillingAddressStreet;
	final private String firmaBillingAddressCity;
	final private String firmaBillingAddressCodJudet;
	final private String firmaPhone;
	final private String firmaEmail;
	final private String firmaIban;
}
