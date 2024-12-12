package com.ncp.moeego.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ConvertDate {

    // ~분 전
    public static String calculateDate(LocalDateTime localDateTime) {

        LocalDateTime now = LocalDateTime.now();

        // 두 시간 간의 차이 계산
        long minutes = ChronoUnit.MINUTES.between(localDateTime, now);
        long hours = ChronoUnit.HOURS.between(localDateTime, now);
        long days = ChronoUnit.DAYS.between(localDateTime, now);

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (days < 7) {
            return days + "일 전";
        } else {
            // 7일 이상이면 등록 날짜 출력
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
            return localDateTime.format(formatter);
        }
    }
}
