package com.study.tddmembership.membership.repository;

import com.study.tddmembership.enums.MembershipType;
import com.study.tddmembership.membership.entity.Membership;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

	Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);

	List<Membership> findAllByUserId(String userId);
}
