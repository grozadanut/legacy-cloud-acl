package ro.linic.cloud.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ro.linic.cloud.command.ChangePriceCommand;
import ro.linic.cloud.command.ChangeStockCommand;
import ro.linic.cloud.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductCommandMapper {
	@Mapping(target = "companyId", source = "product.company.id")
    @Mapping(target = "productId", source = "product.id")
	ChangePriceCommand toChangePriceCommand(Product product);
	
	@Mapping(target = "companyId", source = "product.company.id")
	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "stock", expression = "java(product.stocAllGest())")
	ChangeStockCommand toChangeStockCommand(Product product);
}
