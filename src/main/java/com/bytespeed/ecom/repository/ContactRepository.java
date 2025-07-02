package com.bytespeed.ecom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bytespeed.ecom.entity.ContactEntity;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

	@Query(value = "SELECT * FROM contact WHERE (phone_number = :phoneNumber OR email = :email) AND link_precedence = :linkPrecedence", nativeQuery = true)
	Optional<ContactEntity> findByPhoneNumberOrEmail(@Param("phoneNumber") String phoneNumber,
			@Param("email") String email, @Param("linkPrecedence") String linkPrecedence);
}
