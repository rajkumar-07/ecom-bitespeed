package com.bytespeed.ecom.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class ContactEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String phoneNumber;
	private String email;
	private Long linkedId;
	private String linkPrecedence;

	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private OffsetDateTime deletedAt;
}
