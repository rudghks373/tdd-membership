package com.study.tddmembership.membership.response;

import com.study.tddmembership.membership.type.MembershipType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

/**
 * The type Member response.
 *
 * @author 공통플랫폼팀 오경환
 * @version 1.0
 *     <pre> 2022.04.05 : 최초 작성 </pre>
 *
 * @since 2022.04.05
 */
@Builder
@Getter
public class MembershipResponse {
  private Long id;

  private MembershipType membershipType;

  private String userId;

  private Integer point;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
