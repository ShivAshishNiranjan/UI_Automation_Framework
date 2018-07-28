package utils.commonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class NumberUtils {

	private final static Logger logger = LoggerFactory.getLogger(NumberUtils.class);

	private static boolean isNumberWithinRange(double number, double lowerValue, double higherValue) {
		boolean numberWithinRange = false;

		if (number == lowerValue || number == higherValue)
			numberWithinRange = true;
		else if (number > lowerValue && number < higherValue)
			numberWithinRange = true;

		return numberWithinRange;
	}

	public static boolean verifyNumericRangeValue(String actualValue, String expectedValue) {
		boolean fieldPass = false;
		try {
			if (actualValue != null && !actualValue.equalsIgnoreCase("")) {
				String rangeDelimiter = StringUtils.getNumericRangeDelimiter(expectedValue);
				String values[] = expectedValue.split(Pattern.quote(rangeDelimiter));
				switch (rangeDelimiter) {
					case "-":
						fieldPass = NumberUtils.isNumberWithinRange(Double.parseDouble(actualValue), Double.parseDouble(values[0].trim()),
								Double.parseDouble(values[1].trim()));
						break;

					case "<":
						fieldPass = NumberUtils.isNumberWithinRange(Double.parseDouble(actualValue), -(Double.MAX_VALUE), Double.parseDouble(values[1].trim()));
						break;

					case ">":
						fieldPass = NumberUtils.isNumberWithinRange(Double.parseDouble(actualValue), Double.parseDouble(values[1].trim()), Double.MAX_VALUE);
						break;
				}
			}
		} catch (Exception e) {
			logger.error("Exception while Verifying Range Value. Actual Value {} and Expected Value {}. {}", actualValue, expectedValue, e.getStackTrace());
		}
		return fieldPass;
	}
}
