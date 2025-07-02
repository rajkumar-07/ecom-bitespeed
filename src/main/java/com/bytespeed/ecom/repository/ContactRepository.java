package com.bytespeed.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bytespeed.ecom.entity.ContactEntity;

public interface ContactRepository extends JpaRepository<ContactEntity, Long>{

}
