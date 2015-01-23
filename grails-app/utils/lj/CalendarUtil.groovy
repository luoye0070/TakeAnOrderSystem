/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lj

/**
 * 日历工具类
 * @author mika
 */
class CalendarUtil {
    
    /**
     * 得到日期所在周的开始时间
     */
    public static Date getWeekStart(Date date=new Date(),boolean isResetHMS){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,calendar.getMinimum(Calendar.DAY_OF_WEEK));
        if(isResetHMS){
            calendar.set(Calendar.HOUR_OF_DAY,calendar.getMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,calendar.getMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,calendar.getMinimum(Calendar.SECOND));
        }
        return calendar.getTime();
    }
    
    /**
     * 得到日期所在周的开始时间
     */
    public static Date getWeekEnd(Date date=new Date(),boolean isResetHMS){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,calendar.getMaximum(Calendar.DAY_OF_WEEK));
        if(isResetHMS){
            calendar.set(Calendar.HOUR_OF_DAY,calendar.getMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,calendar.getMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,calendar.getMaximum(Calendar.SECOND));
        }
        return calendar.getTime();
    }
    
    
    
    
    /**
     * 得到日期所在月的开始时间
     */
    public static Date getMonthStart(Date date=new Date(),boolean isResetHMS){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMinimum(Calendar.DAY_OF_MONTH));
        if(isResetHMS){
            calendar.set(Calendar.HOUR_OF_DAY,calendar.getMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,calendar.getMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,calendar.getMinimum(Calendar.SECOND));
        }
        return calendar.getTime();
    }
    
    /**
     * 得到日期所在月的开始时间
     */
    public static Date getMonthEnd(Date date=new Date(),boolean isResetHMS){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getMaximum(Calendar.DAY_OF_MONTH));
        if(isResetHMS){
            calendar.set(Calendar.HOUR_OF_DAY,calendar.getMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,calendar.getMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,calendar.getMaximum(Calendar.SECOND));
        }
        return calendar.getTime();
    }
    
    
    /**
     * 得到日期所在月的开始时间
     */
    public static Date getThisWeekDay(Date date=new Date(),int dayOfWeek,boolean isResetHMS){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        if(isResetHMS){
            calendar.set(Calendar.HOUR_OF_DAY,calendar.getMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,calendar.getMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,calendar.getMinimum(Calendar.SECOND));
        }else{
            calendar.set(Calendar.HOUR_OF_DAY,calendar.getMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,calendar.getMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,calendar.getMaximum(Calendar.SECOND));
        }
        return calendar.getTime();
    }


    /**
     * 得到日期所在月的开始时间
     */
    public static Date getLastWeekDay(Date date=new Date(),int dayOfWeek,boolean isResetHMS){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.roll(Calendar.WEEK_OF_YEAR,-1);
        calendar.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        if(isResetHMS){
            calendar.set(Calendar.HOUR_OF_DAY,calendar.getMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,calendar.getMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,calendar.getMinimum(Calendar.SECOND));
        }else{
            calendar.set(Calendar.HOUR_OF_DAY,calendar.getMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE,calendar.getMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND,calendar.getMaximum(Calendar.SECOND));
        }
        return calendar.getTime();
    }    
    
}

