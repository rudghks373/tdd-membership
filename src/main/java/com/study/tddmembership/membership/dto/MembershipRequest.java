package com.study.tddmembership.membership.dto;

import com.study.tddmembership.enums.MembershipType;
import com.study.tddmembership.membership.validation.ValidationGroups.MembershipAccumulateMarker;
import com.study.tddmembership.membership.validation.ValidationGroups.MembershipAddMarker;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {

    @NotNull(groups = {MembershipAddMarker.class, MembershipAccumulateMarker.class})
    @Min(value = 0, groups = {MembershipAddMarker.class, MembershipAccumulateMarker.class})
    private final Integer point;

    @NotNull(groups = {MembershipAddMarker.class})
    private final MembershipType membershipType;

}
