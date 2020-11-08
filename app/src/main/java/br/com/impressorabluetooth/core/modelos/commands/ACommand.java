package br.com.impressorabluetooth.core.modelos.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Raphael on 08/02/2015.
 */
@Target(ElementType.TYPE )
@Retention(RetentionPolicy.RUNTIME)
public @interface ACommand {
    String name();
    Class css();
}
