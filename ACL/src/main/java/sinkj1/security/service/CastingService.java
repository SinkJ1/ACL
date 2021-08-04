package sinkj1.security.service;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class CastingService {

    public static void cast(Object o) throws IllegalArgumentException, IllegalAccessException{
        Class<? extends Object> clazz = o.getClass();
        //clazz.cast(o);
        System.out.println(clazz.getName() + " >> " + clazz.getDeclaredFields().length);
        for(Field f: clazz.getDeclaredFields()){
            f.setAccessible(true);
            System.out.println( f.getName()  + "=" + f.get(o));
        }
    }
}
