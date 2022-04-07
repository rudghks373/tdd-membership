package com.study.tddmembership.membership.controller;

import static com.study.tddmembership.membership.controller.MembershipConstants.USER_ID_HEADER;
import com.study.tddmembership.membership.response.MembershipResponse;
import com.study.tddmembership.membership.service.MembershipService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MembershipController {

  private final MembershipService membershipService;

  @PostMapping("/api/v1/memberships")
  public ResponseEntity<MembershipResponse> addMembership(
      @RequestHeader(USER_ID_HEADER) final String userId,
      @RequestBody @Valid final MembershipRequest membershipRequest) {

    membershipService.addMembership(
        userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
