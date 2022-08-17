package net.fortressgames.fortressapi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeFormatUtil {

	/**
	 * Get month (STRING) with number
	 *
	 * @param month which month to return (INT)
	 * @return month
	 */
	public static String getMonth(int month) {
		return switch (month) {
			case 1 -> "January";
			case 2 -> "February";
			case 3 -> "March";
			case 4 -> "April";
			case 5 -> "May";
			case 6 -> "June";
			case 7 -> "July";
			case 8 -> "August";
			case 9 -> "September";
			case 10 -> "October";
			case 11 -> "November";
			case 12 -> "December";
			default -> "Invalid month";
		};
	}

	/**
	 * Get current time
	 *
	 * @return time (dd-MM-yyyy HH:mm:ss)
	 */
	public static String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * Converts a time unit into ticks from the given information,
	 * used for tasks instead of putting a tick number.
	 *
	 * @param time the time value of the given timeUnit
	 * @param timeUnit Which unit of time
	 */
	public static int convertTimeToTicks(int time, TimeUnit timeUnit) {
		switch (timeUnit) {
			case MINUTES -> {
				return (20 * 60) * time;
			}
			case SECONDS -> {
				return 20 * time;
			}
			case MILLISECONDS -> {
				return time;
			}
		}

		return 0;
	}

	/**
	 * Convert seconds into a time string (DD/HH/MM/SS)
	 *
	 * @param seconds number of seconds
	 * @return string time
	 */
	public static String convertSeconds(int seconds) {

		int mins = 0;
		int hours = 0;
		int days = 0;

		while (seconds > 60) {
			mins++;
			seconds -= 60;
		}

		while (mins > 60) {
			hours++;
			mins -= 60;
		}

		while (hours > 24) {
			days++;
			hours -= 24;
		}

		return  days + " days, " + hours + " hours, " + mins + " mins, " + seconds + " secs";
	}
}