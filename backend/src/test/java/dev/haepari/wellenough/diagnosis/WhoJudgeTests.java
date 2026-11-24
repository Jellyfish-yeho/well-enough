package dev.haepari.wellenough.diagnosis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import dev.haepari.wellenough.diagnosis.AerobicResult.Judged;
import dev.haepari.wellenough.diagnosis.AerobicResult.NotJudgeable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class WhoJudgeTests {

	private final WhoJudge judge = new WhoJudge();

	// 격렬한 활동 75분(환산 150분)에 중간정도 활동을 더해 경계를 만든다.
	@ParameterizedTest(name = "환산 {2}분 -> {3}")
	@CsvSource({ "0, 0, 0, UNDER, 150", "0, 149, 149, UNDER, 1", "0, 150, 150, MET, 0", "75, 149, 299, MET, 0",
			"75, 150, 300, MET, 0", "75, 151, 301, PLUS, 0" })
	void judgesAtTheBoundaries(int vigorousMinutes, int moderateMinutes, int converted, AerobicVerdict verdict,
			int shortfall) {
		AerobicResult result = judge.judgeAerobic(CleanedActivity.of(1, vigorousMinutes),
				CleanedActivity.of(1, moderateMinutes));

		assertThat(result).isEqualTo(new Judged(converted, verdict, shortfall));
	}

	@Test
	void multipliesDaysAndCountsVigorousTwice() {
		AerobicResult result = judge.judgeAerobic(CleanedActivity.of(1, 30), CleanedActivity.of(2, 25));

		assertThat(result).isEqualTo(new Judged(110, AerobicVerdict.UNDER, 40));
	}

	@Test
	void handlesTheLargestCleanedAnswers() {
		AerobicResult result = judge.judgeAerobic(CleanedActivity.of(7, 180), CleanedActivity.of(7, 180));

		assertThat(result).isEqualTo(new Judged(3780, AerobicVerdict.PLUS, 0));
	}

	@Test
	void doesNotJudgeWhenVigorousIsUnknown() {
		AerobicResult result = judge.judgeAerobic(CleanedActivity.unknown(2), CleanedActivity.of(7, 180));

		assertThat(result).isEqualTo(new NotJudgeable());
	}

	@Test
	void doesNotJudgeWhenModerateIsUnknown() {
		AerobicResult result = judge.judgeAerobic(CleanedActivity.of(7, 180), CleanedActivity.unknown(2));

		assertThat(result).isEqualTo(new NotJudgeable());
	}

	@ParameterizedTest(name = "근력 {0}일 -> {1}")
	@CsvSource({ "0, false", "1, false", "2, true", "7, true" })
	void judgesStrengthSeparately(int days, boolean met) {
		assertThat(judge.judgeStrength(days)).isEqualTo(new StrengthResult(days, met));
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 8 })
	void rejectsImpossibleStrengthDays(int days) {
		assertThatIllegalArgumentException().isThrownBy(() -> judge.judgeStrength(days));
	}

}
