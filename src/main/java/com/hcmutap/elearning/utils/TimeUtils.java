package com.hcmutap.elearning.utils;

import com.hcmutap.elearning.model.ClassModel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
	public static boolean isSameTime(ClassModel now, ClassModel next) {
		// Compare semester
		if (!now.getSemesterId().equalsIgnoreCase(next.getSemesterId())) {
			return false;
		}

		// Compare day of week
		if (!now.getDayOfWeek().equalsIgnoreCase(next.getDayOfWeek())) {
			return false;
		}

		// Compare time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
		LocalTime time_start_now = LocalTime.parse(now.getTimeStart(), formatter);
		LocalTime time_end_now = LocalTime.parse(now.getTimeEnd(), formatter);
		LocalTime time_start_next = LocalTime.parse(next.getTimeStart(), formatter);
		LocalTime time_end_next = LocalTime.parse(next.getTimeEnd(), formatter);

		// Check if the time overlaps
		if ((time_start_now.isBefore(time_end_next) && time_end_now.isAfter(time_start_next)) ||
				(time_start_next.isBefore(time_end_now) && time_end_next.isAfter(time_start_now))) {
			return true;
		}

		return false;
	}
}
