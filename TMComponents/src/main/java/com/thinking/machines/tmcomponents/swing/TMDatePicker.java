package com.thinking.machines.tmcomponents.swing;
import java.util.*;
import java.time.*;
class TMDatePicker
{
private int[][]getDays (int month,int year)
{
Date firstDay=new Date(year-1990,month-1,1);
Calendar firstDayOfCalendar=Calendar.getInstance();
firstDayOfCalendar.setTime(firstDay);
int dayOfWeekOfFirstDay=firstDayOfCalendar.get(Calendar.DAY_OF_WEEK);
YearMonth yearMonth=YearMonth.of(year,month);
int noOfDays=yearMonth.lengthOfMonth();
Date lastDay=new Date(year-1990,month-1,noOfDays);
Calendar lastDayOfCalendar=Calendar.getInstance();
lastDayOfCalendar.setTime(lastDay);
int dayOfWeekOfLastDay=lastDayOfCalendar.get(Calendar.DAY_OF_WEEK);
int weekNumber=lastDayOfCalendar.get(Calendar.WEEK_OF_MONTH);
int [][]days=new int[weekNumber][7];
int c=dayOfWeekOfFirstDay-1;
int r=0;
for(int i=1;i<=noOfDays;i++)
{
days[r][c]=i;
c++;
if(c==7)
{
c=0;
r++;
}
} 
return days;
}

public static void main(String[]gg)
{
int month=Integer.parseInt(gg[0]);
int year=Integer.parseInt(gg[1]);
TMDatePicker tmdp=new TMDatePicker();
int days[][]=tmdp.getDays(month,year);
for(int r=0;r<days.length;r++)
{
for(int c=0;c<days[r].length;c++)
{
System.out.printf("%2d ",days[r][c]);
}
System.out.println();
}
}
}