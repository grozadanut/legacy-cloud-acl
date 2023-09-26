package ro.linic.cloud.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ro.linic.cloud.command.ChangeNameCommand;
import ro.linic.cloud.command.ChangePriceCommand;
import ro.linic.cloud.command.ChangeStockCommand;
import ro.linic.cloud.command.CreateProductCommand;
import ro.linic.cloud.command.DeleteProductCommand;
import ro.linic.cloud.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductCommandMapper {
	ProductCommandMapper INSTANCE = Mappers.getMapper(ProductCommandMapper.class);
	
	@Mapping(target = "companyId", source = "product.company.id")
    @Mapping(target = "productId", source = "product.id")
	ChangePriceCommand toChangePriceCommand(Product product);
	
	@Mapping(target = "companyId", source = "product.company.id")
	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "stock", expression = "java(product.stocAllGest())")
	ChangeStockCommand toChangeStockCommand(Product product);
	
	@Mapping(target = "companyId", source = "product.company.id")
	@Mapping(target = "productId", source = "product.id")
	DeleteProductCommand toDeleteProductCommand(Product product);
	
	@Mapping(target = "companyId", source = "product.company.id")
	@Mapping(target = "productId", source = "product.id")
	CreateProductCommand toCreateProductCommand(Product product);
	
	@Mapping(target = "companyId", source = "product.company.id")
	@Mapping(target = "productId", source = "product.id")
	ChangeNameCommand toChangeNameCommand(Product product);
}
