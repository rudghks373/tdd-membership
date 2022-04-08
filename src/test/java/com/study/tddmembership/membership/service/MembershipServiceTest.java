package com.study.tddmembership.membership.service;

import com.study.tddmembership.enums.MembershipType;
import com.study.tddmembership.membership.domain.Membership;
import com.study.tddmembership.membership.exception.MembershipErrorResult;
import com.study.tddmembership.membership.exception.MembershipException;
import com.study.tddmembership.membership.repository.MembershipRepository;
import com.study.tddmembership.membership.response.MembershipDetailResponse;
import com.study.tddmembership.membership.response.MembershipResponse;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

  private final String userId = "userId";
  private final MembershipType membershipType = MembershipType.NAVER;
  private final Integer point = 10000;

  @InjectMocks private MembershipService target;

  @Mock private MembershipRepository membershipRepository;

  @Mock private MembershipService memberService;

  @Test
  @DisplayName("맴버쉽등록실패_이미존재함")
  void memberAddFailDuplication() {
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
  @DisplayName("멤버쉽등록성공")
  void membershipAddSuccess() {
    // given
    doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
    doReturn(membership()).when(membershipRepository).save(any(Membership.class));

    // when

    final MembershipResponse result = target.addMembership(userId, membershipType, point);

    // then
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

  @Test
  @DisplayName("멤버십 목록조회")
  void membershipListSearch() {
    // given
    doReturn(
            Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()))
        .when(membershipRepository)
        .findAllByUserId(userId);

    // when
    final List<MembershipDetailResponse> result = target.getMembershipList(userId);

    // then
    assertThat(result.size()).isEqualTo(3);
  }
}
