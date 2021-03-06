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
import com.jca.datacommon.enums.ValueEnum;
import com.jca.datacommon.enums.tool.EnumUtils;
import com.jca.datacommon.tool.ObjectUtils;

/**
 * 枚举值校验
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 5:10 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@ValidateBy(EnumValue.EnumValueValidator.class)
public @interface EnumValue {
    /**
     * 枚举值类型，枚举必须继承{@link ValueEnum}
     */
    Class<? extends Enum<? extends ValueEnum>> value();


    class EnumValueValidator implements Validator<EnumValue, Integer> {
        @Override
        public ValidResult validation(EnumValue enumValue, Value<Integer> value) {
            if (value.getValue() == null) {
                return ValidResult.right();
            }
            Class<? extends Enum> enumClass = enumValue.value();
            if (!ValueEnum.class.isAssignableFrom(enumClass)) {
                throw new IllegalArgumentException("@EnumValue注解中的枚举类必须继承ValueEnum接口！");
            }
            //判断字段值是否存在指定的枚举类中
            if (EnumUtils.isExist(ObjectUtils.cast(enumClass.getEnumConstants(), ValueEnum.class), value.getValue())) {
                return ValidResult.right();
            }
            return ValidResult.error(value.getName() + "字段值错误！");
        }
    }
}
