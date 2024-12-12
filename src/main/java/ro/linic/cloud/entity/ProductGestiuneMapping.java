package ro.linic.cloud.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ro.linic.cloud.embedable.ProductGestiuneMappingId;

@Entity
@Table(name = "productgestiunemapping")
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class ProductGestiuneMapping {

	@EmbeddedId
    private ProductGestiuneMappingId id;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("gestiuneId")
    @JoinColumn(name = "GESTIUNE_ID")
    private Gestiune gestiune;
 
    @Column(precision=12, scale=4)
	private BigDecimal stoc;
}
