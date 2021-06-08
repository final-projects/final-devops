package org.ifinalframework.devops.core.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * Parameter.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class Parameter implements Serializable {
    private Class<?> clazz;
    private String value;
}
