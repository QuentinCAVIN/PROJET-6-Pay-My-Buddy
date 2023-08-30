package com.paymybuddy.paymybuddysapp.service;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
@Component
public class DateProvider {

    public String todaysDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String todaysDate = formatter.format(date);
        return todaysDate;
    }
}
