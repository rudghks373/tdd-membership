package com.study.tddmembership.membership.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author 공통플랫폼팀 오경환
 * @version 1.0 <pre> 2022.04.05 : 최초 작성 </pre>
 * @since 2022.04.05
 */
@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {

	DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
	;

	private final HttpStatus httpStatus;
	private final String message;

}
