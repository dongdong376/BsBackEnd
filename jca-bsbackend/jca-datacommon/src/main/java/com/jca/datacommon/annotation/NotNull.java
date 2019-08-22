package com.jca.datacommon.annotation;


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
 * 对象非null校验
 * @author hml
 * @version 1.0
 * @date 2018-10-27 5:10 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@ValidateBy({NotNull.NotNullValidator.class})
public @interface NotNull {

    String message() default "";


    class NotNullValidator implements Validator<NotNull, Object> {
        @Override
        public ValidResult validation(NotNull annotation, Value<Object> value) {
            if (value.getValue() != null) {
                return ValidResult.right();
            }
            String message = "".equals(annotation.message()) ? value.getName() + "值不能为空！" : annotation.message();
            return ValidResult.error(message);
        }
    }
}
