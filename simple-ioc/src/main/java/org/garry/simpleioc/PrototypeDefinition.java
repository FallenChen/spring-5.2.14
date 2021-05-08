package org.garry.simpleioc;

/**
 * @ClassName PrototypeDefinition
 * @Description TODO
 * @Author cy
 * @Date 2021/5/8 16:51
 */
public class PrototypeDefinition extends FactoryDefinition{

    PrototypeDefinition(Environment env,Class klass)
    {
        super(env,klass);
    }

    Object getInstance()
    {
        return constructInstance(klass);
    }
}
