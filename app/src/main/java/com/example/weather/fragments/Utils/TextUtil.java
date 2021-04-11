package com.example.weather.fragments.Utils;

import android.util.Log;

import com.example.weather.FromJson.Current;
import com.example.weather.FromJson.Daily;
import com.example.weather.FromJson.Hourly;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

public class TextUtil {
    private static final String TODAY = "сегодня";
    private static final String FEELS = "Ощущается как ";

    public static String[] textForItem(Hourly hour, long timezoneShift) {
        String[] answer = new String[9]; // 0 - время, 1 - описание погоды, 2 - иконка, 3 - температура, 4 - ощущается как, 5 - ветер, 6 - влажность, 7 - осадки,мм, 8 - вероятность осадков

        StringBuilder forDate = new StringBuilder();

        LocalDate dateNow = LocalDate.now();

        Instant instant = Instant.ofEpochSecond(Long.parseLong(hour.getDt()) + timezoneShift);
        LocalTime timeFromJS = instant.atOffset(ZoneOffset.UTC).toLocalTime();
        LocalDate dateFromJS = instant.atOffset(ZoneOffset.UTC).toLocalDate();

        if (dateFromJS.getDayOfYear() == dateNow.getDayOfYear()) {
            forDate.append(TODAY).append(", ");
        } else {
            forDate.append(dateFromJS.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))).append(", ");
            forDate.append(dateFromJS.getDayOfMonth()).append(" ");
            forDate.append(dateFromJS.getMonth().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))).append(", ");
        }
        forDate.append(timeFromJS.getHour()).append(":").append("00");

        answer[0] = forDate.toString();
        String description = hour.getWeather()[0].getDescription();
        answer[1] = description.substring(0,1).toUpperCase().concat(description.substring(1));
        answer[2] = hour.getWeather()[0].getIcon();
        answer[3] = String.valueOf(Math.round(Double.parseDouble(hour.getTemp()))).concat("℃");
        answer[4] = String.valueOf(Math.round(Double.parseDouble(hour.getFeels_like()))).concat("℃");
        answer[5] =  String.valueOf(Math.round(Double.parseDouble(hour.getWind_speed()))).concat(" м/с");
        answer[6] = hour.getHumidity().concat("%");

        if (hour.getRain() != null || hour.getSnow() != null) {
            double sum = 0.0;
            if (hour.getRain() != null) {
                sum += Double.parseDouble(hour.getRain().getOneH());
            }
            if (hour.getSnow() != null) {
                sum += Double.parseDouble(hour.getSnow().getOneH());
            }
            answer[7] = String.valueOf(Math.round(sum)).concat(" мм");
        } else {
            answer[7] = "0 мм";
        }

        answer[8] = "";
        if(!Objects.equals("0", hour.getPop())) {
            if (Objects.equals("1", hour.getPop())) {
                answer[8] = "100%";
            } else {
                answer[8] = hour.getPop().replaceAll("[0][.]", "").concat("%").replaceAll("^0", "");
            }
        }
        return answer;
    }

    public static String[] forTodayFragment(Current current, Daily today, String timezoneOffset) {
        String[] answer = new String[8]; // 0 - дата, 1 - темп. днём, 2 - темп. ночью, 3 - темп. сейчас, 4 - ощущается как, 5 - иконка, 6 - описание погоды, 7 - вероятность осадков

        answer[0] = todayTimestampCreator(timezoneOffset, current.getDt());
        answer[1] = String.valueOf(Math.round(Double.parseDouble(today.getTemp().getDay()))).concat("°");
        answer[2] = String.valueOf(Math.round(Double.parseDouble(today.getTemp().getNight()))).concat("°");
        answer[3] = String.valueOf(Math.round(Double.parseDouble(current.getTemp())));
        answer[4] = FEELS.concat(String.valueOf(Math.round(Double.parseDouble(current.getFeels_like()))).concat("°"));
        answer[5] = current.getWeather()[0].getIcon();
        String description = current.getWeather()[0].getDescription();
        answer[6] = description.substring(0,1).toUpperCase().concat(description.substring(1));


        answer[7] =  String.valueOf(Double.parseDouble(today.getPop())*100).replaceAll("^0[.]", "").replaceAll("[.][0-9]+", "").concat("%");
        return answer;
    }

    public static String[] forTomorrowFragment(Daily tomorrow, String timezoneOffset) {
        String[] answer = new String[6];  // 0 - дата, 1 - темп. днём, 2 - темп. ночью, 3 - иконка, 4 - описание погоды, 5 - вероятность осадков

       answer[0] = tomorrowTimestampCreator(tomorrow.getDt(), timezoneOffset);
       answer[1] = String.valueOf(Math.round(Double.parseDouble(tomorrow.getTemp().getDay()))).concat("°");
       answer[2] = String.valueOf(Math.round(Double.parseDouble(tomorrow.getTemp().getNight()))).concat("°");
       answer[3] = tomorrow.getWeather()[0].getIcon();
       String description = tomorrow.getWeather()[0].getDescription();
       answer[4] = description.substring(0,1).toUpperCase().concat(description.substring(1));

       answer[5] =  String.valueOf(Double.parseDouble(tomorrow.getPop())*100).replaceAll("^0[.]", "").replaceAll("[.][0-9]+", "").concat("%");

       return answer;
    }

    public static String tomorrowTimestampCreator (String dt, String timezoneOffset) {
        StringBuilder forDate = new StringBuilder();
        Instant instant = Instant.ofEpochSecond(Long.parseLong(dt) + Long.parseLong(timezoneOffset));
        LocalDate dateFromJS = instant.atOffset(ZoneOffset.UTC).toLocalDate();
        forDate.append(dateFromJS.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))).append(", ");
        forDate.append(dateFromJS.getDayOfMonth()).append(" ");
        forDate.append(dateFromJS.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")));
        Log.d("timestamp", forDate.toString());
        return forDate.toString();
    }
    public static String todayTimestampCreator(String timezoneOffset, String dt) {

        Instant instant = Instant.ofEpochSecond(Long.parseLong(dt) + Long.parseLong(timezoneOffset));
        LocalTime timeFromJS = instant.atOffset(ZoneOffset.UTC).toLocalTime();
        LocalDate dateFromJS = instant.atOffset(ZoneOffset.UTC).toLocalDate();

        int day = dateFromJS.getDayOfMonth();
        String month = dateFromJS.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"));
        LocalTime lt = LocalTime.now();
        int hour = timeFromJS.getHour();
        int minute = timeFromJS.getMinute();
        return String.format("%d %s, %d:%02d", day, month, hour, minute);
    }
    public static int currentHour(String dt, String offset) {
        Instant instant = Instant.ofEpochSecond(Long.parseLong(dt) + Long.parseLong(offset));
        LocalTime timeFromJS = instant.atOffset(ZoneOffset.UTC).toLocalTime();
        return  timeFromJS.getHour();
    }

    public static String[] todayTomorrowDiagram(Hourly hourly, String timezoneOffset) {
        String[] answer = new String[2]; /// 0 - час, 1 - градус
        Instant instant = Instant.ofEpochSecond(Long.parseLong(hourly.getDt()) + Long.parseLong(timezoneOffset));
        LocalTime timeFromJS = instant.atOffset(ZoneOffset.UTC).toLocalTime();

        String hour =  String.valueOf(timeFromJS.getHour());
        if (hour.length() == 1) {
            hour = "0".concat(hour);
        }
        answer[0] = hour.concat(":00");
        answer[1] = String.valueOf(Math.round(Double.parseDouble(hourly.getTemp()))).concat("°");

        return  answer;
    }
}
