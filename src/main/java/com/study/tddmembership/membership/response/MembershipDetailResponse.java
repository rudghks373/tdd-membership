package com.study.tddmembership.membership.response;

import com.study.tddmembership.enums.MembershipType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MembershipDetailResponse {

	private final Long id;
	private final MembershipType membershipType;
	private final LocalDateTime createdAt;
	private final Integer point;

}