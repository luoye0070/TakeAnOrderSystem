/**
 *Title:        时间处理函数库
 *Description:  时间间隔的计算，时间的加减
 *@author:      杨飞
 *@create:      2011-11-18
 *@edit:
 *              2011-11-18 by 杨飞
 *              2012-02-24 by 杨飞
 */


package lj

import java.text.DateFormat
import java.text.SimpleDateFormat

class Time {

    /**
     * 按指定日期单位计算两个日期间的间隔
     *
     * @param timeInterval(year,quarter,month,week,day,hour,minute,second)
     * @param date1
     * @param date2
     * @return
     */
    public static long DateDiff(String timeInterval, Date date1, Date date2) {
        if (timeInterval.equals("year")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int time = calendar.get(Calendar.YEAR);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.YEAR);
        }

        if (timeInterval.equals("quarter")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int time = calendar.get(Calendar.YEAR) * 4;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 4;
            calendar.setTime(date1);
            time += calendar.get(Calendar.MONTH) / 4;
            calendar.setTime(date2);
            return time - calendar.get(Calendar.MONTH) / 4;
        }

        if (timeInterval.equals("month")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int time = calendar.get(Calendar.YEAR) * 12;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 12;
            calendar.setTime(date1);
            time += calendar.get(Calendar.MONTH);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.MONTH);
        }

        if (timeInterval.equals("week")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int time = calendar.get(Calendar.YEAR) * 52;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 52;
            calendar.setTime(date1);
            time += calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.WEEK_OF_YEAR);
        }

        if (timeInterval.equals("day")) {
            long time = date1.getTime() / 1000 / 60 / 60 / 24;
            return time - date2.getTime() / 1000 / 60 / 60 / 24;
        }

        if (timeInterval.equals("hour")) {
            long time = date1.getTime() / 1000 / 60 / 60;
            return time - date2.getTime() / 1000 / 60 / 60;
        }

        if (timeInterval.equals("minute")) {
            long time = date1.getTime() / 1000 / 60;
            return time - date2.getTime() / 1000 / 60;
        }

        if (timeInterval.equals("second")) {
            long time = date1.getTime() / 1000;
            return time - date2.getTime() / 1000;
        }

        return date1.getTime() - date2.getTime();
    }


    /**
     * 为日期计算指定间隔
     *
     * @param timeInterval(year,quarter,month,week,day,hour,minute,second)
     * @param date（原始时间）
     * @param n（间隔的数字）
     * @return
     */

    public static Date DateAdd(String timeInterval, Date date, Integer n) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.setTime(date);
        switch (timeInterval) {
            case "year":    calendar.add(Calendar.YEAR, n);     break;
            case "month":   calendar.add(Calendar.MONTH, n);    break;
            case "week":    calendar.add(Calendar.WEAK, n);     break;
            case "day":     calendar.add(Calendar.DATE, n);     break;
            case "hour":    calendar.add(Calendar.HOUR, n);     break;
            case "minute":  calendar.add(Calendar.MINUTE, n);   break;
            case "second":  calendar.add(Calendar.SECOND, n);   break;
        }
        String newdate = df.format(calendar.getTime());
        return df.parse(newdate);
    }

    /**
     * 获取当前月的第一天
     * @return Date
     */
    public static Date FirstDayofMonth(Date t = new Date()) {
        t.setDate(1);
        t.setHours(0);
        t.setMinutes(0);
        t.setSeconds(0);
        return t
    }

    /**
     * 获取当前月的最后一天
     * @return Date
     */
    public static Date LastDayofMonth(Date t = new Date()) {
        Date FirstDayofNextMonth = DateAdd("month", this.FirstDayofMonth(t), 1)
        return DateAdd("day", FirstDayofNextMonth, -1)
    }


    public static Map DateDiffirent(Date startDate,Date endDate){
        long start=startDate.getTime();
        long end=endDate.getTime();
        println new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate)
        println new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate)
        Map result = new HashMap();
        int day = (end-start)/(1000*60*60*24);
        result.day=day;
        int hour = (end-start)/(1000*60*60)-day*24;
        result.hour=hour;
        int minute = (end-start)/(1000*60)-day*24*60-hour*60;
        result.minute=minute;
        int second = (end-start)/(1000)-day*24*60*60-hour*60*60-minute*60;
        result.second=second;
        
        return result;
    }
    
    public static Date addDate(Date date ,int timeInterval,int number){
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        cal.add(timeInterval,number);
        return cal.getTime();
    }
}
