package com.study.tddmembership.membership.controller;

import static com.study.tddmembership.membership.constants.MembershipConstants.USER_ID_HEADER;
import com.study.tddmembership.membership.request.MembershipRequest;
import com.study.tddmembership.membership.response.MembershipDetailResponse;
import com.study.tddmembership.membership.response.MembershipResponse;
import com.study.tddmembership.membership.service.MembershipService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    final MembershipResponse membershipResponse =
        membershipService.addMembership(
            userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

    return ResponseEntity.status(HttpStatus.CREATED).body(membershipResponse);
  }

  @GetMapping("/api/v1/memberships")
  public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
      @RequestHeader(USER_ID_HEADER) final String userId) {
    return ResponseEntity.ok(membershipService.getMembershipList(userId));
  }
}
