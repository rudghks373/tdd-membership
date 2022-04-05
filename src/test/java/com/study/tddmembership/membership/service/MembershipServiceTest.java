package com.study.tddmembership.membership.service;

import com.study.tddmembership.membership.domain.Membership;
import com.study.tddmembership.membership.exception.MembershipErrorResult;
import com.study.tddmembership.membership.exception.MembershipException;
import com.study.tddmembership.membership.repository.MembershipRepository;
import com.study.tddmembership.membership.type.MembershipType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author 공통플랫폼팀 오경환
 * @version 1.0
 *     <pre> 2022.04.05 : 최초 작성 </pre>
 *
 * @since 2022.04.05
 */
@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

  private final String userId = "userId";
  private final MembershipType membershipType = MembershipType.NAVER;
  private final Integer point = 10000;

  @InjectMocks
  private MembershipService target;

  @Mock
  private MembershipRepository membershipRepository;

  @Test
  public void 맴버쉽등록실패_이미존재함(){
    //given
    doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId,membershipType);

    final MembershipException result =
        assertThrows(MembershipException.class, () -> target.addMembership(userId, membershipType, point));

    assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);

  }
  
}
