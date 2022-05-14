package fr.slypy.slymyjge.utils;

import java.text.NumberFormat;
import java.util.Calendar;

public class Date {

	public static String getTime() {
		
		NumberFormat f = NumberFormat.getInstance();
		f.setMinimumIntegerDigits(2);
		f.setMaximumIntegerDigits(2);
		
		return f.format(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":" + f.format(Calendar.getInstance().get(Calendar.MINUTE)) + ":" + f.format(Calendar.getInstance().get(Calendar.SECOND));
		
	}
	
}
