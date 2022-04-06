package com.study.tddmembership.membership.repository;

import com.study.tddmembership.membership.domain.Membership;
import com.study.tddmembership.membership.type.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

	Membership findByUserIdAndMembershipType(String userId, MembershipType naver);
}
