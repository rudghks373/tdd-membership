package com.study.tddmembership.membership.repository;

import com.study.tddmembership.enums.MembershipType;
import com.study.tddmembership.membership.domain.Membership;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MembershipRepositoryTest {

  @Autowired private MembershipRepository membershipRepository;

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
  @DisplayName("멤버십이 존재하는지 테스트")
  void membershipIsNotNull() {
    // given
    final Membership membership =
        Membership.builder()
            .userId("userId")
            .membershipType(MembershipType.NAVER)
            .point(10000)
            .build();

    // when
    membershipRepository.save(membership);
    final Membership findResult =
        membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);

    // then
    assertThat(findResult).isNotNull();
    assertThat(findResult.getId()).isNotNull();
    assertThat(findResult.getUserId()).isEqualTo("userId");
    assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
    assertThat(findResult.getPoint()).isEqualTo(10000);
  }

  @Test
  @DisplayName("멤버십조회 사이즈가 0")
  void membershipBySizeZero() {
    // given

    // when
    List<Membership> result = membershipRepository.findAllByUserId("userId");

    // then
    assertThat(result.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("멤버십조회 사이즈가 2")
  void membershipBySizeTwo() {
    // given
    final Membership naverMembership =
        Membership.builder()
            .userId("userId")
            .membershipType(MembershipType.NAVER)
            .point(10000)
            .build();

    final Membership kakaoMembership =
        Membership.builder()
            .userId("userId")
            .membershipType(MembershipType.KAKAO)
            .point(10000)
            .build();
    membershipRepository.save(naverMembership);
    membershipRepository.save(kakaoMembership);

    // when
    List<Membership> result = membershipRepository.findAllByUserId("userId");

    // then
    assertThat(result.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("멤버십 추가후 삭제")
  void membershipDeleteById(){

    // given
    final Membership naverMembership = Membership.builder()
        .userId("userId")
        .membershipType(MembershipType.NAVER)
        .point(10000)
        .build();

    // when
    final Membership savedMembership = membershipRepository.save(naverMembership);

    // then
  }
}
