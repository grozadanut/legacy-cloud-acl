package ro.linic.cloud.command;

import lombok.Data;

@Data
public class ChangeNameCommand {
	Integer companyId;
	Integer productId;
	String barcode;
	String name;
}
