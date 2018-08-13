package com.shivashish.utils.commonutils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static final String[] formats = {
			"dd-MM-yyyy",
			"dd-MM-yyyy a z",
			"dd-MM-yyyy hh:mm",
			"dd-MM-yyyy HH:mm:ss",
			"dd-MM-yyyy HH:mm:ss a z",
			"dd-MMM-yyyy",
			"dd-MMM-yyyy a z",
			"dd-MM-yyyy HH:mm:ss",
			"dd-MM-yyyy HH:mm:ss a z",
			"MM-dd-yyyy",
			"MM-dd-yyyy a z",
			"MM-dd-yyyy HH:mm:ss",
			"MM-dd-yyyy HH:mm:ss a z",
			"MMM-dd-yyyy",
			"MMM-dd-yyyy a z",
			"MMM-dd-yyyy HH:mm:ss",
			"MMM-dd-yyyy HH:mm:ss a z",
			"yyyy:MM:dd HH:mm:ss",
			"MMMMM-dd-yyyy"
	};

	public static String getCurrentDateInDDMMYYYY() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getCurrentDateInMM_DD_YYYY() {
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getCurrentDateIndd_MM_YYYY_HH_mm_ss() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getCurrentDateInDDMMMYYYY() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static String getDateFromEpoch(long epoch, String currentFormat) {
		DateFormat dateFormat = new SimpleDateFormat(currentFormat);
		Date date = new Date(epoch);
		return dateFormat.format(date);
	}



	public static String convertDateToDDMMYYYY(String origDate, String currentFormat) throws ParseException {
		if (!currentFormat.equalsIgnoreCase("dd-MM-yyyy") && !currentFormat.equalsIgnoreCase("dd/MM/yyyy")
				&& !currentFormat.equalsIgnoreCase("dd:MM:yyyy")) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentFormat);
			Date date = simpleDateFormat.parse(origDate);
			SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			return outputDateFormat.format(date);
		}
		return origDate;
	}

	public static String convertDateToMMDDYYYY(String origDate, String currentFormat) throws ParseException {
		if (!currentFormat.equalsIgnoreCase("MM-dd-yyyy") && !currentFormat.equalsIgnoreCase("MM/dd/yyyy")
				&& !currentFormat.equalsIgnoreCase("MM:dd:yyyy")) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentFormat);
			Date date = simpleDateFormat.parse(origDate);
			SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM-dd-yyyy");
			return outputDateFormat.format(date);
		}
		return origDate;
	}

	public static String convertDateToAnyFormat(String origDate, String requiredFormat) throws ParseException {
		return convertDateToAnyFormat(origDate, null, requiredFormat);
	}

	public static String convertDateToAnyFormat(String origDate, String currentFormat, String requiredFormat) throws ParseException {
		if (currentFormat == null)
			currentFormat = DateUtils.getDateFormat(origDate);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentFormat);
		Date date = simpleDateFormat.parse(origDate);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat(requiredFormat);
		return outputDateFormat.format(date);
	}

	public static String getDateFormat(String date) {
		String format = null;

		for (String parse : formats) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(parse);
			try {
				dateFormat.parse(date);
				format = parse;
			} catch (ParseException e) {
			}
			if (format != null)
				break;
		}
		return format;
	}

	public static String getPreviousDateInDDMMYYYY(String currentDate) {
		String Tokens[] = currentDate.split("-");
		int date = Integer.parseInt(Tokens[0]);
		int month = Integer.parseInt(Tokens[1]);
		int year = Integer.parseInt(Tokens[2]);

		date--;

		if (date < 1) {
			month -= 1;

			if (month < 1) {
				month += 12;
				year--;
			}

			String tokens[] = DateUtils.getMonthEndDateInMMDDFormat(month - 1, year).split("/");
			date = Integer.parseInt(tokens[1]);
		}

		String previousDate = "";

		if (date < 10)
			previousDate += "0";

		previousDate += date + "-";

		if (month < 10)
			previousDate += "0";

		previousDate += month + "-" + year;

		return previousDate;
	}

	public static String getNextDateInDDMMYYYY(String currentDate) {
		String Tokens[] = currentDate.split("-");
		int date = Integer.parseInt(Tokens[0]);
		int month = Integer.parseInt(Tokens[1]);
		int year = Integer.parseInt(Tokens[2]);

		date++;

		String endDateTokens[] = DateUtils.getMonthEndDateInMMDDFormat(month - 1, year).split("/");

		if (date > Integer.parseInt(endDateTokens[1])) {
			month = Integer.parseInt(Tokens[1]) + 1;

			if (month > 12) {
				month -= 12;
				year++;
			}

			String tokens[] = DateUtils.getMonthStartDateInMMDDFormat(month - 1).split("/");
			date = Integer.parseInt(tokens[1]);
		}

		String nextDate = "";

		if (date < 10)
			nextDate += "0";

		nextDate += date + "-";

		if (month < 10)
			nextDate += "0";

		nextDate += month + "-" + year;

		return nextDate;
	}


	public static String getDateOfXDaysFromYDate(String yDate, int xDays, String currentFormat) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentFormat);
		Date date = simpleDateFormat.parse(yDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, xDays);
		String xDate = simpleDateFormat.format(cal.getTime());

		return xDate;
	}


	public static String getMonthStartDateInMMDDFormat(int month) {
		String monthStartDate[] = {
				"01/01",
				"02/01",
				"03/01",
				"04/01",
				"05/01",
				"06/01",
				"07/01",
				"08/01",
				"09/01",
				"10/01",
				"11/01",
				"12/01"
		};

		return monthStartDate[month - 1];
	}

	public static String getMonthEndDateInMMDDFormat(int month, int year) {
		String monthEndDate[] = {
				"01/31",
				"",
				"03/31",
				"04/30",
				"05/31",
				"06/30",
				"07/31",
				"08/31",
				"09/30",
				"10/31",
				"11/31",
				"12/31"
		};

		if (month != 1)
			return monthEndDate[month - 1];

		if (year % 4 == 0)
			return "02/29";

		return "02/28";
	}

	public static String getCurrentDateInAnyFormat(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}
}