package ro.linic.cloud.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ro.linic.cloud.entity.Product;

@RepositoryRestResource
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, CrudRepository<Product, Integer> {

	List<Product> findByCompanyIdAndBarcode(@Param("companyId") int companyId, @Param("barcode") String barcode);
	Product findById(int id);
	List<Product> findByCompanyIdAndNameIgnoreCase(@Param("companyId") int companyId, @Param("name") String name);
}