package com.study.tddmembership.membership.service;

import com.study.tddmembership.membership.domain.Membership;
import com.study.tddmembership.membership.exception.MembershipErrorResult;
import com.study.tddmembership.membership.exception.MembershipException;
import com.study.tddmembership.membership.repository.MembershipRepository;
import com.study.tddmembership.membership.response.MembershipResponse;
import com.study.tddmembership.membership.type.MembershipType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

  private final String userId = "userId";
  private final MembershipType membershipType = MembershipType.NAVER;
  private final Integer point = 10000;

  @InjectMocks private MembershipService target;

  @Mock private MembershipRepository membershipRepository;

  @Test
  void 맴버쉽등록실패_이미존재함() {
    // given
    doReturn(Membership.builder().build())
        .when(membershipRepository)
        .findByUserIdAndMembershipType(userId, membershipType);

    final MembershipException result =
        assertThrows(
            MembershipException.class, () -> target.addMembership(userId, membershipType, point));

    assertThat(result.getErrorResult())
        .isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
  }

  @Test
  void 멤버쉽등록성공() {
    // given
    doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
    doReturn(membership()).when(membershipRepository).save(any(Membership.class));

    // when

    final MembershipResponse result = target.addMembership(userId, membershipType, point);

    // tehn
    assertThat(result.getId()).isNotNull();
    assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

    // verify
    verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
    verify(membershipRepository, times(1)).save(any(Membership.class));
  }

  private Membership membership() {
    return Membership.builder()
        .id(-1L)
        .userId(userId)
        .point(point)
        .membershipType(MembershipType.NAVER)
        .build();
  }
}
