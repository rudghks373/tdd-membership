package com.study.tddmembership.membership.service;

import com.study.tddmembership.membership.domain.Membership;
import com.study.tddmembership.membership.exception.MembershipErrorResult;
import com.study.tddmembership.membership.exception.MembershipException;
import com.study.tddmembership.membership.repository.MembershipRepository;
import com.study.tddmembership.membership.response.MembershipResponse;
import com.study.tddmembership.membership.type.MembershipType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {

  private final MembershipRepository membershipRepository;

  public MembershipResponse addMembership(String userId, MembershipType membershipType, Integer point) {
    final Membership result =
        membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
    if (result != null) {
      throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    final Membership membership =
        Membership.builder().userId(userId).point(point).membershipType(membershipType).build();

    final Membership savedMembership = membershipRepository.save(membership);

    return MembershipResponse.builder()
        .id(savedMembership.getId())
        .membershipType(savedMembership.getMembershipType())
        .build();
  }
}
