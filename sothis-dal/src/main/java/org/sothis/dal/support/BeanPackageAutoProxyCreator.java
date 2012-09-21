package org.sothis.dal.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class BeanPackageAutoProxyCreator extends AbstractAutoProxyCreator {

	private static final long serialVersionUID = -3344529761123559113L;

	private List<String> beanPackages;

	public void setBeanPackages(String[] beanPackages) {
		Assert.notEmpty(beanPackages, "'beanNames' must not be empty");
		this.beanPackages = new ArrayList<String>(beanPackages.length);
		for (String mappedName : beanPackages) {
			this.beanPackages.add(StringUtils.trimWhitespace(mappedName));
		}
	}

	@Override
	protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource) {
		if (this.beanPackages != null) {
			for (String mappedPackage : this.beanPackages) {
				if (isMatch(beanClass, mappedPackage)) {
					return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
				}
			}
		}
		return DO_NOT_PROXY;
	}

	protected boolean isMatch(Class<?> beanClass, String mappedPackage) {
		Package pkg = beanClass.getPackage();
		if (null != pkg) {
			return mappedPackage.equals(pkg.getName());
		}
		return false;
	}

}
