package com.jca.datacommon.annotation;



import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.jca.datacommon.annotation.validator.ValidResult;
import com.jca.datacommon.annotation.validator.Validator;
import com.jca.datacommon.annotation.validator.Value;

/**
 * 字符串正则表达式校验
 * @author Administrator
 *
 */
@Target({ FIELD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
@ValidateBy(Pattern.PatternValidator.class)
public @interface Pattern {

    String message() default "";

    String regexp();


    class PatternValidator implements Validator<Pattern, String> {
        @Override
        public ValidResult validation(Pattern pattern, Value<String> value) {
            if (value.getValue() == null) {
                return ValidResult.right();
            }
            if (value.getValue().matches(pattern.regexp())) {
                return ValidResult.right();
            }
            String message = !"".equals(pattern.message()) ? pattern.message() : value.getName() + "参数不符合指定正则！";
            return ValidResult.error(message);
        }
    }
}
