package pers.lurker.webconfig.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pers.lurker.webconfig.exception.ext.DateException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateConverter implements Converter<String, Date> {

    private final Logger log = LoggerFactory.getLogger(DateConverter.class);

    @Override
    public Date convert(String source) {
        if (!StringUtils.hasText(source)) {
            return null;
        }
        String sourceTrimed = StringUtils.trimWhitespace(source);

        try {
            SimpleDateFormat sdf = null;
            if (sourceTrimed.matches("^\\d{4}-\\d{1,2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM");
            } else if (sourceTrimed.matches("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            } else if (sourceTrimed.matches("^\\d{4}/\\d{1,2}$")) {
                sdf = new SimpleDateFormat("yyyy/MM");
            } else if (sourceTrimed.matches("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{1,2}$")) {
                sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            } else if (sourceTrimed.matches("^\\d{4}$")) {
                sdf = new SimpleDateFormat("yyyy");
            } else if (sourceTrimed.matches("^\\d{4}\\d{1,2}$")) {
                sdf = new SimpleDateFormat("yyyyMM");
            } else if (sourceTrimed.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            } else if (sourceTrimed.matches("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            if (sdf == null) {
                throw new DateException("日期[" + sourceTrimed + "]格式错误");
            }
            return sdf.parse(sourceTrimed);
        } catch (ParseException e) {
            log.error("source:" + sourceTrimed + "\n" + e.getMessage(), e);
        }
        return null;
    }

}
