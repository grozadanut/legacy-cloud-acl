package ro.linic.cloud.repository;

import org.springframework.data.repository.CrudRepository;

import ro.linic.cloud.entity.Company;

public interface CompanyRepository extends CrudRepository<Company, Integer> {

}