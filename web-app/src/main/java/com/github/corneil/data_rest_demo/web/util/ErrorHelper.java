package com.github.corneil.data_rest_demo.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Corneil du Plessis.
 */
public class ErrorHelper {
	private static Logger logger = LoggerFactory.getLogger(ErrorHelper.class);

	public static String stackTrace(Throwable x) {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating stacktrace for " + x, x);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream pos = new PrintStream(bos);
		x.printStackTrace(pos);
		pos.flush();
		try {
			bos.flush();
		} catch (IOException e) {
			logger.error("stackTrace:" + e, e);
		}
		return bos.toString();
	}
}
