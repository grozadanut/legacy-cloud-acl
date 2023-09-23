package ro.linic.cloud.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	private String barcode;
	private String name;
	private String uom;
	
	@Column(precision = 8, scale = 2, name = "priceperuom")
	private BigDecimal pricePerUom;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<ProductGestiuneMapping> stocuri = new HashSet<>();
	
	@JsonGetter("stocuri")
	public BigDecimal stocAllGest()
	{
		return getStocuri().stream()
				.map(ProductGestiuneMapping::getStoc)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
}