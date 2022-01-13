package org.hsqldb.corrupted;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

/**
 * Exclude {@link CorruptedDataSavedExample} from the test context configuration. Include only persistence config.
 */
@ComponentScan(basePackageClasses = DbCorruptionApp.class,
    excludeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = CorruptedDataSavedExample.class))
public class DbCorruptionAppTestConfig {

}
