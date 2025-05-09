package org.example.expert.util;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;


public class DataUtil{
	public static LocalDateTime getRandomDateTimeWithinDays(int daysRange) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start = now.minusDays(daysRange);
		long startEpoch = start.toEpochSecond(java.time.ZoneOffset.UTC);
		long endEpoch = now.toEpochSecond(java.time.ZoneOffset.UTC);

		long randomEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);
		return LocalDateTime.ofEpochSecond(randomEpoch, 0, java.time.ZoneOffset.UTC);
	}
}
