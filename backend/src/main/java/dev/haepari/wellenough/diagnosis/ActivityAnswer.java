package dev.haepari.wellenough.diagnosis;

import java.util.OptionalInt;

/**
 * 활동 하나에 대한 설문 응답. 시간이 비어 있으면 "모르겠다"를 뜻한다.
 */
public record ActivityAnswer(int days, OptionalInt minutesPerDay) {

	private static final int MAX_DAYS = 7;

	private static final int MAX_MINUTES_PER_DAY = 24 * 60;

	public ActivityAnswer {
		if (days < 0 || days > MAX_DAYS) {
			throw new IllegalArgumentException("days must be between 0 and 7: " + days);
		}
		minutesPerDay.ifPresent(minutes -> {
			if (minutes < 0 || minutes > MAX_MINUTES_PER_DAY) {
				throw new IllegalArgumentException("minutesPerDay must be between 0 and 1440: " + minutes);
			}
		});
	}

	public static ActivityAnswer of(int days, int minutesPerDay) {
		return new ActivityAnswer(days, OptionalInt.of(minutesPerDay));
	}

	public static ActivityAnswer none() {
		return new ActivityAnswer(0, OptionalInt.empty());
	}

	public static ActivityAnswer unknownTime(int days) {
		return new ActivityAnswer(days, OptionalInt.empty());
	}

}
