package org.sothis.mvc.interceptors.validation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.sothis.core.beans.Autowire;
import org.sothis.core.beans.Bean;
import org.sothis.mvc.ActionInvocation;
import org.sothis.mvc.Interceptor;
import org.sothis.mvc.ModelAndViewSupport;

@Bean(autowire = Autowire.BY_NAME)
public class ValidationInterceptor implements Interceptor {

	private Validator validator;

	@Override
	public Object intercept(ActionInvocation invocation) throws Exception {
		Method method = invocation.getActionContext().getAction().getActionMethod();
		Parameter[] params = method.getParameters();
		Object[] paramValues = invocation.getActionContext().getActionParams();
		for (int i = 0; i < params.length; i++) {
			if (!(paramValues[i] instanceof ModelAndViewSupport)) {
				continue;
			}
			ModelAndViewSupport model = (ModelAndViewSupport) (paramValues[i]);
			Validated validated = params[i].getAnnotation(Validated.class);
			if (validated != null || params[i].getAnnotation(Valid.class) != null) {
				Set<ConstraintViolation<ModelAndViewSupport>> violations = validated == null
						? this.validator.validate(model) : this.validator.validate(model, validated.value());
				for (ConstraintViolation<ModelAndViewSupport> violation : violations) {
					model.addFieldError(violation.getPropertyPath().toString(), violation.getMessage());
				}
			}
		}
		return invocation.invoke();
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
