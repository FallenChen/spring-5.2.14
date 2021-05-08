package org.garry.simpleioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName InstanceDefinition
 * @Description TODO
 * @Author cy
 * @Date 2021/4/30 14:44
 */
class InstanceDefinition extends Definition{

    private static final Logger log = LoggerFactory.getLogger(InstanceDefinition.class);

    Object instance;

    InstanceDefinition(Object instance) {
        this.instance = instance;
    }

    @Override
    void checkForDependencies(InstanceFactory factory) {
        // No dependencies
    }

    @Override
    Class getDefinitionClass() {
        return instance.getClass();
    }

    @Override
    Object getInstance() {
        return instance;
    }
}
