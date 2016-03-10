package at.htl.entity.converter;

import at.htl.entity.Result;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by Laurenz on 10.12.2015.
 */
@Converter(autoApply = true)
public class ResultStringConverter implements AttributeConverter<Result,String> {
    public String convertToDatabaseColumn(Result result) {
        return result.toString();
    }

    public Result convertToEntityAttribute(String s) {
        return new Result(s);
    }
}
