package no.officenet.test.hibernatetest.infrastructure.jpa;

import no.officenet.test.hibernatetest.CustomOption;
import no.officenet.test.hibernatetest.CustomOption;
import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

@Converter(autoApply = true)
public class OptionalStringConverter implements AttributeConverter<CustomOption<String>, String> {

	@Override
	public String convertToDatabaseColumn(CustomOption<String> attribute) {
		if (attribute == null || attribute.isNone()) {
			return null;
		} else {
			return attribute.some();
		}
	}

	@Override
	public CustomOption<String> convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return CustomOption.none();
		} else {
			return CustomOption.some(dbData);
		}
	}
}
