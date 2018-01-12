package org.sothis.mvc;

public interface Parameterized {

	String getQueryString();

	Parameters parameters() throws RequestParseExecption;
}
