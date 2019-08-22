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
import com.jca.datacommon.tool.StringUtils;

/**
 * 字符串非空校验
 * @author Administrator
 *
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@ValidateBy({NotBlank.NotBlankValidator.class})
public @interface NotBlank {

    String message() default "";



    class NotBlankValidator implements Validator<NotBlank, String> {
        @Override
        public ValidResult validation(NotBlank annotation, Value<String> value) {
            if (!StringUtils.isEmpty(value.getValue())) {
                return ValidResult.right();
            }
            String message = "".equals(annotation.message()) ? value.getName() + "值不能为空！" : annotation.message();
            return ValidResult.error(message);
        }
    }
}
