package utils;

/**
 * Holds equality functions
 */
public class ModelEqualityUtils {

	/**
	 * Check if two times have equal "strength"
	 *
	 * @param firstStartTime
	 * @param secondStartTime
	 * @return
	 */
	public static boolean isTimeCorrelated( double firstStartTime, double secondStartTime ) {
		int originStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( firstStartTime );
		int substitutorStartTimeDecimalPlacesNumber = Utils.getDecimalPlaces( secondStartTime );
		if ( originStartTimeDecimalPlacesNumber == substitutorStartTimeDecimalPlacesNumber ) {
			return true;
		}
		return false;
	}

}
