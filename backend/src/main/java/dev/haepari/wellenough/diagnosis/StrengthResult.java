package dev.haepari.wellenough.diagnosis;

/**
 * 근력운동 판정 결과. 유산소와 별도로 판정한다.
 */
public record StrengthResult(int days, boolean met) {
}
