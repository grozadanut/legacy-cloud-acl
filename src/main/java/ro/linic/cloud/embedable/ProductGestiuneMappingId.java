package ro.linic.cloud.embedable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
public class ProductGestiuneMappingId {

	@Column(name = "PRODUCT_ID")
	private Integer productId;

	@Column(name = "GESTIUNE_ID")
	private Integer gestiuneId;
}
