package dev.haepari.wellenough.diagnosis;

import java.util.OptionalInt;

/**
 * 정리 규칙을 적용한 활동. 시간이 비어 있으면 이 활동은 판정할 수 없다.
 */
public record CleanedActivity(int days, OptionalInt minutesPerDay) {

	static CleanedActivity of(int days, int minutesPerDay) {
		return new CleanedActivity(days, OptionalInt.of(minutesPerDay));
	}

	static CleanedActivity zero() {
		return of(0, 0);
	}

	static CleanedActivity unknown(int days) {
		return new CleanedActivity(days, OptionalInt.empty());
	}

	public boolean canBeJudged() {
		return minutesPerDay.isPresent();
	}

}
