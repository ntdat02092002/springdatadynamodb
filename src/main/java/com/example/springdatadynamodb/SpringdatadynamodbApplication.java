package com.example.springdatadynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.example.springdatadynamodb.model.Customer;
import com.example.springdatadynamodb.model.ProductInfo;
import com.example.springdatadynamodb.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SpringdatadynamodbApplication {

	private static final Logger log = LoggerFactory.getLogger(SpringdatadynamodbApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringdatadynamodbApplication.class);
	}

	private DynamoDBMapper dynamoDBMapper;

	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
		return (args) -> {

			dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

			CreateTableRequest tableRequest = dynamoDBMapper
					.generateCreateTableRequest(Customer.class);
			tableRequest.setProvisionedThroughput(
					new ProvisionedThroughput(1L, 1L));
			amazonDynamoDB.createTable(tableRequest);

			//...

			dynamoDBMapper.batchDelete(
					(List<Customer>)repository.findAll());

			// save a few customers
			repository.save(new Customer("Jack", "Bauer"));
			repository.save(new Customer("Chloe", "O'Brian"));
			repository.save(new Customer("Kim", "Bauer"));
			repository.save(new Customer("David", "Palmer"));
			repository.save(new Customer("Michelle", "Dessler"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Customer customer : repository.findAll()) {
				log.info(customer.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Optional<Customer> customer = repository.findById("1");
			log.info("Customer found with findById(1):");
			log.info("--------------------------------");
			if(customer.isPresent())
			{
				log.info(customer.get().toString());
			}
			else
			{
				log.info("Nothing");
			}
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repository.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
			// for (Customer bauer : repository.findByLastName("Bauer")) {
			//  log.info(bauer.toString());
			// }
			log.info("");
		};
	}

}
