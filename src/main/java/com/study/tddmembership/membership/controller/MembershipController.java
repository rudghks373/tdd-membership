package com.study.tddmembership.membership.controller;

import static com.study.tddmembership.membership.constants.MembershipConstants.USER_ID_HEADER;
import com.study.tddmembership.membership.dto.MembershipRequest;
import com.study.tddmembership.membership.dto.MembershipDetailResponse;
import com.study.tddmembership.membership.dto.MembershipResponse;
import com.study.tddmembership.membership.service.MembershipService;
import com.study.tddmembership.membership.validation.ValidationGroups.MembershipAccumulateMarker;
import com.study.tddmembership.membership.validation.ValidationGroups.MembershipAddMarker;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
      @RequestBody @Validated(MembershipAddMarker.class)
          final MembershipRequest membershipRequest) {

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

  @GetMapping("/api/v1/memberships/{id}")
  private ResponseEntity<MembershipDetailResponse> getMembership(
      @RequestHeader(USER_ID_HEADER) final String userId, @PathVariable final Long id) {
    return ResponseEntity.ok(membershipService.getMembership(id, userId));
  }

  @DeleteMapping("/api/v1/memberships/{id}")
  public ResponseEntity<Void> removeMembership(
      @RequestHeader(USER_ID_HEADER) final String userId, @PathVariable final Long id) {
    membershipService.removeMembership(id, userId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/api/v1/memberships/{id}/accumulate")
  public ResponseEntity<Void> accumulateMembershipPoint(
      @RequestHeader(USER_ID_HEADER) final String userId,
      @PathVariable final Long id,
      @RequestBody @Validated(MembershipAccumulateMarker.class)
          final MembershipRequest membershipRequest) {
    membershipService.accumulateMembershipPoint(id, userId, membershipRequest.getPoint());
    return ResponseEntity.ok().build();
  }
}
