package com.jca.datacommon.annotation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

import com.jca.datacommon.annotation.validator.ValidResult;
import com.jca.datacommon.annotation.validator.Validator;
import com.jca.datacommon.annotation.validator.Value;

/**
 * 如果校验字段类型为{@link String}和{@link Collection}类型则比较其长度和size<br/>
 * 如果为{@link Integer}则比较范围
 * @author Administrator
 *
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@ValidateBy(Size.SizeValidator.class)
public @interface Size {

    String message() default "";

    int min() default 0;

    int max() default Integer.MAX_VALUE;


    
    /**
     * size内部验证器
     * @author Administrator
     *
     */
    class SizeValidator implements Validator<Size, Object> {
        @Override
        public ValidResult validation(Size size, Value<Object> value) {
            Object fieldValue = value.getValue();
            if (fieldValue == null) {
                return ValidResult.right();
            }

            int min = size.min();
            int max = size.max();

            if (fieldValue instanceof String) {
                String v = (String)fieldValue;
                int len = v.length();
                if (len >= min && len <= max) {
                    return ValidResult.right();
                }
                return errorResult(value.getName(), size);
            }

            if (fieldValue instanceof Number) {
                Number v = (Number) fieldValue;
                int val = v.intValue();
                if (val >= min && val <= max) {
                    return ValidResult.right();
                }
                return errorResult(value.getName(), size);
            }

            if (fieldValue instanceof Collection) {
                Collection v = (Collection)fieldValue;
                int len = v.size();
                if (len >= min && len <= max) {
                    return ValidResult.right();
                }
                return errorResult(value.getName(), size);
            }

            return ValidResult.right();
        }

        private ValidResult errorResult(String fieldName, Size size) {
            String message = "".equals(size.message()) ? fieldName + "范围错误！" : size.message();
            return ValidResult.error(message);
        }
    }
}
