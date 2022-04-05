package com.study.tddmembership.membership.domain;

import com.study.tddmembership.membership.type.MembershipType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author 공통플랫폼팀 오경환
 * @version 1.0
 *     <pre> 2022.04.05 : 최초 작성 </pre>
 *
 * @since 2022.04.05
 */
@Entity
@Table
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Membership {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private MembershipType membershipType;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Integer point;

  @CreationTimestamp
  @Column(nullable = false, length = 20, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(length = 20)
  private LocalDateTime updatedAt;
}
