package com.study.tddmembership.point.service;

import org.springframework.stereotype.Service;

@Service
public class RatePointService implements PointService {

	private static final int POINT_RATE = 1;

	@Override
	public int calculateAmount(int price) {
		return price * POINT_RATE / 100;
	}
}
