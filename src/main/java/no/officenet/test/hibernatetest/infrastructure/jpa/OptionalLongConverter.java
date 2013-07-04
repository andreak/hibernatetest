package no.officenet.test.hibernatetest.infrastructure.jpa;

import no.officenet.test.hibernatetest.CustomOption;
import no.officenet.test.hibernatetest.CustomOption;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class OptionalLongConverter implements AttributeConverter<CustomOption<Long>, Long> {

	@Override
	public Long convertToDatabaseColumn(CustomOption<Long> attribute) {
		if (attribute == null || attribute.isNone()) {
			return null;
		} else {
			return attribute.some();
		}
	}

	@Override
	public CustomOption<Long> convertToEntityAttribute(Long dbData) {
		if (dbData == null) {
			return CustomOption.none();
		} else {
			return CustomOption.some(dbData);
		}
	}
}
