package com.study.tddmembership.membership.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 공통플랫폼팀 오경환
 * @version 1.0
 *     <pre> 2022.04.05 : 최초 작성 </pre>
 *
 * @since 2022.04.05
 */
@Getter
@RequiredArgsConstructor
public enum MembershipType {
  NAVER("네이버"),
  LINE("라인"),
  KAKAO("카카오"),
  ;

  private final String companyName;
}
