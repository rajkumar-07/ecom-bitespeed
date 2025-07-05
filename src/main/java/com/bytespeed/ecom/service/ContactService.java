package com.bytespeed.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.bytespeed.ecom.constants.CommonConstants.PRIMARY;
import static com.bytespeed.ecom.constants.CommonConstants.SECONDARY;
import static com.bytespeed.ecom.constants.CommonConstants.CONTACT;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public ResponseEntity<Object> getOrAddContactDetail(ContactReqDto requestDto) {
        if (requestDto == null || (isEmpty(requestDto.getEmail()) && isEmpty(requestDto.getPhoneNumber()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide at least email or phone number.");
        }
        List<ContactEntity> exactMatches = contactRepository.findExactMatch(requestDto.getEmail(), requestDto.getPhoneNumber());
        if (!exactMatches.isEmpty()) {
            Long primaryId = getRootPrimaryId(exactMatches.get(0));
            List<ContactEntity> allContacts = contactRepository.findAllRelatedContacts(primaryId);
            return ResponseEntity.ok(Map.of(CONTACT, buildResponse(primaryId, allContacts)));
        }
        List<ContactEntity> partialMatches = contactRepository.findByEitherEmailOrPhoneOnly(requestDto.getEmail(), requestDto.getPhoneNumber());
        if (!partialMatches.isEmpty()) {
            ContactEntity oldestPrimary = partialMatches.stream()
                    .filter(contact -> PRIMARY.equalsIgnoreCase(contact.getLinkPrecedence()))
                    .min(Comparator.comparing(ContactEntity::getCreatedAt))
                    .orElse(partialMatches.get(0));
            ContactEntity newSecondary = ContactEntity.builder()
                    .email(requestDto.getEmail())
                    .phoneNumber(requestDto.getPhoneNumber())
                    .linkPrecedence(SECONDARY)
                    .linkedId(oldestPrimary.getId())
                    .createdAt(OffsetDateTime.now(ZoneId.of("Asia/Kolkata")))
                    .build();
            contactRepository.save(newSecondary);
            List<ContactEntity> allContacts = contactRepository.findAllRelatedContacts(oldestPrimary.getId());
            return ResponseEntity.ok(Map.of(CONTACT, buildResponse(oldestPrimary.getId(), allContacts)));
        }
        ContactEntity newPrimary = ContactEntity.builder()
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .linkPrecedence(PRIMARY)
                .createdAt(OffsetDateTime.now(ZoneId.of("Asia/Kolkata")))
                .build();
        contactRepository.save(newPrimary);
        return ResponseEntity.ok(Map.of(CONTACT, buildResponse(newPrimary.getId(), List.of(newPrimary))));
    }

    private Long getRootPrimaryId(ContactEntity contact) {
        return contact.getLinkedId() != null ? contact.getLinkedId() : contact.getId();
    }

    private ContactResponseDto buildResponse(Long primaryId, List<ContactEntity> contacts) {
        List<String> emails = contacts.stream().map(ContactEntity::getEmail).filter(Objects::nonNull).distinct().toList();
        List<String> phones = contacts.stream().map(ContactEntity::getPhoneNumber).filter(Objects::nonNull).distinct().toList();
        List<Long> secondaries = contacts.stream()
                .filter(contact -> !PRIMARY.equalsIgnoreCase(contact.getLinkPrecedence()))
                .map(ContactEntity::getId)
                .toList();
        return ContactResponseDto.builder()
                .primaryContatctId(primaryId)
                .emails(emails)
                .phoneNumbers(phones)
                .secondaryContactIds(secondaries)
                .build();
    }
}
