/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.ShiftScheduleUtils;
import com.advantech.webservice.atmc.HttpClientUtil;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import static com.advantech.helper.ShiftScheduleUtils.*;
import com.advantech.webservice.mes.UploadType;
import com.google.common.base.CharMatcher;
import static oracle.security.pki.resources.OraclePKICmd.p;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    private static final Logger log = LoggerFactory.getLogger(TestClass.class);

    List<StopWatch> temp_L = new ArrayList();

    @Test
    public void test() {
        DateTime currentTime = new DateTime();
        currentTime = currentTime.withMillisOfDay(0);
        currentTime = currentTime.withTimeAtStartOfDay();

        DateTime dt = new DateTime("2025-5-13").withDayOfWeek(DateTimeConstants.SUNDAY);
        dt = new DateTime("2025-5-13").withDayOfWeek(DateTimeConstants.MONDAY);

        int period;
        if (currentTime.getHourOfDay() == 10 && currentTime.getMinuteOfHour() < 30) {
            period = new Period(new DateTime().withTime(13, 54, 23, 0), new DateTime().withTime(12, 00, 0, 0)).toStandardMinutes().getMinutes();
            log.info("bab_id {} / Max: {} / Sum: {} / BANANCE: {} / STANDARD: {}", 1, 0.1,
                    0.2, 0.3, 0.4);
        }
        log.info("bab_id {} / Max: {} / Sum: {} / BANANCE: {} / STANDARD: {}", 1, 0.1,
                0.2, 0.3, 0.4);
    }

//    @Test
    public void testKeywordFilter() throws InterruptedException {
        List<String> keywords = newArrayList("TPC", "T1PC1", "ABCC", "T1PC1331", "DBB");
        String modelName = "TPC1331-2213-ZZ";

        String key = keywords.stream()
                .filter(modelName::contains)
                .max(Comparator.comparing(String::length)).orElse(null);
        System.out.println(key);
    }

    @Test
    public void testEnum() {
        UploadType ut = UploadType.UPDATE;
        System.out.println(ut.toString());
    }

//    @Test
    public void testString() {
        String str = "TPAB810807";
        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(str);
        String st = null;
        while (m.find()) {
            st = m.group();
        }
        str = str.replace(st, Integer.toString((NumberUtils.createInteger(st)) - 1));
        System.out.println(str);
    }

//    @Test
    public void testEnum2() {
        System.out.println(1 >> 1);
    }

//    @Test
    public void testInterval() {
        Interval rest1 = new Interval(new DateTime().withTime(12, 0, 0, 0), new DateTime().withTime(12, 50, 0, 0));
        Interval testRange = new Interval(new DateTime().withTime(9, 40, 2, 983), new DateTime().withTime(17, 8, 38, 470));

        System.out.println(testRange.overlaps(rest1));
        Interval overlap = testRange.overlap(rest1);
        System.out.println(
                Minutes.minutesBetween(testRange.getStart(), testRange.getEnd()).getMinutes()
                - Minutes.minutesBetween(overlap.getStart(), overlap.getEnd()).getMinutes()
        );
    }

//    @Test
    public void testRegex() {
        String[] strs = {
            "測試",
            "包裝",
            "前置",
            "間接人員",
            "間接人員",
            "包裝",
            "測試",
            "組裝",
            "包裝",
            "測試"
        };

        for (String str : strs) {
            if (str.matches("(前置|組裝|測試|包裝)")) {
                System.out.println(str);
            }
        }
    }

    DateTimeFormatter df = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
    List<Interval> restTimes = newArrayList(new Interval(new DateTime().withTime(15, 30, 0, 0), new DateTime().withTime(15, 45, 0, 0)));

