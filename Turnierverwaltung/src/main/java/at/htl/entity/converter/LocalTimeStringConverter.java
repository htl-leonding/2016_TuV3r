package at.htl.entity.converter;

import at.htl.entity.Result;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Laurenz on 10.12.2015.
 */
@Converter(autoApply = true)
public class LocalTimeStringConverter implements AttributeConverter<LocalTime,String> {
    @Override
    public String convertToDatabaseColumn(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public LocalTime convertToEntityAttribute(String s) {
        return LocalTime.parse(s,DateTimeFormatter.ofPattern("HH:mm"));
    }
}
