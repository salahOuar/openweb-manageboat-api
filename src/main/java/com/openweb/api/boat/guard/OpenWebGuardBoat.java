package com.openweb.api.boat.guard;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
//only the owner of the resource can update or delete it
public @interface OpenWebGuardBoat {

}
