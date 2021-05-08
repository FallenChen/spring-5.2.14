package org.garry.simpleioc;

/**
 * @ClassName InstanceFactory
 * @Description TODO
 * @Author cy
 * @Date 2021/4/30 14:12
 */
public interface InstanceFactory {

    boolean containsInstance(Class klass);

    <T> T getInstance(Class<T> klass);

    <T> T getOptionalInstance(Class<T> klass);

    <T> T[] getInstances(Class<T> klass);
}
