package com.study.tddmembership.membership.response;

import com.study.tddmembership.membership.type.MembershipType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

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
