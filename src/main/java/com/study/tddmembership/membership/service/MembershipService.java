package com.study.tddmembership.membership.service;

import com.study.tddmembership.enums.MembershipType;
import com.study.tddmembership.membership.entity.Membership;
import com.study.tddmembership.membership.exception.MembershipErrorResult;
import com.study.tddmembership.membership.exception.MembershipException;
import com.study.tddmembership.membership.repository.MembershipRepository;
import com.study.tddmembership.membership.dto.MembershipDetailResponse;
import com.study.tddmembership.membership.dto.MembershipResponse;
import com.study.tddmembership.point.service.PointService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MembershipService {

  private final MembershipRepository membershipRepository;
  private final PointService ratePointService;

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
        .map(
            v ->
                MembershipDetailResponse.builder()
                    .id(v.getId())
                    .membershipType(v.getMembershipType())
                    .point(v.getPoint())
                    .createdAt(v.getCreatedAt())
                    .build())
        .collect(Collectors.toList());
  }

  public MembershipDetailResponse getMembership(Long membershipId, String userId) {
    final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
    final Membership membership =
        optionalMembership.orElseThrow(
            () -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
    if (!userId.equals(membership.getUserId())) {
      throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }
    return MembershipDetailResponse.builder()
        .id(membership.getId())
        .membershipType(membership.getMembershipType())
        .point(membership.getPoint())
        .createdAt(membership.getCreatedAt())
        .build();
  }

  public void removeMembership(final Long membershipId, final String userId) {
    final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);

    final Membership membership =
        optionalMembership.orElseThrow(
            () -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
    if (!membership.getUserId().equals(userId)) {
      throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }
    membershipRepository.deleteById(membershipId);
  }

  @Transactional
  public void accumulateMembershipPoint(
      final Long membershipId, final String userId, final int amount) {
      final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
      final Membership membership = optionalMembership
          .orElseThrow(()-> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
      if (!membership.getUserId().equals(userId)){
        throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
      }

      final int additionalAmount = ratePointService.calculateAmount(amount);

      //Entity
      membership.setPoint(additionalAmount + membership.getPoint());
  }
}
