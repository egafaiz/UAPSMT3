package controller;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateController {

    public static String selectDate(JFrame parentFrame) {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        JOptionPane.showMessageDialog(parentFrame, dateChooser, "Select Date", JOptionPane.PLAIN_MESSAGE);

        Date selectedDate = dateChooser.getDate();
        if (selectedDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(selectedDate);
        } else {
            return null; 
        }
    }
}
