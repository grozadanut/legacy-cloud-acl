package ro.linic.cloud.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import ro.linic.cloud.command.ChangeNameCommand;
import ro.linic.cloud.command.ChangePriceCommand;
import ro.linic.cloud.command.ChangeStockCommand;
import ro.linic.cloud.command.CreateProductCommand;
import ro.linic.cloud.command.DeleteProductCommand;
import ro.linic.cloud.entity.Company;
import ro.linic.cloud.entity.Product;
import ro.linic.cloud.entity.ProductGestiuneMapping;

public class ProductCommandMapperTest {
	
	@Test
	public void toChangePriceCommand() throws Exception {
		// given
		final Company company = new Company();
		company.setId(1);
		final Product p = new Product();
		p.setCompany(company);
		p.setId(30);
		p.setPricePerUom(new BigDecimal("55.20"));
		
		// when
		final ChangePriceCommand command = ProductCommandMapper.INSTANCE.toChangePriceCommand(p);
		
		// then
		assertThat(command.getCompanyId()).isEqualTo(1);
		assertThat(command.getProductId()).isEqualTo(30);
		assertThat(command.getPricePerUom()).isEqualByComparingTo(new BigDecimal("55.2"));
	}
	
	@Test
	public void toChangePriceCommandNullFields() throws Exception {
		// given
		final Product p = new Product();
		p.setCompany(null);
		p.setId(null);
		p.setPricePerUom(null);
		
		// when
		final ChangePriceCommand command = ProductCommandMapper.INSTANCE.toChangePriceCommand(p);
		
		// then
		assertThat(command.getCompanyId()).isNull();
		assertThat(command.getProductId()).isNull();
		assertThat(command.getPricePerUom()).isNull();
	}
	
	@Test
	public void toChangePriceCommandNull() throws Exception {
		// when
		final ChangePriceCommand command = ProductCommandMapper.INSTANCE.toChangePriceCommand(null);
		
		// then
		assertThat(command).isNull();
	}
	
	@Test
	public void toChangeStockCommand() throws Exception {
		// given
		final Company company = new Company();
		company.setId(1);
		final Product p = new Product();
		p.setCompany(company);
		p.setId(30);
		final ProductGestiuneMapping mapping1 = new ProductGestiuneMapping();
		mapping1.setStoc(new BigDecimal("10"));
		p.getStocuri().add(mapping1);
		final ProductGestiuneMapping mapping2 = new ProductGestiuneMapping();
		mapping2.setStoc(new BigDecimal("20"));
		p.getStocuri().add(mapping2);
		
		// when
		final ChangeStockCommand command = ProductCommandMapper.INSTANCE.toChangeStockCommand(p);
		
		// then
		assertThat(command.getCompanyId()).isEqualTo(1);
		assertThat(command.getProductId()).isEqualTo(30);
		assertThat(command.getStock()).isEqualByComparingTo(new BigDecimal("30"));
	}
	
	@Test
	public void toDeleteProductCommand() throws Exception {
		// given
		final Company company = new Company();
		company.setId(1);
		final Product p = new Product();
		p.setCompany(company);
		p.setId(35);
		
		// when
		final DeleteProductCommand command = ProductCommandMapper.INSTANCE.toDeleteProductCommand(p);
		
		// then
		assertThat(command.getCompanyId()).isEqualTo(1);
		assertThat(command.getProductId()).isEqualTo(35);
	}
	
	@Test
	public void toCreateProductCommand() throws Exception {
		// given
		final Company company = new Company();
		company.setId(1);
		final Product p = new Product();
		p.setCompany(company);
		p.setId(35);
		p.setBarcode("1235");
		p.setName("cement");
		p.setUom("buc");
		p.setPricePerUom(new BigDecimal("29.5"));
		
		// when
		final CreateProductCommand command = ProductCommandMapper.INSTANCE.toCreateProductCommand(p);
		
		// then
		assertThat(command.getCompanyId()).isEqualTo(1);
		assertThat(command.getProductId()).isEqualTo(35);
		assertThat(command.getBarcode()).isEqualTo("1235");
		assertThat(command.getName()).isEqualTo("cement");
		assertThat(command.getUom()).isEqualTo("buc");
		assertThat(command.getPricePerUom()).isEqualByComparingTo(new BigDecimal("29.5"));
	}
	
	@Test
	public void toChangeNameCommand() throws Exception {
		// given
		final Company company = new Company();
		company.setId(1);
		final Product p = new Product();
		p.setCompany(company);
		p.setId(35);
		p.setBarcode("12365");
		p.setName("white cement");
		
		// when
		final ChangeNameCommand command = ProductCommandMapper.INSTANCE.toChangeNameCommand(p);
		
		// then
		assertThat(command.getCompanyId()).isEqualTo(1);
		assertThat(command.getProductId()).isEqualTo(35);
		assertThat(command.getBarcode()).isEqualTo("12365");
		assertThat(command.getName()).isEqualTo("white cement");
	}
}
