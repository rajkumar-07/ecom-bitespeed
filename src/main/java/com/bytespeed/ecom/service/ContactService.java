package com.bytespeed.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bytespeed.ecom.dto.ContactReqDto;
import com.bytespeed.ecom.dto.ContactResponseDto;
import com.bytespeed.ecom.entity.ContactEntity;
import com.bytespeed.ecom.repository.ContactRepository;

import static io.micrometer.common.util.StringUtils.isEmpty;

import static java.util.Objects.isNull;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static com.bytespeed.ecom.constants.CommonConstants.PRIMARY;

@Service
public class ContactService {

	@Autowired
	ContactRepository contactRepository;

	public ResponseEntity<Object> getOrAddContactDetail(ContactReqDto requestDto) {
		if (isNull(requestDto) || (isEmpty(requestDto.getEmail()) && isEmpty(requestDto.getPhoneNumber()))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Kindly provide valid request body data");
		}
		return ResponseEntity.status(HttpStatus.OK).body(findDataExistence(requestDto));
	}
	
	private Map<String,ContactResponseDto> findDataExistence(ContactReqDto requestDto){
		Optional<ContactEntity> contactDetail =  contactRepository
		.findByPhoneNumberOrEmail(requestDto.getPhoneNumber(), requestDto.getEmail(), "primary");
		Map<String,ContactResponseDto> responseMap = new HashMap<>();
		if(contactDetail.isPresent()) {
			return null;
		}else {
			responseMap.put("contact", createPrimaryEntry(requestDto));
			return responseMap;
		}
	}
	
	private ContactResponseDto createPrimaryEntry(ContactReqDto requestDto) {
		ContactEntity contactEntity = ContactEntity.builder()
				.phoneNumber(requestDto.getPhoneNumber())
				.email(requestDto.getEmail())
				.linkPrecedence(PRIMARY)
				.createdAt(OffsetDateTime.now(ZoneId.of("Asia/Kolkata"))).build();
		ContactEntity savedData = contactRepository.save(contactEntity);
		ContactResponseDto contactResDto = ContactResponseDto.builder().primaryContatctId(savedData.getId())
				.phoneNumbers(List.of(savedData.getPhoneNumber())).emails(List.of(savedData.getEmail()))
				.secondaryContactIds(List.of()).build();
		return contactResDto;
		
	}
	
	public ResponseEntity<Object> testDB(ContactReqDto reqDto) {
		return ResponseEntity.status(HttpStatus.OK).body(contactRepository
				.findByPhoneNumberOrEmail(reqDto.getPhoneNumber(), reqDto.getEmail(), PRIMARY).get());
	}

}
