package br.com.javahome;

import br.com.javahome.di.ContainerBeanManager;
import br.com.javahome.enums.RoomType;
import br.com.javahome.enums.ViewType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationContext {

    private final static Properties properties = new Properties();
    private final static ContainerBeanManager manager = new ContainerBeanManager();
    public static RoomType roomTypeCurrent = null;
    public static ViewType currentViewType = null;


    public static void loadProperties(InputStream inputStream) throws IOException {
        properties.load(inputStream);
    }

    public static String getProperty(String key){
        return properties.getProperty(key, "");
    }

    public static Properties getProperties(){
        return properties;
    }

    public static ContainerBeanManager getManager(){
        return manager;
    }

}
