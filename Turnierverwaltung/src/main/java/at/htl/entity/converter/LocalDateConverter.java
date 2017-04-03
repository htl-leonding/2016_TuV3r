package at.htl.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Lokal on 12.01.2016.
 */

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate,Date> {
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return Date.valueOf(localDate);
    }

    public LocalDate convertToEntityAttribute(Date date) {
        return date.toLocalDate();
    }
}

