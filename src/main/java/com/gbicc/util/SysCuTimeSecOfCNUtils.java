package com.gbicc.util;

import java.util.Calendar;
import java.util.TimeZone;

public class SysCuTimeSecOfCNUtils {

	public static long getSysCuTimeSecOfCN() {

		Calendar instance = Calendar.getInstance(TimeZone
				.getTimeZone("Asia/Shanghai"));
		long timeInMillis = instance.getTimeInMillis() / 1000;
		return timeInMillis;

	}

}
