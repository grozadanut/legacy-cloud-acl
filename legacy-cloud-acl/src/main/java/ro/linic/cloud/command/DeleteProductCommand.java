package ro.linic.cloud.command;

import lombok.Data;

@Data
public class DeleteProductCommand {
	Integer companyId;
	Integer productId;
}
