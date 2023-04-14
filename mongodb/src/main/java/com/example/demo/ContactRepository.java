package com.example.demo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

public interface ContactRepository extends ReactiveMongoRepository<Contact, String> {
	Mono<Contact> findFirstByEmail(String email);

	Mono<Contact> findAllByPhoneOrName(String phoneOrName);
}
