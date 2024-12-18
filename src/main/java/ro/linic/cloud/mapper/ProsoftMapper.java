package ro.linic.cloud.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import ro.linic.cloud.entity.Operatiune;
import ro.linic.cloud.pojo.Achizitii;
import ro.linic.cloud.pojo.Livrari;

@Mapper
public interface ProsoftMapper {
	ProsoftMapper INSTANCE = Mappers.getMapper(ProsoftMapper.class);
	
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

	@Mapping(target = "type", constant = "GESTIUNE-OUT")
    @Mapping(target = "company", source = "op.company.name")
    @Mapping(target = "warehouse", source = "op.gestiune.importName")
    @Mapping(target = "partner", source = "op.accDoc.partner.name")
    @Mapping(target = "partnerCif", source = "op.accDoc.partner.codFiscal")
    @Mapping(target = "invoiceNumber", source = "op.accDoc.nrDoc")
    @Mapping(target = "invoiceDate", source = "op.accDoc.dataDoc", qualifiedByName = "displayDate")
    @Mapping(target = "currency", constant = "LEI")
    @Mapping(target = "fxRate", constant = "1.0000")
    @Mapping(target = "stvai", constant = "0")
    @Mapping(target = "category", source = "op.categorie")
    @Mapping(target = "taxRate", source = "op", qualifiedByName = "extractSaleTaxRate")
    @Mapping(target = "barcode", source = "op.barcode")
    @Mapping(target = "name", source = "op.name")
    @Mapping(target = "uom", source = "op.uom")
    @Mapping(target = "quantity", source = "op.cantitate")
    @Mapping(target = "price", source = "op.pretVanzareUnitarCuTVA")
    @Mapping(target = "lineAmount", source = "op.valoareVanzareFaraTVA")
    @Mapping(target = "taxAmount", source = "op.valoareVanzareTVA")
	@Mapping(target = "indexSpv", source = "indexSpv")
    Livrari toOutgLine(Operatiune op, String indexSpv);
	
	@Mapping(target = "type", constant = "GESTIUNE-IN")
    @Mapping(target = "company", source = "op.company.name")
    @Mapping(target = "warehouse", source = "op.gestiune.importName")
    @Mapping(target = "partner", source = "op.accDoc.partner.name")
    @Mapping(target = "partnerCif", source = "op.accDoc.partner.codFiscal")
    @Mapping(target = "invoiceNumber", source = "op.accDoc.nrDoc")
    @Mapping(target = "invoiceDate", source = "op.accDoc.dataDoc", qualifiedByName = "displayDate")
    @Mapping(target = "currency", constant = "LEI")
    @Mapping(target = "fxRate", constant = "1.0000")
    @Mapping(target = "stvai", constant = "0")
    @Mapping(target = "category", source = "op.categorie")
    @Mapping(target = "taxRate", source = "op", qualifiedByName = "extractPurchaseTaxRate")
    @Mapping(target = "barcode", source = "op.barcode")
    @Mapping(target = "name", source = "op.name")
    @Mapping(target = "uom", source = "op.uom")
    @Mapping(target = "quantity", source = "op.cantitate")
	@Mapping(target = "purchasePrice", source = "op.pretUnitarAchizitieFaraTVA")
    @Mapping(target = "purchaseAmount", source = "op.valoareAchizitieFaraTVA")
    @Mapping(target = "purchaseTaxAmount", source = "op.valoareAchizitieTVA")
	@Mapping(target = "price", source = "op.pretVanzareUnitarCuTVA")
    @Mapping(target = "lineAmount", source = "op.valoareVanzareFaraTVA")
    @Mapping(target = "taxAmount", source = "op.valoareVanzareTVA")
	Achizitii toIncLine(Operatiune op);
    
    @Named("displayDate")
	default String displayDate(final LocalDateTime date) {
		if (date == null) {
			return null;
		}
		
        return date.format(DATE_TIME_FORMATTER) + date.atZone(ZoneId.of("Europe/Bucharest")).getOffset().toString();
    }
    
    @Named("extractSaleTaxRate")
	default String extractSaleTaxRate(final Operatiune op) {
		if (op == null) {
			return null;
		}
		
		return op.getVanzareTvaPercentCalculated().multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_EVEN).toString();
    }
    
    @Named("extractPurchaseTaxRate")
    default String extractPurchaseTaxRate(final Operatiune op) {
    	if (op == null) {
    		return null;
    	}

    	return op.getAchizitieTvaPercentCalculated().multiply(new BigDecimal("100")).setScale(0, RoundingMode.HALF_EVEN).toString();
    }
}
