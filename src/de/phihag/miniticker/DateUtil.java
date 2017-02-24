package de.phihag.miniticker;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	private final static Map<Integer, String> WEEKDAYS = new HashMap<>();
	static {
		WEEKDAYS.put(Calendar.SUNDAY, "Sonntag");
		WEEKDAYS.put(Calendar.MONDAY, "Montag");
		WEEKDAYS.put(Calendar.TUESDAY, "Dienstag");
		WEEKDAYS.put(Calendar.WEDNESDAY, "Mittwoch");
		WEEKDAYS.put(Calendar.THURSDAY, "Donnerstag");
		WEEKDAYS.put(Calendar.FRIDAY, "Freitag");
		WEEKDAYS.put(Calendar.SATURDAY, "Samstag");
	};
	private final static String[] MONTHS = new String[]{
			"Januar", "Februar", "März", "April", "Mai", "Juni",
			"Juli", "August", "September", "Oktober", "November", "Dezember"
	};
	
	public static String longGermanString(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		return WEEKDAYS.get(dayOfWeek) + ", " + dayOfMonth + ". " + MONTHS[month] + " " + year;
	}
}
