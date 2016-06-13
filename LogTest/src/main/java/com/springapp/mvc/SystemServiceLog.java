package com.springapp.mvc;

/**
 * Created by yimilan on 2016/2/29.
 */
import java.lang.annotation.*;

/**
 *自定义注解 拦截service
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface SystemServiceLog {

    String description()  default "";


}