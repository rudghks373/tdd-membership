package com.study.tddmembership.membership.dto;

import com.study.tddmembership.enums.MembershipType;
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
