package ro.linic.cloud.pojo;

import lombok.Data;

@Data
public class Party {
	/**
	 * CUI
	 */
	private String vatId;
	/**
	 * The full formal name by which the Party is registered in the national registry of legal entities 
	 * or as a Taxable person or otherwise trades as a person or persons.
	 * Example value: Full Formal Seller Name LTD.
	 */
	private String registrationName;
	/**
	 * Party trading name
	 * A name by which the Party is known, other than Party name (also known as Business name).
	 * Example value: Seller Business Name AS
	 */
	private String businessName;
	/**
	 * Party additional legal information
	 * Additional legal information relevant for the Party.
	 * Example value: Share capital 200 Ron("Capital social 200 RON")
	 */
	private String companyLegalForm;
	private Address postalAddress;
	/**
	 * A contact point for a legal entity or person.
	 * Example value: John Wick
	 */
	private String contactName;
	private String telephone;
	private String electronicMail;
}
