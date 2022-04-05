package com.study.tddmembership.membership.repository;

import com.study.tddmembership.membership.domain.Membership;
import com.study.tddmembership.membership.type.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 공통플랫폼팀 오경환
 * @version 1.0
 *     <pre> 2022.04.05 : 최초 작성 </pre>
 *
 * @since 2022.04.05
 */
public interface MembershipRepository extends JpaRepository<Membership, Long> {

	Membership findByUserIdAndMembershipType(String userId, MembershipType naver);
}