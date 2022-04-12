package com.study.tddmembership.membership.service;

import com.study.tddmembership.point.service.RatePointService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RatePointServiceTest {

	@InjectMocks
	private RatePointService ratePointService;
	@Test
	@DisplayName("10000원의 적립은 100원")
	void pay10000_point100(){

		// given
		final int price = 10000;

		// when
		final int result = ratePointService.calculateAmount(price);

		// then
		assertThat(result).isEqualTo(100);

	}

	@Test
	@DisplayName("20000원의 적립은 200원")
	void pay20000_point200(){

		// given
		final int price = 20000;

		// when
		final int result = ratePointService.calculateAmount(price);

		// then
		assertThat(result).isEqualTo(200);

	}

	@Test
	@DisplayName("30000원의 적립은 300원")
	void pay30000_point300(){

		// given
		final int price = 30000;

		// when
		final int result = ratePointService.calculateAmount(price);

		// then
		assertThat(result).isEqualTo(300);

	}

}
