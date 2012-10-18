package org.sothis.core.util.cron;

import java.util.Calendar;
import java.util.Date;

/**
 * cron表达式
 * 
 * @author velna
 * 
 */
public class Cron {

	private final Field[] fields = new Field[7];
	private final String expression;

	private Cron(String expression) {
		if (null == expression) {
			throw new IllegalArgumentException("expression can not be null");
		}
		this.expression = expression;
		try {
			parse();
		} catch (NumberFormatException e) {
			throw new CronSyntaxException("error parse cron expression: " + expression, e);
		} catch (IllegalArgumentException e) {
			throw new CronSyntaxException("error parse cron expression: " + expression, e);
		}
	}

	/**
	 * 将一个cron表达式编译为Cron对象
	 * 
	 * @param cronExpression
	 * @throws CronSyntaxException
	 *             如果编译出错
	 * @throws IllegalArgumentException
	 *             如果cronExpression为null
	 * @return
	 */
	public static Cron compile(String cronExpression) {
		return new Cron(cronExpression);
	}

	private void parse() {
		String[] fieldSplit = expression.toUpperCase().split("\\s+");
		if (fieldSplit.length > 7 || fieldSplit.length < 6) {
			throw new CronSyntaxException("invalid cron expression: " + expression);
		}
		for (int i = 0; i < fieldSplit.length; i++) {
			String fieldExpr = fieldSplit[i];
			switch (i) {
			case 0:
				fields[i] = new SecondField(fieldExpr);
				break;
			case 1:
				fields[i] = new MinuteField(fieldExpr);
				break;
			case 2:
				fields[i] = new HourField(fieldExpr);
				break;
			case 3:
				fields[i] = new DayOfMonthField(fieldExpr);
				break;
			case 4:
				fields[i] = new MonthField(fieldExpr);
				break;
			case 5:
				fields[i] = new DayOfWeekField(fieldExpr);
				break;
			case 6:
				fields[i] = new YearField(fieldExpr);
				break;
			}
		}
	}

	/**
	 * 判断当前的cron表达式是否匹配一个时间
	 * 
	 * @param date
	 * @return true为匹配，false为不匹配
	 */
	public boolean matches(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		for (int i = 0; i < 7; i++) {
			Field field = fields[i];
			if (null != field && !field.isBlank() && !field.matches(calendar)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 原始的表达式
	 * 
	 * @return
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * 得到编译后的各个字段
	 * 
	 * @return
	 */
	public Field[] getFields() {
		return fields;
	}

}
