package com.example.automatsapp.model;

public class DateConvertClass {
    public static String convertDate(long date) {
        String result = "";
        long milisecondsPassed = System.currentTimeMillis() - date;
        long time = (milisecondsPassed / 1000 / 60);
        if (time >= 60*24*365)
        result = time/365/24/60 + " years ago";
       else if(time>=60*24*30)
           result = time/60/24/30+" months ago";
       else if(time>=60*24*7)
           result = time/60/24/7 + " weeks ago";
       else if (time >= 60*24)
           if (time/60/24 == 1)
               result = "1 day ago";
           else
           result = time/60/24 + " days ago";
       else if (time >= 60)
           result = time/60 + " hours ago";
       else{
           result = time + " minutes ago";
        }
        return result;
    }
}
