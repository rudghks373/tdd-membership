package com.study.tddmembership.membership.controller;

import com.google.gson.Gson;
import static com.study.tddmembership.membership.controller.MembershipConstants.USER_ID_HEADER;
import com.study.tddmembership.membership.exception.MembershipErrorResult;
import com.study.tddmembership.membership.exception.MembershipException;
import com.study.tddmembership.membership.response.MembershipResponse;
import com.study.tddmembership.membership.service.MembershipService;
import com.study.tddmembership.membership.type.MembershipType;
import java.nio.charset.StandardCharsets;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MembershipControllerTest {

  @InjectMocks private MembershipController target;

  @Mock private MembershipService membershipService;

  private MockMvc mockMvc;

  private Gson gson;

  @BeforeEach
  public void init() {
    gson = new Gson();
    mockMvc =
        MockMvcBuilders.standaloneSetup(target)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  @DisplayName("멤버십등록성공")
  void membershipAddSuccess() throws Exception{
    // given
    final String url = "/api/v1/memberships";
    final MembershipResponse membershipResponse = MembershipResponse.builder()
        .id(-1L)
        .membershipType(MembershipType.NAVER).build();
    doReturn(membershipResponse).when(membershipService).addMembership("12345", MembershipType.NAVER, 10000);

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .header(USER_ID_HEADER,"12345")
            .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isCreated());

    final MembershipResponse response = gson.fromJson(resultActions.andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8), MembershipResponse.class);

    assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
    assertThat(response.getId()).isNotNull();
  }

  @Test
  @DisplayName("멤버십 등록 실패 사용자 식별값이 헤더에없음")
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

  @Test
  @DisplayName("멤버십등록실패 포인트가 null")
  void membershipAddFailIsPointIsNull() throws Exception {
    // given
    final String url = "/api/v1/memberships";

    // when
    final ResultActions resultActions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("멤버십등록실패 멤버십종류가 null")
  void MembershipAddFailMembershipTypeISNull() throws Exception {
    // given
    final String url = "/api/v1/memberships";

    // when
    final ResultActions resultActions;
    resultActions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, null)))
                .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  private MembershipRequest membershipRequest(
      final Integer point, final MembershipType membershipType) {
    return MembershipRequest.builder().point(point).membershipType(membershipType).build();
  }

  @Test
  @DisplayName("멤버십등록실패 MemberService 에러 Throw")
  public void membershipAddFailMemberServiceThrow() throws Exception {
    // given
    final String url = "/api/v1/memberships";
    doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
        .when(membershipService)
        .addMembership("12345", MembershipType.NAVER, 10000);

    // when
    final ResultActions resultActions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isBadRequest());
  }
}
