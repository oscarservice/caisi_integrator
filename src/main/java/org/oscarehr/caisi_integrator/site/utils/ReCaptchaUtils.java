package org.oscarehr.caisi_integrator.site.utils;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.util.ConfigXmlUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;

public class ReCaptchaUtils
{
	private static final Logger logger=MiscUtils.getLogger();
	public static final String PUBLIC_KEY = StringUtils.trimToNull(ConfigXmlUtils.getPropertyString("recaptcha", "public_key"));
	public static final String PRIVATE_KEY = StringUtils.trimToNull(ConfigXmlUtils.getPropertyString("recaptcha", "private_key"));

	public static boolean enabled()
	{
		return(PUBLIC_KEY!=null && PRIVATE_KEY!=null);
	}
	
	public static boolean validate(HttpServletRequest request)
	{
		String reCaptchaUserResponse = request.getParameter("recaptcha_response_field");
		String reCaptchaChallenge = request.getParameter("recaptcha_challenge_field");
		String remoteAddr = request.getRemoteAddr();

		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey(PRIVATE_KEY);
		ReCaptchaResponse reCaptchaServerResponse = reCaptcha.checkAnswer(remoteAddr, reCaptchaChallenge, reCaptchaUserResponse);
		if (reCaptchaServerResponse.isValid())
		{
			return(true);
		}
		else
		{
			logger.warn("Failed recaptcha validation. userResponse="+reCaptchaUserResponse+", serverError="+reCaptchaServerResponse.getErrorMessage());
			return(false);
		}
	}
}
