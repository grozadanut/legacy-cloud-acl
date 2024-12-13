package ro.linic.cloud.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Achizitii {
	@JsonProperty("MODUL") private String type;
	@JsonProperty("Unitate") private String company;
	@JsonProperty("gestiune") private String warehouse;
	@JsonProperty("tert") private String partner;
	@JsonProperty("cif") private String partnerCif;
	@JsonProperty("Nr") private String invoiceNumber;
	@JsonProperty("Data_") private String invoiceDate;
	@JsonProperty("moneda") private String currency;
	@JsonProperty("Curs") private String fxRate;
	@JsonProperty("Stvai") private String stvai;
	@JsonProperty("Cod") private String category;
	@JsonProperty("cota_tva") private String taxRate;
	@JsonProperty("Cod_mat") private String barcode;
	@JsonProperty("Den_mat") private String name;
	@JsonProperty("UM") private String uom;
	@JsonProperty("Cantitate") private String quantity;
	@JsonProperty("Pret") private String purchasePrice;
	@JsonProperty("ValFTVA") private String purchaseAmount;
	@JsonProperty("ValTVA") private String purchaseTaxAmount;
	@JsonProperty("PretLiv") private String price;
	@JsonProperty("ValLivFTVA") private String lineAmount;
	@JsonProperty("ValLivTVA") private String taxAmount;
}
