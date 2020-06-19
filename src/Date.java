public class Date {
    int year;
    int month;
    int day;
    int weekDay;
    String date;
    public static Date firstDay;

    public Date() {}

    public Date(String dateString, int inputWeekDay){
        String[] dateArray = dateString.split("/");
        year = Integer.parseInt(dateArray[0]);
        month = Integer.parseInt(dateArray[1]);
        day = Integer.parseInt(dateArray[2]);
        date = dateString;
        weekDay = inputWeekDay;
    }

    public Date(String dateString){
        String[] dateArray = dateString.split("/");
        year = Integer.parseInt(dateArray[0]);
        month = Integer.parseInt(dateArray[1]);
        day = Integer.parseInt(dateArray[2]);
        date = dateString;
        weekDay = setWeekDay(year, month, day);
    }

    public Date clone(){
        Date date1 = new Date();
        date1.year = year;
        date1.month = month;
        date1.day = day;
        date1.date = date;
        date1.weekDay = weekDay;
        return date1;
    }

    public void setDate (){
        date = Integer.toString(year)+"/"+Integer.toString(month)+"/"+Integer.toString(day);
    }

    public void setWeekDay (){
        int delta = (year - firstDay.year)*360 + (month-firstDay.month)*30 + day-firstDay.day;
        weekDay = delta % 7;
    }

    public static int setWeekDay (int year, int month, int day){
        int delta = (year - firstDay.year)*360 + (month-firstDay.month)*30 + day-firstDay.day;
        return delta % 7;
    }

    public void updateDate(int nextNum){
        day += nextNum;
        if (day>30){
            day -= 30;
            month++;
        }
        if (month>12){
            month -= 12;
            year++;
        }
        this.setWeekDay();
        this.setDate();
    }

    public static int deltaTime (Date date1, Date date2){
        int delta = ((date2.year-date2.year)*360)+((date2.month-date1.month)+30)+(date2.day-date1.day);
        return delta;
    }
}
