package com.openweb.api.boat.guard;

import com.openweb.api.boat.exception.UnauthorizedActionException;
import com.openweb.api.security.domain.User;
import com.openweb.api.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
//only the owner of the resource can update or delete it
public class OpenWebGuardBoatAspect {


    /**
     *
     */
    private final UserService userService;

    public OpenWebGuardBoatAspect(UserService userService) {
        this.userService = userService;
    }

    @Around("@annotation(OpenWebGuardBoat)")
    public Object guardCommunity(ProceedingJoinPoint joinPoint) throws Throwable {
        //only the owner of the resource can update or delete it
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null && joinPoint.getArgs()[0] instanceof Long) {
            Long boatId = (Long) joinPoint.getArgs()[0];
            User user = userService.getUser();
            user.getBoats().stream().filter(boat -> boatId.equals(boat.getId())).findFirst().orElseThrow(() -> new UnauthorizedActionException() {
            });
        }
        return joinPoint.proceed();
    }
}