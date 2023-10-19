package ro.linic.cloud.pojo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class Invoice {
	/**
	 * Database persistence id.
	 */
	private Long id;
	private List<InvoiceLine> lines;
	private String invoiceNumber;
	private Instant issueDate;
	private Instant dueDate;
	
	private Party accountingSupplier;
	private Party accountingCustomer;
	/**
	 * A group of business terms providing information about the Payee, i.e. the role that receives the payment. 
	 * Shall be used when the Payee is different from the Seller.
	 */
	private Party payeeParty;
	
	/**
	 * Invoice currency code
	 * The currency in which all Invoice amounts are given, except for the Total VAT amount in accounting currency. 
	 * Only one currency shall be used in the Invoice, except for the VAT accounting currency code (BT-6) 
	 * and the invoice total VAT amount in accounting currency (BT-111).
	 * Example value: EUR
	 */
	private String documentCurrencyCode;
	
	/**
	 * Payment means type code(UNCL4461)
	 * The means, expressed as code, for how a payment is expected to be or has been settled.
	 * Example value: 30 (Credit transfer also known as Wire transfer)
	 */
	private String paymentMeansCode;
	/**
	 * Payment account identifier
	 * A unique identifier of the financial payment account, at a payment service provider, to which payment should be made. 
	 * Such as IBAN or BBAN.
	 * Example value: NO99991122222
	 */
	private String paymentMeansId;
	/**
	 * Payment account name
	 * The name of the payment account, at a payment service provider, to which payment should be made.
	 * Example value: Payment Account
	 */
	private String paymentMeansName;
	/**
	 * Payment service provider identifier
	 * An identifier for the payment service provider where a payment account is located. 
	 * Such as a BIC or a national clearing code where required. No identification scheme Identifier to be used.
	 * Example value: 9999
	 */
	private String paymentMeansFinancialInstitutionBranch;
	
	/**
	 *  VAT accounting currency code
	 *  The currency used for VAT accounting and reporting purposes as accepted or required in the country of the Seller. 
	 *  Shall be used in combination with the Invoice total VAT amount in accounting currency (BT-111), 
	 *  when the VAT accounting currency code differs from the Invoice currency code.
	 *  Example value: SEK
	 */
	private String taxCurrencyCode;
	/**
	 * Invoice total VAT amount, Invoice total VAT amount in accounting currency
	 * The total VAT amount for the Invoice or the VAT total amount expressed in the accounting currency accepted or 
	 * required in the country of the Seller. Must be rounded to maximum 2 decimals
	 * Example value: 486.25
	 */
	private BigDecimal taxAmount;
	/**
	 * VAT BREAKDOWN
	 * A group of business terms providing information about VAT breakdown by different categories, rates and exemption reasons
	 */
	private List<TaxSubtotal> taxSubtotals;
	
	/**
	 * DOCUMENT LEVEL ALLOWANCES AND CHARGES
	 * A group of business terms providing information about allowances applicable to the Invoice as a whole. 
	 * A group of business terms providing information about charges and taxes other than VAT, applicable to the Invoice as a whole.
	 */
	private List<AllowanceCharge> allowanceCharges;
	
	/**
	 * Sum of Invoice line net amount
	 * Sum of all Invoice line net amounts in the Invoice. Must be rounded to maximum 2 decimals.
	 * Example value: 3800.0
	 */
	private BigDecimal lineExtensionAmount;
	/**
	 * Invoice total amount without VAT
	 * The total amount of the Invoice without VAT. Must be rounded to maximum 2 decimals.
	 * Example value: 3600.0
	 */
	private BigDecimal taxExclusiveAmount;
	/**
	 * Invoice total amount with VAT
	 * The total amount of the Invoice with VAT. Must be rounded to maximum 2 decimals.
	 * Example value: 4500.0
	 */
	private BigDecimal taxInclusiveAmount;
	/**
	 * Sum of allowances on document level
	 * OPTIONAL
	 * Sum of all allowances on document level in the Invoice. Must be rounded to maximum 2 decimals.
	 * Example value: 200.0
	 */
	private BigDecimal allowanceTotalAmount;
	/**
	 * Sum of charges on document level
	 * OPTIONAL
	 * Sum of all charges on document level in the Invoice. Must be rounded to maximum 2 decimals.
	 * Example value: 0.0
	 */
	private BigDecimal chargeTotalAmount;
	/**
	 * Paid amount
	 * OPTIONAL
	 * The sum of amounts which have been paid in advance. Must be rounded to maximum 2 decimals.
	 * Example value: 1000.0
	 */
	private BigDecimal prepaidAmount;
	/**
	 * Rounding amount
	 * OPTIONAL
	 * The amount to be added to the invoice total to round the amount to be paid. Must be rounded to maximum 2 decimals.
	 * Example value: 0.0
	 */
	private BigDecimal payableRoundingAmount;
	/**
	 * Amount due for payment
	 * The outstanding amount that is requested to be paid. Must be rounded to maximum 2 decimals.
	 * Example value: 3500.0
	 */
	private BigDecimal payableAmount;
	
	/**
	 * A textual note that gives unstructured information that is relevant to the Invoice as a whole. 
	 * Such as the reason for any correction or assignment note in case the invoice has been factored.
	 * Example value: Please note our new phone number 33 44 55 66
	 */
	private String note;
}
