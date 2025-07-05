package com.bytespeed.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bytespeed.ecom.entity.ContactEntity;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

    @Query(value = "SELECT * FROM contact WHERE email = :email AND phone_number = :phoneNumber AND deleted_at IS NULL", nativeQuery = true)
    List<ContactEntity> findExactMatch(@Param("email") String email, @Param("phoneNumber") String phoneNumber);

    @Query(value = """
        SELECT * FROM contact 
        WHERE deleted_at IS NULL AND 
        ((email = :email AND (phone_number IS NULL OR phone_number <> :phoneNumber)) 
        OR (phone_number = :phoneNumber AND (email IS NULL OR email <> :email)))
        """, nativeQuery = true)
    List<ContactEntity> findByEitherEmailOrPhoneOnly(@Param("email") String email, @Param("phoneNumber") String phoneNumber);

    @Query(value = """
        SELECT * FROM contact 
        WHERE id = :id OR linked_id = :id OR id = (SELECT linked_id FROM contact WHERE id = :id) OR linked_id = (SELECT linked_id FROM contact WHERE id = :id)
        """, nativeQuery = true)
    List<ContactEntity> findAllRelatedContacts(@Param("id") Long id);
}
