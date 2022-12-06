package com.telederma.gov.co.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Daniel Hernández on 17/09/2018.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface TeledermaAudit {

    AuditActionType action();

    enum AuditActionType {
        LOGIN
    }

}
