package com.bytespeed.ecom.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponseDto {
	
	private Long primaryContatctId;
	
	private List<String> emails;
	
	private List<String> phoneNumbers;
	
	private List<Long> secondaryContactIds;
}