//    @Test
    public void testDateTime() {
        DateTime dS = new DateTime().withTime(15, 20, 0, 0);
        DateTime dE = new DateTime().withTime(15, 25, 0, 0);

        DateTime dS1 = new DateTime().withTime(15, 20, 0, 0);
        DateTime dE1 = new DateTime().withTime(15, 35, 0, 0);

        DateTime dS2 = new DateTime().withTime(15, 20, 0, 0);
        DateTime dE2 = new DateTime().withTime(15, 50, 0, 0);

        DateTime dS3 = new DateTime().withTime(15, 40, 0, 0);
        DateTime dE3 = new DateTime().withTime(15, 42, 0, 0);

        DateTime dS4 = new DateTime().withTime(15, 40, 0, 0);
        DateTime dE4 = new DateTime().withTime(15, 55, 0, 0);

        Interval i = byPassRestTime(new Interval(dS, dE));
        Interval i1 = byPassRestTime(new Interval(dS1, dE1));
        Interval i2 = byPassRestTime(new Interval(dS2, dE2));
        Interval i3 = byPassRestTime(new Interval(dS3, dE3));
        Interval i4 = byPassRestTime(new Interval(dS4, dE4));

        System.out.printf("Interval 1: %s --- %s\r\n", df.print(i.getStart()), df.print(i.getEnd()));
        System.out.printf("Interval 2: %s --- %s\r\n", df.print(i1.getStart()), df.print(i1.getEnd()));
        System.out.printf("Interval 3: %s --- %s\r\n", df.print(i2.getStart()), df.print(i2.getEnd()));
        System.out.printf("Interval 4: %s --- %s\r\n", df.print(i3.getStart()), df.print(i3.getEnd()));
        System.out.printf("Interval 5: %s --- %s\r\n", df.print(i4.getStart()), df.print(i4.getEnd()));

    }

    private Interval byPassRestTime(Interval i) {
        for (Interval restTime : restTimes) {
            int iMin = Minutes.minutesBetween(i.getStart(), i.getEnd()).getMinutes();
            int restMin = Minutes.minutesBetween(restTime.getStart(), restTime.getEnd()).getMinutes();
            if (hasOverlap(i, restTime)) {
                if (isInRestTime(restTime, i.getStart()) && isInRestTime(restTime, i.getEnd())) {
                    /*
                        i   |----|
                        r |--------|   
                     */
                    return new Interval(restTime.getEnd(), restTime.getEnd().plusMinutes(iMin));
                } else if (isInRestTime(restTime, i.getStart()) && !isInRestTime(restTime, i.getEnd())) {
                    /*
                        i    |--------|
                        r |----|   
                     */
                    int overlap = Minutes.minutesBetween(i.getStart(), restTime.getEnd()).getMinutes();
                    return new Interval(restTime.getEnd(), i.getEnd().plusMinutes(overlap));
                } else if (!isInRestTime(restTime, i.getStart()) && isInRestTime(restTime, i.getEnd())) {
                    /*
                        i |----|
                        r   |--------|   
                     */
                    int overlap = Minutes.minutesBetween(restTime.getStart(), i.getEnd()).getMinutes();
                    return new Interval(i.getStart(), restTime.getEnd().plusMinutes(overlap));
                } else {
                    /*
                        i |--------|
                        r   |----|   
                     */
                    return new Interval(i.getStart(), i.getEnd().plusMinutes(restMin));
                }
            }
        }
        return i;
    }

    private boolean hasOverlap(Interval t1, Interval t2) {
        return !t1.getEnd().isBefore(t2.getStart()) && !t1.getStart().isAfter(t2.getEnd());
    }

    private boolean isInRestTime(Interval rest, DateTime d) {
        return rest.getStart().compareTo(d) * d.compareTo(rest.getEnd()) >= 0;
    }

    @Test
    public void testMap() throws Exception {
        String url = "http://172.22.250.120:7878/v1/Employee/" + "A-7568";
        String url2 = "http://172.22.250.120:7878/v1/Employee/login";
        Map m = new HashMap();
        m.put("empNo", "A-7568");
        m.put("password", "ww75687568");
        String charset = "UTF-8";

        String result = HttpClientUtil.doGet(url, m, charset);
        String result2 = HttpClientUtil.doPost(url2, m, charset);

//        String result = AtmcEmployeeUtils.getUser("A-75s68");
//        Boolean result2 = AtmcEmployeeUtils.userLogin("A-7568", "www757687568");
//
        HibernateObjectPrinter.print(result, result2);
    }

