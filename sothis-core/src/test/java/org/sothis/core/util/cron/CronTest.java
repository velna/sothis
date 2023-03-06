package org.sothis.core.util.cron;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CronTest {

	private final static SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Test(dataProvider = "matches")
	public void testMatches(String cronExpression, String dateString, boolean match, boolean error)
			throws ParseException {
		CronSyntaxException exception = null;
		try {
			Cron cron = Cron.compile(cronExpression);
			Assert.assertEquals(cron.matches(DF.parse(dateString)), match, "cronExpression: " + cronExpression);
		} catch (CronSyntaxException e) {
			exception = e;
		}
		if (exception != null && !error) {
			Assert.fail("find exception", exception);
		}
	}

	@DataProvider(name = "matches")
	public Object[][] matches() {
		List<Object[]> paramList = new ArrayList<Object[]>();

		String now = DF.format(new Date());
		paramList.add(new Object[] { "* * * * * *", now, true, false });
		paramList.add(new Object[] { "* * * * * ?", now, true, false });
		paramList.add(new Object[] { "* * * ? * *", now, true, false });
		paramList.add(new Object[] { "* * * ? * ?", now, true, false });
		paramList.add(new Object[] { "* * * * * * *", now, true, false });
		paramList.add(new Object[] { "* * * ? * * *", now, true, false });
		paramList.add(new Object[] { "* * * * * ? *", now, true, false });
		paramList.add(new Object[] { "* * * ? * ? *", now, true, false });

		// single value match
		paramList.add(new Object[] { "0 0 0 1 0 1 2012", "2012-01-01 00:00:00", true, false });

		// second not match
		paramList.add(new Object[] { "1 0 0 1 0 1 2012", "2012-01-01 00:00:00", false, false });
		paramList.add(new Object[] { "59 0 0 1 0 1 2012", "2012-01-01 00:00:00", false, false });

		// minute not match
		paramList.add(new Object[] { "0 1 0 1 0 1 2012", "2012-01-01 00:00:00", false, false });
		paramList.add(new Object[] { "0 59 0 1 0 1 2012", "2012-01-01 00:00:00", false, false });

		// hour not match
		paramList.add(new Object[] { "0 0 1 1 0 1 2012", "2012-01-01 00:00:00", false, false });
		paramList.add(new Object[] { "0 0 23 1 0 1 2012", "2012-01-01 00:00:00", false, false });

		// day of month not match
		paramList.add(new Object[] { "0 0 0 2 0 1 2012", "2012-01-01 00:00:00", false, false });
		paramList.add(new Object[] { "0 0 0 31 0 1 2012", "2012-01-01 00:00:00", false, false });

		// month not match
		paramList.add(new Object[] { "0 0 0 1 1 1 2012", "2012-01-01 00:00:00", false, false });
		paramList.add(new Object[] { "0 0 0 1 11 1 2012", "2012-01-01 00:00:00", false, false });

		// day of week not match
		paramList.add(new Object[] { "0 0 0 1 0 2 2012", "2012-01-01 00:00:00", false, false });
		paramList.add(new Object[] { "0 0 0 1 0 7 2012", "2012-01-01 00:00:00", false, false });

		// year not match
		paramList.add(new Object[] { "0 0 0 1 0 1 2013", "2012-01-01 00:00:00", false, false });
		paramList.add(new Object[] { "0 0 0 1 0 1 2011", "2012-01-01 00:00:00", false, false });

		// others
		paramList.add(new Object[] { "0 0 0 * * *", "2012-03-20 12:00:00", false, false });
		paramList.add(new Object[] { "0 0 0 * * *", "2012-03-20 00:00:00", true, false });
		paramList.add(new Object[] { "0 0 0 * * *", "2012-03-20 24:00:00", true, false });
		paramList.add(new Object[] { "12 34 9 20 2 ? 2012", "2012-03-20 09:34:12", true, false });
		paramList.add(new Object[] { "7/5 30/4 3/3 17/3 2 ? 2012", "2012-03-20 09:34:12", true, false });
		paramList.add(new Object[] { "7/5 30/4 */3 LW 2 ? 2012", "2012-03-20 09:34:12", false, false });
		paramList.add(new Object[] { "* 30-40/2 22-10 LW * ?", "2012-10-31 09:34:12", true, false });
		paramList.add(new Object[] { "7/5,12 * 9-10 WL * ?", "2012-10-31 09:34:12", true, false });
		paramList.add(new Object[] { "10,11,12,13 * 8-9 6W * ?", "2012-10-05 09:34:12", true, false });
		paramList.add(new Object[] { "* * 9 6W * ?", "2012-10-06 09:34:12", false, false });
		paramList.add(new Object[] { "* * 9 6W * ?", "2012-10-04 09:34:12", false, false });
		paramList.add(new Object[] { "* * 9 6W * ?", "2012-10-08 09:34:12", false, false });
		paramList.add(new Object[] { "1-10,12,13 * 8-10 ? * 2#2", "2012-10-08 09:34:12", true, false });
		paramList.add(new Object[] { "* * 8-10 ? * 2#2", "2012-10-01 09:34:12", false, false });
		paramList.add(new Object[] { "* * 8-10 ? * 2#2", "2012-10-15 09:34:12", false, false });

		// alias
		paramList.add(new Object[] { "7/5 30/4 3/3 ? MAR TUE 2012", "2012-03-20 09:34:12", true, false });
		paramList.add(new Object[] { "7/5 30/4 3/3 ? JAN-APR MON-TUE 2012", "2012-03-20 09:34:12", true, false });
		paramList.add(new Object[] { "7/5 30/4 3/3 ? JAN-APR MON,TUE 2012", "2012-03-20 09:34:12", true, false });

		// exceptions
		paramList.add(new Object[] { "** * * * * *", now, false, true });
		paramList.add(new Object[] { "* * * * * ss", now, false, true });
		paramList.add(new Object[] { "* * * * * #", now, false, true });
		paramList.add(new Object[] { "? * * * * *", now, false, true });
		paramList.add(new Object[] { "* ? * * * *", now, false, true });
		paramList.add(new Object[] { "* * ? * * *", now, false, true });
		paramList.add(new Object[] { "* * * * ? *", now, false, true });
		paramList.add(new Object[] { "* * * * * * ?", now, false, true });
		paramList.add(new Object[] { "* 1- * * * *", now, false, true });
//		paramList.add(new Object[] { "* -10 * * * *", now, false, true });

		return paramList.toArray(new Object[paramList.size()][3]);
	}

	@Test(dataProvider = "nexts")
	public void testNext(String cronExpression, String dateString, String nextDateString) throws ParseException {
		Cron cron = Cron.compile(cronExpression);
		Date nextDate = cron.next(DF.parse(dateString));
		Assert.assertEquals(DF.format(nextDate), nextDateString);
	}

	@DataProvider(name = "nexts")
	public Object[][] nexts() {
		List<Object[]> paramList = new ArrayList<Object[]>();

		paramList.add(new Object[] { "* * * * * *", "2012-03-20 09:34:12", "2012-03-20 09:34:13" });
		paramList.add(new Object[] { "* 35 * * * *", "2012-03-20 09:34:12", "2012-03-20 09:35:00" });
		paramList.add(new Object[] { "12 35 * * * *", "2012-03-20 09:34:12", "2012-03-20 09:35:12" });
		paramList.add(new Object[] { "* * */4 * * *", "2012-03-20 09:34:12", "2012-03-20 12:00:00" });
		paramList.add(new Object[] { "0 0 */3 * * *", "2012-03-20 09:34:12", "2012-03-20 12:00:00" });

		return paramList.toArray(new Object[paramList.size()][3]);
	}
}
