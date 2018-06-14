package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

/**
 * @author gabriele
 *
 */
public class SenMLFaredgeMessages {

	public static String whr_BOL(String productionLine, String timestamp) {
			String result = "[" +
					"{\"bn\": \"BOL\", \"bt\": "+ timestamp +"}," +
					"{\"n\": \"industrialCode\", \"u\": \"vs\", \"v\": \"944934370000\"}," +
					"{\"n\": \"serialNumber\", \"u\": \"vs\", \"v\": \"901702003358\"}," +
					"{\"n\": \"productionLine\", \"u\": \"vs\", \"v\": \""+ productionLine +"\"}" +
				"]";
			return result;
	}

	public static String whr_EOL_GR(String productionLine, String timestamp) {
		String result = "[" +
				"{\"bn\": \"EOLGR\", \"bt\": "+ timestamp +"}," +
				"{\"n\": \"serialNumber\", \"u\": \"vs\", \"v\": \"901702003358\"}," +
				"{\"n\": \"batchNumber\", \"u\": \"vs\", \"v\": \"1234\"}," +
				"{\"n\": \"sku\", \"u\": \"vs\", \"v\": \"AAA\"}," +
				"{\"n\": \"productionLine\", \"u\": \"vs\", \"v\": \""+ productionLine +"\"}" +
			"]";
		return result;
	}

	public static String whr_EOL_ST(String productionLine, String timestamp) {
		String result = "[" +
				"{\"bn\": \"EOLST\", \"bt\": "+ timestamp +"}," +
				"{\"n\": \"serialNumber\", \"u\": \"vs\", \"v\": \"901702003358\"}," +
				"{\"n\": \"batchNumber\", \"u\": \"vs\", \"v\": \"1234\"}," +
				"{\"n\": \"sku\", \"u\": \"vs\", \"v\": \"AAA\"}," +
				"{\"n\": \"productionLine\", \"u\": \"vs\", \"v\": \""+ productionLine +"\"}" +
			"]";
		return result;
	}
}
