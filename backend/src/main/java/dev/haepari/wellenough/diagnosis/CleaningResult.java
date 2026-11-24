package dev.haepari.wellenough.diagnosis;

/**
 * 정리 결과. 정리가 끝났거나, 사용자에게 입력값 확인을 요청해야 한다.
 */
public sealed interface CleaningResult {

	record Cleaned(CleanedActivity vigorous, CleanedActivity moderate, CleanedActivity walking)
			implements CleaningResult {
	}

	record NeedsReview(int totalMinutesPerDay) implements CleaningResult {
	}

}
