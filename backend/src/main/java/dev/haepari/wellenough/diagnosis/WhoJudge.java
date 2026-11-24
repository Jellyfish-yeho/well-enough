package dev.haepari.wellenough.diagnosis;

import dev.haepari.wellenough.diagnosis.AerobicResult.Judged;
import dev.haepari.wellenough.diagnosis.AerobicResult.NotJudgeable;

/**
 * WHO 2020 신체활동 가이드라인의 성인(18–64세) 권장량으로 판정한다.
 * 걷기와 앉아 있던 시간은 판정에 쓰지 않는다.
 *
 * @see <a href="https://www.ncbi.nlm.nih.gov/books/NBK566048/">WHO 2020 가이드라인</a>
 */
public class WhoJudge {

	private static final int MIN_MINUTES_PER_WEEK = 150;

	private static final int MAX_RECOMMENDED_MINUTES_PER_WEEK = 300;

	private static final int VIGOROUS_WEIGHT = 2;

	private static final int MIN_STRENGTH_DAYS = 2;

	private static final int DAYS_PER_WEEK = 7;

	public AerobicResult judgeAerobic(CleanedActivity vigorous, CleanedActivity moderate) {
		if (!vigorous.canBeJudged() || !moderate.canBeJudged()) {
			return new NotJudgeable();
		}
		int converted = weeklyMinutes(moderate) + weeklyMinutes(vigorous) * VIGOROUS_WEIGHT;
		int shortfall = Math.max(0, MIN_MINUTES_PER_WEEK - converted);
		return new Judged(converted, verdict(converted), shortfall);
	}

	public StrengthResult judgeStrength(int days) {
		if (days < 0 || days > DAYS_PER_WEEK) {
			throw new IllegalArgumentException("days must be between 0 and 7: " + days);
		}
		return new StrengthResult(days, days >= MIN_STRENGTH_DAYS);
	}

	private static int weeklyMinutes(CleanedActivity activity) {
		return activity.days() * activity.minutesPerDay().getAsInt();
	}

	private static AerobicVerdict verdict(int convertedMinutes) {
		if (convertedMinutes < MIN_MINUTES_PER_WEEK) {
			return AerobicVerdict.UNDER;
		}
		if (convertedMinutes <= MAX_RECOMMENDED_MINUTES_PER_WEEK) {
			return AerobicVerdict.MET;
		}
		return AerobicVerdict.PLUS;
	}

}
