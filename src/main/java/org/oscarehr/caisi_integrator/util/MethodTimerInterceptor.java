package org.oscarehr.caisi_integrator.util;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.dao.EventLogDao;
import org.springframework.beans.factory.annotation.Autowired;

public class MethodTimerInterceptor implements MethodInterceptor
{
	private static final Logger logger = MiscUtils.getLogger();
	private static boolean logPerformance = Boolean.parseBoolean(ConfigXmlUtils.getPropertyString("integrator_server", "log_performance"));

	@Autowired
	private EventLogDao eventLogDao;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		long startTime = System.nanoTime();
		try
		{
			return(methodInvocation.proceed());
		}
		finally
		{
			if (logPerformance)
			{
				Method method = methodInvocation.getMethod();
				if (!method.isAnnotationPresent(AdditionalSchemaGenerationSql.class))
				{
					try
					{
						// prevent logging of this call itself
						if (!EventLogDao.class.equals(method.getDeclaringClass()))
						{
							eventLogDao.createPerformanceEventEntry(extractMethodName(method), (System.nanoTime() - startTime));
						}
					}
					catch (Exception e)
					{
						logger.error("Error logging performance entry : " + extractMethodName(method) + " : " + (System.nanoTime() - startTime), e);
					}
				}
			}
		}
	}

	private static String extractMethodName(Method method)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(method.getDeclaringClass().getSimpleName());
		sb.append('.');
		sb.append(method.getName());
		sb.append('(');

		boolean firstParam = true;
		for (Class<?> parameterType : method.getParameterTypes())
		{
			if (firstParam) firstParam = false;
			else sb.append(',');

			sb.append(parameterType.getSimpleName());
		}

		sb.append(')');

		if (sb.length() > 255)
		{
			sb.substring(0, 252);
			sb.append("...");
		}

		return(sb.toString());
	}
}
