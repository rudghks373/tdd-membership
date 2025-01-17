package com.study.tddmembership.membership.controller;

import com.google.gson.Gson;
import com.study.tddmembership.common.GlobalExceptionHandler;
import com.study.tddmembership.enums.MembershipType;
import com.study.tddmembership.membership.exception.MembershipErrorResult;
import com.study.tddmembership.membership.exception.MembershipException;
import com.study.tddmembership.membership.dto.MembershipRequest;
import com.study.tddmembership.membership.dto.MembershipDetailResponse;
import com.study.tddmembership.membership.dto.MembershipResponse;
import com.study.tddmembership.membership.service.MembershipService;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.study.tddmembership.membership.constants.MembershipConstants.USER_ID_HEADER;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MembershipControllerTest {

  @InjectMocks private MembershipController target;

  @Mock private MembershipService membershipService;

  private MockMvc mockMvc;

  private Gson gson;

  private static Stream<Arguments> invalidMembershipAddParameter() {
    return Stream.of(
        Arguments.of(null, MembershipType.NAVER),
        Arguments.of(-1, MembershipType.NAVER),
        Arguments.of(10000, null));
  }

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
  void membershipAddSuccess() throws Exception {
    // given
    final String url = "/api/v1/memberships";
    final MembershipResponse membershipResponse =
        MembershipResponse.builder().id(-1L).membershipType(MembershipType.NAVER).build();
    doReturn(membershipResponse)
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
    resultActions.andExpect(status().isCreated());

    final MembershipResponse response =
        gson.fromJson(
            resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8),
            MembershipResponse.class);

    assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
    assertThat(response.getId()).isNotNull();
  }

  @Test
  @DisplayName("멤버십목록 조회성공")
  void membershipListSearchSuccess() throws Exception {
    // given
    final String url = "/api/v1/memberships";
    doReturn(
            Arrays.asList(
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build(),
                MembershipDetailResponse.builder().build()))
        .when(membershipService)
        .getMembershipList("12345");

    // when
    final ResultActions resultActions =
        mockMvc.perform(MockMvcRequestBuilders.get(url).header(USER_ID_HEADER, "12345"));

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  @DisplayName("멤버십상세 조회성공")
  void membershipSearchDetailSuccess() throws Exception {
    // given
    final String url = "/api/v1/memberships/123";
    doReturn(MembershipDetailResponse.builder().build())
        .when(membershipService)
        .getMembership(123L, "12345");

    // then
    final ResultActions resultActions =
        mockMvc.perform(MockMvcRequestBuilders.get(url).header(USER_ID_HEADER, "12345"));

    // then
    resultActions.andExpect(status().isOk());
  }

  @ParameterizedTest
  @MethodSource("invalidMembershipAddParameter")
  @DisplayName("멤버십등록실패 잘못된파라미터")
  void membershipAddFailWrongParameter(final Integer point, final MembershipType membershipType)
      throws Exception {
    // given
    final String url = "/api/v1/memberships";

    // when
    final ResultActions resultActions =
        mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(point, membershipType)))
                .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("멤버십등록실패 MemberService 에러 Throw")
  void membershipAddFailMemberServiceThrow() throws Exception {
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

  private MembershipRequest membershipRequest(
      final Integer point, final MembershipType membershipType) {
    return MembershipRequest.builder().point(point).membershipType(membershipType).build();
  }

  private MembershipRequest membershipRequest(final Integer point) {
    return MembershipRequest.builder()
        .point(point)
        .build();
  }

  @Test
  @DisplayName("멤버십목록 조회실패 사용자식별값이 헤더에없음")
  void membershipSearchFailByNullHeader() throws Exception {
    // given
    final String url = "/api/v1/memberships";

    // when
    final ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url));

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("멤버십상세조회실패 멤버십이 존재하지않음")
  void membershipSearchDetailFailByNull() throws Exception {

    // given
    final String url = "/api/v1/memberships/123";
    doThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND))
        .when(membershipService)
        .getMembership(123L, "12345");

    // when
    final ResultActions resultActions =
        mockMvc.perform(MockMvcRequestBuilders.get(url).header(USER_ID_HEADER, "12345"));

    // then
    resultActions.andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("멤버십삭제실패 사용자식별값이헤더에없음")
  void membershipDeleteFailByNotHeader() throws Exception {
    // given
    final String url = "/api/v1/memberships/-1";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.delete(url)
    );

    // then
    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("멤버십삭제성공")
  void membershipDeleteSuccess() throws Exception {
    // given
    final String url = "/api/v1/memberships/-1";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.delete(url)
            .header(USER_ID_HEADER, "12345")
    );

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  @DisplayName("멤버십적립성공")
  void membershipCollectSuccess() throws Exception {

    // given
    final String url = "/api/v1/memberships/-1/accumulate";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .header(USER_ID_HEADER, "12345")
            .content(gson.toJson(membershipRequest(10000)))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isOk());
  }

  @Test
  @DisplayName("멤버십적립실패 사용자식별값이헤더에없음")
  void membershipCollectFailByNotHeader() throws Exception {

    // given
    final String url = "/api/v1/memberships/-1/accumulate";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .content(gson.toJson(membershipRequest(10000)))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest());

  }

  @Test
  @DisplayName("멤버십적립실패 포인트가음수")
  void membershipCollectFailByPointMinus() throws Exception {

    // given
    final String url = "/api/v1/memberships/-1/accumulate";

    // when
    final ResultActions resultActions = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .header(USER_ID_HEADER,"12345")
            .content(gson.toJson(membershipRequest(-1,MembershipType.NAVER)))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest());
  }


}
