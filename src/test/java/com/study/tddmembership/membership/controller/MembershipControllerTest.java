package com.study.tddmembership.membership.controller;

import com.google.gson.Gson;
import com.study.tddmembership.membership.type.MembershipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

  @InjectMocks private MembershipController target;

  private MockMvc mockMvc;
  private Gson gson;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(target).build();
  }

  @Test
  @DisplayName("mockMvc가 Null 이아님")
  void mockMvcIsNotNull() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(target).build();
    assertThat(target).isNotNull();
    assertThat(mockMvc).isNotNull();
  }

  @Test
  @DisplayName("멤버십 등록 실패_사용자 식별값이 헤더에없음")
  void membershipAddFailNotHeader() throws Exception {
    // given
    final String url = "/api/v1/memberships";

    // when
    final ResultActions resultActions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  private Object membershipRequest(
      final Integer point, final MembershipType membershipType) {
    return MembershipRequest.builder().point(point).membershipType(membershipType).build();
  }
}
