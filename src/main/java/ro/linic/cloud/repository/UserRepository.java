package ro.linic.cloud.repository;

import org.springframework.data.repository.CrudRepository;

import ro.linic.cloud.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}