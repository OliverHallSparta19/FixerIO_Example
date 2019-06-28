package FixerIO_Example;

import com.sun.org.apache.xml.internal.utils.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import javax.lang.model.element.ExecutableElement;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExample {

//    public static final int MAX_THREADS = Integer.parseInt(System.getProperty("max.threads"));
    public static final int MAX_THREADS = 6;

    protected ObjectPool pool;
    protected ExecutorService executableService;

    public void  setUp() throws Exception{
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(MAX_THREADS);
        boolean browserOverride = Boolean.parseBoolean(System.getProperty("browser.override"));
        String browserValue = System.getProperty("browser.value");
        if (browserOverride){
//            pool = new GenericObjectPool<>(new )
        } else {
//            pool = new  GenericObjectPool<>(new )
        }
        executableService = Executors.newFixedThreadPool(MAX_THREADS);

    }

}
