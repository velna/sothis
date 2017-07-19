package org.sothis.mvc.interceptors.validation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumberatedValidator implements ConstraintValidator<Enumberated, CharSequence> {

	private Set<String> names;

	@Override
	public void initialize(Enumberated constraintAnnotation) {
		names = new HashSet<String>();
		for (Class<? extends Enum<?>> clazz : constraintAnnotation.value()) {
			Enum<?>[] es = clazz.getEnumConstants();
			for (Enum<?> element : es) {
				names.add(element.name());
			}
		}
	}

	@Override
	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		return names.contains(value);
	}

}
