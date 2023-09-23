package ro.linic.cloud.command;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ChangePriceCommand {
	Integer companyId;
	Integer productId;
	BigDecimal pricePerUom;
}
