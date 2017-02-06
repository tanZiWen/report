package utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	/** 
     * 获取某年第一天日期 
     * @return Date 
     */  
    public static Date getCurrYearFirst(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        return calendar.getTime();
    }  
    
    /** 
     * 获取某年第一天日期 
     * @return Date 
     */  
    public static Date getCurrYearFirstISO(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        return calendar.getTime();
    } 
      
    /** 
     * 获取某年最后一天日期 
     * @return Date 
     */  
    public static Date getCurrYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1); 
        calendar.roll(Calendar.HOUR_OF_DAY, -1);
        calendar.roll(Calendar.MINUTE, -1);
        calendar.roll(Calendar.SECOND, -1);
        return calendar.getTime();  
    }
    
    /** 
     * 获取某年最后一天日期 
     * @return Date 
     */  
    public static Date getCurrYearLastISO(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1); 
        calendar.roll(Calendar.HOUR_OF_DAY, -1);
        calendar.roll(Calendar.MINUTE, -1);
        calendar.roll(Calendar.SECOND, -1);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        return calendar.getTime();  
    }
    
    /** 
     * 获取某月第一天日期 
     * @return Date 
     */  
    public static Date getCurrMonthFirst(int year, int month){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.set(Calendar.MONTH, month-1);
        return calendar.getTime();  
    }  
    
    /** 
     * 获取某月第一天日期 
     * @return Date 
     */  
    public static Date getCurrMonthFirstISO(int year, int month){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.set(Calendar.MONTH, month-1);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        return calendar.getTime();  
    }
      
    /** 
     * 获取某月最后一天日期 
     * @return Date 
     */  
    public static Date getCurrMonthLast(int year, int month){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();
        calendar.set(Calendar.YEAR, year);  
        calendar.set(Calendar.MONTH, month-1);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        calendar.roll(Calendar.HOUR_OF_DAY, -1);
        calendar.roll(Calendar.MINUTE, -1);
        calendar.roll(Calendar.SECOND, -1);
        return calendar.getTime();  
    }
    
    /** 
     * 获取某月最后一天日期 
     * @return Date 
     */  
    public static Date getCurrMonthLastISO(int year, int month){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();
        calendar.set(Calendar.YEAR, year); 
        if(month == 12) {
        	calendar.set(Calendar.MONTH, month-1);
        	calendar.roll(Calendar.DAY_OF_YEAR, -1);  
	        calendar.roll(Calendar.HOUR_OF_DAY, -1);
	        calendar.roll(Calendar.MINUTE, -1);
	        calendar.roll(Calendar.SECOND, -1);
	        calendar.add(Calendar.HOUR_OF_DAY, -8);
	        calendar.add(Calendar.MONTH, 1);
        }else {
        	calendar.set(Calendar.MONTH, month);
	    	calendar.roll(Calendar.DAY_OF_YEAR, -1);  
	        calendar.roll(Calendar.HOUR_OF_DAY, -1);
	        calendar.roll(Calendar.MINUTE, -1);
	        calendar.roll(Calendar.SECOND, -1);
	        calendar.add(Calendar.HOUR_OF_DAY, -8);
        }
        return calendar.getTime();  
    }
    
    /** 
     * 获取某天开始 
     * @return Date 
     */  
    public static Date getCurrDayFirst(int year, int month, int day){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();  
    }  
    
    /** 
     * 获取某天开始 
     * @return Date 
     */  
    public static Date getCurrDayFirstISO(int year, int month, int day){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        return calendar.getTime();  
    }
      
    /** 
     * 获取某月天结束 
     * @return Date 
     */  
    public static Date getCurrDayLast(int year, int month, int day){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();
        calendar.set(Calendar.YEAR, year);  
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day); 
        calendar.roll(Calendar.HOUR_OF_DAY, -1);
        calendar.roll(Calendar.MINUTE, -1);
        calendar.roll(Calendar.SECOND, -1);
        return calendar.getTime();  
    }
    
    /** 
     * 获取某月天结束 
     * @return Date 
     */  
    public static Date getCurrDayLastISO(int year, int month, int day){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();
        calendar.set(Calendar.YEAR, year);  
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day); 
        calendar.roll(Calendar.HOUR_OF_DAY, -1);
        calendar.roll(Calendar.MINUTE, -1);
        calendar.roll(Calendar.SECOND, -1);
        calendar.add(Calendar.HOUR_OF_DAY, -8);
        return calendar.getTime();  
    }
}
