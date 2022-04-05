package com.study.tddmembership.membership.repository;

import com.study.tddmembership.membership.domain.Membership;
import com.study.tddmembership.membership.type.MembershipType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 공통플랫폼팀 오경환
 * @version 1.0
 *     <pre> 2022.04.05 : 최초 작성 </pre>
 *
 * @since 2022.04.05
 */
@DataJpaTest
class MembershipRepositoryTest {

  @Autowired
  private MembershipRepository membershipRepository;

  @Test
  @DisplayName("MembershipRepository null이 아니다")
  void MembershipRepositoryNotNull() {
    assertThat(membershipRepository).isNotNull();
  }

  @Test
  @DisplayName("멤버십 등록 테스트")
  void membershipAdd() {

    // given
    final Membership membership =
        Membership.builder()
            .userId("userId")
            .membershipType(MembershipType.NAVER)
            .point(10000)
            .build();

    // when
    final Membership result = membershipRepository.save(membership);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getUserId()).isEqualTo("userId");
    assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
    assertThat(result.getPoint()).isEqualTo(10000);
  }

  @Test
  void 멤버십이존재하는지테스트() {
    // given
    final Membership membership = Membership.builder()
        .userId("userId")
        .membershipType(MembershipType.NAVER)
        .point(10000)
        .build();

    // when
    membershipRepository.save(membership);
    final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);

    // then
    assertThat(findResult).isNotNull();
    assertThat(findResult.getId()).isNotNull();
    assertThat(findResult.getUserId()).isEqualTo("userId");
    assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
    assertThat(findResult.getPoint()).isEqualTo(10000);
  }

}
