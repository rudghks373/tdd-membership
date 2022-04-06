package com.study.tddmembership.membership.controller;

import com.study.tddmembership.membership.type.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {

    private final Integer point;
    private final MembershipType membershipType;

}
