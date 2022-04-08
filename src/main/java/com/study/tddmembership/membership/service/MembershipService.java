package com.study.tddmembership.membership.service;

import com.study.tddmembership.enums.MembershipType;
import com.study.tddmembership.membership.domain.Membership;
import com.study.tddmembership.membership.exception.MembershipErrorResult;
import com.study.tddmembership.membership.exception.MembershipException;
import com.study.tddmembership.membership.repository.MembershipRepository;
import com.study.tddmembership.membership.response.MembershipDetailResponse;
import com.study.tddmembership.membership.response.MembershipResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {

  private final MembershipRepository membershipRepository;

  public MembershipResponse addMembership(
      String userId, MembershipType membershipType, Integer point) {
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

  public List<MembershipDetailResponse> getMembershipList(String userId) {
    List<Membership> membershipList = membershipRepository.findAllByUserId(userId);
    return membershipList.stream()
        .map(v -> MembershipDetailResponse.builder()
            .id(v.getId())
            .membershipType(v.getMembershipType())
            .point(v.getPoint())
            .createdAt(v.getCreatedAt())
            .build())
        .collect(Collectors.toList());
  }
}
