package org.garry.simpleioc;

/**
 * @ClassName Definition
 * @Description TODO
 * @Author cy
 * @Date 2021/4/30 14:21
 */
abstract class Definition {

    abstract void checkForDependencies(InstanceFactory factory);

    abstract Class getDefinitionClass();

    abstract Object getInstance();

    public String toString()
    {
        return "#<" + getClass().getSimpleName() + ":"
                + getDefinitionClass().getName() + ">";
    }
}
