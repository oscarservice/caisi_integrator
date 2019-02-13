package org.oscarehr.caisi_integrator.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * You can only annotate methods that have no parameters
 * and have a return type of String[]
 * Each String in the array should be one complete sql statement.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdditionalSchemaGenerationSql
{
	// no contents required, it's just an annotation
}
