package org.garry.simpleioc;

/**
 * @ClassName SingletonDefinition
 * @Description TODO
 * @Author cy
 * @Date 2021/4/30 14:51
 */
class SingletonDefinition extends FactoryDefinition{

    SingletonDefinition(Environment env, Class klass)
    {
        super(env,klass);
    }

    Object instance;

    Object getInstance()
    {
        if(instance == null)
        {
            instance = constructInstance(klass);
        }
        return instance;
    }
}
