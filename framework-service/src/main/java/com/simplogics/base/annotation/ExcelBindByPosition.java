package com.simplogics.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcelBindByPosition {

    /**
     * The column position in the input that is used to fill the annotated
     * field.
     *
     * @return The position of the column in the XLS file from which this field
     * should be taken. This column number is zero-based.
     */
    int position();
    
}
