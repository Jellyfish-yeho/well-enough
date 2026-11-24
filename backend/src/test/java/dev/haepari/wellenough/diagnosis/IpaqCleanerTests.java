package dev.haepari.wellenough.diagnosis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import dev.haepari.wellenough.diagnosis.CleaningResult.Cleaned;
import dev.haepari.wellenough.diagnosis.CleaningResult.NeedsReview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IpaqCleanerTests {

	private final IpaqCleaner cleaner = new IpaqCleaner();

	// 9분 이하는 일수와 시간을 모두 0으로, 181분 이상은 180분으로 바꾼다.
	@ParameterizedTest(name = "3일 {0}분 -> {1}일 {2}분")
	@CsvSource({ "0, 0, 0", "9, 0, 0", "10, 3, 10", "11, 3, 11", "179, 3, 179", "180, 3, 180", "181, 3, 180" })
	void appliesMinimumAndMaximumToEveryActivity(int minutes, int expectedDays, int expectedMinutes) {
		CleanedActivity expected = CleanedActivity.of(expectedDays, expectedMinutes);
		ActivityAnswer answer = ActivityAnswer.of(3, minutes);

		assertThat(cleaner.clean(answer, answer, answer)).isEqualTo(new Cleaned(expected, expected, expected));
	}

	@Test
	void keepsEachActivityInItsOwnPlace() {
		CleaningResult result = cleaner.clean(ActivityAnswer.of(1, 30), ActivityAnswer.of(2, 25),
				ActivityAnswer.of(5, 40));

		assertThat(result).isEqualTo(new Cleaned(CleanedActivity.of(1, 30), CleanedActivity.of(2, 25),
				CleanedActivity.of(5, 40)));
	}

	@Test
	void acceptsTotalOf960Minutes() {
		CleaningResult result = cleaner.clean(ActivityAnswer.of(1, 320), ActivityAnswer.of(1, 320),
				ActivityAnswer.of(1, 320));

		CleanedActivity truncated = CleanedActivity.of(1, 180);
		assertThat(result).isEqualTo(new Cleaned(truncated, truncated, truncated));
	}

	@Test
	void asksForReviewWhenTotalIs961Minutes() {
		CleaningResult result = cleaner.clean(ActivityAnswer.of(1, 320), ActivityAnswer.of(1, 320),
				ActivityAnswer.of(1, 321));

		assertThat(result).isEqualTo(new NeedsReview(961));
	}

	// 원문 순서: 합계 확인이 10분 미만 처리보다 먼저다.
	@Test
	void checksTotalBeforeDroppingShortActivities() {
		CleaningResult result = cleaner.clean(ActivityAnswer.of(1, 952), ActivityAnswer.of(1, 9),
				ActivityAnswer.none());

		assertThat(result).isEqualTo(new NeedsReview(961));
	}

	@Test
	void ignoresTimeOfActivityDoneOnZeroDays() {
		CleaningResult result = cleaner.clean(ActivityAnswer.of(0, 900), ActivityAnswer.of(1, 100),
				ActivityAnswer.none());

		assertThat(result).isEqualTo(new Cleaned(CleanedActivity.zero(), CleanedActivity.of(1, 100),
				CleanedActivity.zero()));
	}

	@Test
	void marksOnlyTheUnknownActivityAsNotJudgeable() {
		CleaningResult result = cleaner.clean(ActivityAnswer.unknownTime(2), ActivityAnswer.of(3, 30),
				ActivityAnswer.none());

		assertThat(result).isEqualTo(new Cleaned(CleanedActivity.unknown(2), CleanedActivity.of(3, 30),
				CleanedActivity.zero()));
		Cleaned cleaned = (Cleaned) result;
		assertThat(cleaned.vigorous().canBeJudged()).isFalse();
		assertThat(cleaned.moderate().canBeJudged()).isTrue();
		assertThat(cleaned.walking().canBeJudged()).isTrue();
	}

	@Test
	void checksTotalOfKnownTimesEvenWhenOneIsUnknown() {
		CleaningResult result = cleaner.clean(ActivityAnswer.of(1, 500), ActivityAnswer.of(1, 461),
				ActivityAnswer.unknownTime(1));

		assertThat(result).isEqualTo(new NeedsReview(961));
	}

	@ParameterizedTest
	@CsvSource({ "-1, 30", "8, 30", "3, -1", "3, 1441" })
	void rejectsImpossibleAnswers(int days, int minutes) {
		assertThatIllegalArgumentException().isThrownBy(() -> ActivityAnswer.of(days, minutes));
	}

	@ParameterizedTest
	@CsvSource({ "0, 0", "7, 1440" })
	void acceptsAnswersAtTheLimits(int days, int minutes) {
		assertThat(ActivityAnswer.of(days, minutes).days()).isEqualTo(days);
	}

}
