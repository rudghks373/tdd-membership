package com.study.tddmembership.membership.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MembershipException extends RuntimeException{

	private final MembershipErrorResult errorResult;

}
