package ro.linic.cloud.command;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreateProductCommand {
	Integer companyId;
	Integer productId;
	String barcode;
	String name;
	String uom;
	BigDecimal pricePerUom;
}
