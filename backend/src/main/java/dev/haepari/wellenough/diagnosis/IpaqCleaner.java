package dev.haepari.wellenough.diagnosis;

import dev.haepari.wellenough.diagnosis.CleaningResult.Cleaned;
import dev.haepari.wellenough.diagnosis.CleaningResult.NeedsReview;

/**
 * IPAQ 데이터 처리 지침(2005 개정) 7.2–7.4를 적용한다.
 * 순서는 7.5를 따른다: 하루 합계 확인, 짧은 활동 제외, 긴 활동 자르기.
 *
 * @see <a href="https://biobank.ctsu.ox.ac.uk/ukb/ukb/docs/ipaq_analysis.pdf">IPAQ 데이터 처리 지침</a>
 */
public class IpaqCleaner {

	private static final int MAX_TOTAL_MINUTES_PER_DAY = 960;

	private static final int MIN_MINUTES_PER_DAY = 10;

	private static final int MAX_MINUTES_PER_DAY = 180;

	public CleaningResult clean(ActivityAnswer vigorous, ActivityAnswer moderate, ActivityAnswer walking) {
		int total = reportedMinutes(vigorous) + reportedMinutes(moderate) + reportedMinutes(walking);
		if (total > MAX_TOTAL_MINUTES_PER_DAY) {
			return new NeedsReview(total);
		}
		return new Cleaned(clean(vigorous), clean(moderate), clean(walking));
	}

	private static int reportedMinutes(ActivityAnswer answer) {
		return answer.days() == 0 ? 0 : answer.minutesPerDay().orElse(0);
	}

	private static CleanedActivity clean(ActivityAnswer answer) {
		if (answer.days() == 0) {
			return CleanedActivity.zero();
		}
		if (answer.minutesPerDay().isEmpty()) {
			return CleanedActivity.unknown(answer.days());
		}
		int minutes = answer.minutesPerDay().getAsInt();
		if (minutes < MIN_MINUTES_PER_DAY) {
			return CleanedActivity.zero();
		}
		return CleanedActivity.of(answer.days(), Math.min(minutes, MAX_MINUTES_PER_DAY));
	}

}
