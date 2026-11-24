package dev.haepari.wellenough.diagnosis;

/**
 * 유산소 판정 결과. 격렬한 활동이나 중간정도 활동의 시간을 모르면 판정하지 않는다.
 */
public sealed interface AerobicResult {

	/**
	 * @param convertedMinutes 중간정도 강도로 환산한 주간 시간(분)
	 * @param shortfallMinutes 기준까지 부족한 시간(분), 충족이면 0
	 */
	record Judged(int convertedMinutes, AerobicVerdict verdict, int shortfallMinutes) implements AerobicResult {
	}

	record NotJudgeable() implements AerobicResult {
	}

}
