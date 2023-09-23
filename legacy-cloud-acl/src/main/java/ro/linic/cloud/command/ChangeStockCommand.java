package ro.linic.cloud.command;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ChangeStockCommand {
	Integer companyId;
	Integer productId;
	BigDecimal stock;
}
