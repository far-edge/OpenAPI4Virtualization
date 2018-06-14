package ch.supsi.isteps.virtualfactory.tools;

public class StringUtils {

	public static String removeLastChar(String aString) {
		return removeLastChars(aString, 1);
	}

	public static String removeLastChars(String aString, int howManyChar) {
		if (aString.length() <= 0) return aString;
		return aString.substring(0, aString.length() - howManyChar);
	}

	public static int countOf(String each, String aSubstring) {
		return each.length() - each.replace(aSubstring, "").length();		
	}
}
