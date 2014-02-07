package org.sothis.core.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 根据package名称来创建自动代理。同时也可以通过额外的类型来限制自动代理的创建。
 * 
 * @author velna
 * 
 */
public class BeanPackageAutoProxyCreator extends AbstractAutoProxyCreator {

	private static final long serialVersionUID = -3344529761123559113L;

	private List<String> beanPackages;

	private Class<?> assignableClass;

	/**
	 * 设置用来自动创建代理的包名。包名必须完全匹配。
	 * 
	 * @param beanPackages
	 */
	public void setBeanPackages(String[] beanPackages) {
		Assert.notEmpty(beanPackages, "'beanNames' must not be empty");
		this.beanPackages = new ArrayList<String>(beanPackages.length);
		for (String mappedName : beanPackages) {
			this.beanPackages.add(StringUtils.trimWhitespace(mappedName));
		}
	}

	/**
	 * 设置用来限制代理创建的类型。只有{@code assignableClass}所指定的类及其子类才自动创建代理。
	 * 
	 * @param assignableClass
	 */
	public void setAssignableClass(Class<?> assignableClass) {
		this.assignableClass = assignableClass;
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
		if (null != assignableClass && !assignableClass.isAssignableFrom(beanClass)) {
			return false;
		}
		Package pkg = beanClass.getPackage();
		if (null != pkg) {
			return mappedPackage.equals(pkg.getName());
		}
		return false;
	}

}