//    @Test
    public void testSt() {
        int s = 0, k = 0, j = 0;

        for (k = 1; k <= 6; k += 2) {
            for (j = 3; j <= 8; j += 3) {
                s += j;
            }
        }
        System.out.println(s + k + j);
    }

//    @Test
    public void testDateTime2() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy/M/d");
        DateTime testDate = new DateTime("2020-10-23").withTime(16, 45, 0, 0);

        int executeDayCnt = 4;
        List<DateTime> executeDays = new ArrayList();

        DateTime d = new DateTime(testDate);
        if (d.getHourOfDay() >= 17) {
            d = d.plusDays(d.getDayOfWeek() == 6 ? 2 : 1);
        }

        for (int i = 0; i < executeDayCnt; i++) {
            if (d.getDayOfWeek() == 7) {
                d = d.plusDays(1);
            }
            executeDays.add(d);
            d = d.plusDays(1);
        }

        executeDays.forEach(dt -> {
            System.out.println(fmt.print(dt));
        });

    }

//    @Test
    public void testDateTime3() {
        DateTime d = new DateTime();
        System.out.println(d.getWeekOfWeekyear());
    }

//    @Test
    public void testShiftScheduleUtils() {
        DatetimeGenerator ge = new DatetimeGenerator("yyyy-MM-dd HH:mm");

        List<DateTime> testDates = new ArrayList();
        DateTime tD = new DateTime().withTime(7, 55, 0, 0);
        DateTime tD2 = new DateTime().withTime(19, 55, 0, 0);
        testDates.add(tD);
        testDates.add(tD2);

        testDates.forEach(d -> {
            System.out.println("Testing date " + ge.dateFormatToString(d));

            Shift shift = getShift(d);

            if (null == shift) {
                System.out.println("Date in unsupported shift...");
            } else {
                switch (shift) {
                    case MORNING_SHIFT:
                        DateTime m_sD = getMorningShiftStart(d);
                        DateTime m_eD = getMorningShiftEnd(d);
                        System.out.printf("MORNING_SHIFT %s to %s \r\n ", ge.dateFormatToString(m_sD), ge.dateFormatToString(m_eD));
                        break;
                    case NIGHT_SHIFT:
                        DateTime n_sD = getNightShiftStart(d);
                        DateTime n_eD = getNightShiftEnd(d);
                        System.out.printf("NIGHT_SHIFT %s to %s \r\n ", ge.dateFormatToString(n_sD), ge.dateFormatToString(n_eD));
                        break;
                    default:
                        System.out.println("Date in unsupported shift...");
                        break;
                }
            }

            System.out.println("------------");
        });
    }

    @Test
    public void testShiftScheduleUtils2() {
        DatetimeGenerator ge = new DatetimeGenerator("yyyy-MM-dd HH:mm");
        DateTime now = DateTime.now().withTime(8, 30, 0, 0);
        Shift shift = ShiftScheduleUtils.getShift(now);
        DateTime sd = now, ed = (shift == Shift.MORNING_SHIFT ? new DateTime(sd).plusDays(1) : new DateTime(sd));
        ed = ed.withTime(8, 0, 0, 0);
        System.out.printf("%s %s to %s \r\n ", shift.toString(), ge.dateFormatToString(sd), ge.dateFormatToString(ed));
    }

    @Test
    public void testPattern() {
        String testString = "45F";
        String testString2 = "5F";
        assertEquals("45", testString.replaceAll("^(\\d+).*$", "$1"));
        assertEquals("5", testString2.replaceAll("^(\\d+).*$", "$1"));
    }

}
