package com.bytespeed.ecom.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name="contact")
@AllArgsConstructor
@NoArgsConstructor
public class ContactEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String phoneNumber;
	
	private String email;
	
	private Long linkedId;
	
	private String linkPrecedence;
	
	@Column(updatable = false)
	private OffsetDateTime createdAt;
	
	@Column(insertable = false)
	private OffsetDateTime updatedAt;
	
	private OffsetDateTime deletedAt;
}
