package dp.tag.helpers;

import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class CaptchaAPI {
	private static final String URL = "http://172.18.26.19/api/";
	private static final String URL2 = "http://172.18.26.18/api/";
	private static final String API_KEY = "f9RFHmUoRQe592VJSbyJYTEPChLE3V9dUeYq23OCXPw9hP2JJeZAus0ahLrhhXp9";
	private static final String Authorization = "Basic bWNpX2xpdmU6TlZMRjI1R2N6ajNKMFppZ3N0WHd1bjZXSjRhU1R0SFE=";

	/**
	 * get a new catcha image
	 * 
	 * 
	 * @return Captcha
	 */
	public Captcha getNew() {
		final String URI = "generate";
		try {
			HttpResponse<JsonNode> response = Unirest.post(URL + URI).header("App-Key", API_KEY).header("Authorization", Authorization).asJson();

			JSONObject object = response.getBody().getObject();
			if (response.getStatus() != 200)
				object = retry(URI);

			Captcha captcha = new Captcha();
			captcha.setId(object.getJSONObject("data").getString("captchaId"));
			captcha.setImage(object.getJSONObject("data").getString("captchaImage"));

			return captcha;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get new captcha with same id
	 * 
	 * @return Captcha
	 */
	public Captcha reCaptcha(String id) {
		final String URI = "refresh";
		try {
			HttpResponse<JsonNode> response = Unirest.post(URL + URI).header("App-Key", API_KEY).header("Authorization", Authorization)
					.field("captchaId", id).asJson();

			JSONObject object = response.getBody().getObject();
			if (response.getStatus() != 200)
				object = retry(URI, id);

			// System.out.println(object);

			Captcha captcha = new Captcha();
			captcha.setId(object.getJSONObject("data").getString("captchaId"));
			captcha.setImage(object.getJSONObject("data").getString("captchaImage"));

			return captcha;
		} catch (UnirestException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * check that user entered correct value for specific id (note : if it
	 * returns null check was sucesfull)
	 * 
	 * 
	 * @param enteredValue
	 * @param captchaId
	 * 
	 * @return Captcha
	 */
	public Captcha check(String enteredValue, String captchaId) {
		final String URI = "check";
		JSONObject object = null;
		try {
			
			HttpResponse<JsonNode> response = Unirest.post(URL + URI).header("App-Key", API_KEY).header("Authorization", Authorization)
					.field("captchaId", captchaId).field("enteredValue", enteredValue).asJson();

			object = response.getBody().getObject();

			if (response.getStatus() != 200)
				object = retry(URI, enteredValue, captchaId);

			return analyseCheckResponse(object, captchaId);

		} catch (UnirestException e) {
			e.printStackTrace();
			// this prevent null pointer exception
			Captcha captcha = new Captcha();
			captcha.setErrorCode(500);
			captcha.setErrorMessage("Captcha didn't check");
			return captcha;
		}
	}

	/**
	 * analyse returned json of check API and generate a true Captcha instance
	 * 
	 * 
	 * @param object
	 * @param captchaId
	 * @return Captcha
	 */
	private Captcha analyseCheckResponse(JSONObject object, String captchaId) {
		Captcha captcha = new Captcha();

		if (object.getString("status").equals("error")) {
			captcha.setErrorCode(object.getInt("code"));
			captcha.setErrorMessage(object.getString("message"));

			/*
			 * -9 happen when we didn't send entered value and API will not send
			 * back a new captcha
			 */
			if (captcha.getErrorCode() != -9) {
				captcha.setId(object.getJSONObject("data").getString("captchaId"));
				captcha.setImage(object.getJSONObject("data").getString("captchaImage"));
			}
			return captcha;
		}

		// this change captcha when check was successful for security reasons
		return reCaptcha(captchaId);
	}

	/**
	 * try to get data from second server with same data
	 * 
	 * 
	 * @param URI
	 * @return JSONObject
	 * @throws UnirestException
	 */
	public JSONObject retry(String URI) throws UnirestException {

		HttpResponse<JsonNode> response = Unirest.post(URL2 + URI).header("App-Key", API_KEY).header("Authorization", Authorization).asJson();
		JSONObject object = response.getBody().getObject();

		if (response.getStatus() != 200)
			throw new UnirestException("server does not response");

		return object;
	}

	/**
	 * try to get data from second server with same data
	 * 
	 * 
	 * @param URI
	 * @param id
	 * @return JSONObject
	 * @throws UnirestException
	 */
	public JSONObject retry(String URI, String enteredValue, String id) throws UnirestException {

		HttpResponse<JsonNode> response = Unirest.post(URL2 + URI).header("App-Key", API_KEY).header("Authorization", Authorization)
				.field("captchaId", id).field("enteredValue", enteredValue).asJson();

		JSONObject object = response.getBody().getObject();

		if (response.getStatus() != 200)
			throw new UnirestException("server does not response");

		return object;
	}

	/**
	 * try to get data from second server with same data
	 * 
	 * 
	 * @param URI
	 * @param id
	 * @return JSONObject
	 * @throws UnirestException
	 */
	public JSONObject retry(String URI, String id) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.post(URL2 + URI).header("App-Key", API_KEY).header("Authorization", Authorization)
				.field("captchaId", id).asJson();

		JSONObject object = response.getBody().getObject();

		if (response.getStatus() != 200)
			throw new UnirestException("server does not response");

		return object;
	}
}
