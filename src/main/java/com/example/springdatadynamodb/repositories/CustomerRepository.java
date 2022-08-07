package com.example.springdatadynamodb.repositories;

import com.example.springdatadynamodb.model.Customer;
import com.example.springdatadynamodb.model.CustomerId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


@EnableScan
public interface CustomerRepository extends CrudRepository<Customer, CustomerId> {

    List<Customer> findByLastName(String lastName);

    Optional<Customer> findById(String id);
}