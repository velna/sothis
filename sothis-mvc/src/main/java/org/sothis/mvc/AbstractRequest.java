package org.sothis.mvc;

public abstract class AbstractRequest implements Request {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(String name, String defaultValue) {
		String ret = getString(name);
		return null == ret ? defaultValue : ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getBoolean(java.lang.String)
	 */
	@Override
	public Boolean getBoolean(String name) {
		String value = getString(name);
		return null == value ? null : Boolean.parseBoolean(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getBoolean(java.lang.String,
	 * java.lang.Boolean)
	 */
	@Override
	public Boolean getBoolean(String name, Boolean defaultValue) {
		String value = getString(name);
		return null == value ? defaultValue : Boolean.parseBoolean(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getBooleans(java.lang.String)
	 */
	@Override
	public Boolean[] getBooleans(String name) {
		String[] values = getStrings(name);
		Boolean[] booleans;
		if (values.length > 0) {
			booleans = new Boolean[values.length];
			for (int i = 0; i < values.length; i++) {
				booleans[i] = Boolean.parseBoolean(values[i]);
			}
		} else {
			booleans = new Boolean[0];
		}
		return booleans;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getInteger(java.lang.String)
	 */
	@Override
	public Integer getInteger(String name) {
		String value = getString(name);
		return null == value ? null : Integer.parseInt(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getInteger(java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	public Integer getInteger(String name, Integer defaultValue) {
		String value = getString(name);
		return null == value ? defaultValue : Integer.parseInt(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getIntegers(java.lang.String)
	 */
	@Override
	public Integer[] getIntegers(String name) {
		String[] values = getStrings(name);
		Integer[] integers;
		if (values.length > 0) {
			integers = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				integers[i] = Integer.parseInt(values[i]);
			}
		} else {
			integers = new Integer[0];
		}
		return integers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getLong(java.lang.String)
	 */
	@Override
	public Long getLong(String name) {
		String value = getString(name);
		return null == value ? null : Long.parseLong(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getLong(java.lang.String, java.lang.Long)
	 */
	@Override
	public Long getLong(String name, Long defaultValue) {
		String value = getString(name);
		return null == value ? defaultValue : Long.parseLong(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getLongs(java.lang.String)
	 */
	@Override
	public Long[] getLongs(String name) {
		String[] values = getStrings(name);
		Long[] longs;
		if (values.length > 0) {
			longs = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				longs[i] = Long.parseLong(values[i]);
			}
		} else {
			longs = new Long[0];
		}
		return longs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getDouble(java.lang.String)
	 */
	@Override
	public Double getDouble(String name) {
		String value = getString(name);
		return null == value ? null : Double.parseDouble(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getDouble(java.lang.String, java.lang.Double)
	 */
	@Override
	public Double getDouble(String name, Double defaultValue) {
		String value = getString(name);
		return null == value ? defaultValue : Double.parseDouble(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sothis.mvc.Request#getDoubles(java.lang.String)
	 */
	@Override
	public Double[] getDoubles(String name) {
		String[] values = getStrings(name);
		Double[] doubles;
		if (values.length > 0) {
			doubles = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				doubles[i] = Double.parseDouble(values[i]);
			}
		} else {
			doubles = new Double[0];
		}
		return doubles;
	}

}
