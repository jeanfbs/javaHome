package br.com.javahome.di;

import br.com.javahome.ApplicationContext;
import br.com.javahome.client.HouseClient;
import br.com.javahome.client.RoomClient;
import br.com.javahome.client.core.DefaultWebSocketClientAsync;
import br.com.javahome.client.core.JsonWebSocketClient;
import br.com.javahome.collector.HomeCollectorData;
import br.com.javahome.collector.RoomCollectorData;
import br.com.javahome.enums.RoomType;
import br.com.javahome.event.DefaultEventBus;
import br.com.javahome.loader.HousePlanSVGLoader;
import br.com.javahome.logging.Logger;
import br.com.javahome.logging.LoggerFactory;
import br.com.javahome.ui.HousePlanUI;
import br.com.javahome.ui.MainMenu;
import br.com.javahome.views.*;
import tools.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Bootstrap {
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    private Map<String, Object> beans = null;
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private ScheduledExecutorService executorService = null;

    private void initSchedulers(ContainerBeanManager manager) {
        Runnable homeTask = manager.getBean(HomeCollectorData.class);
        Runnable roomTask = manager.getBean(RoomCollectorData.class);
        if(executorService != null){
            executorService.submit(homeTask);
            executorService.scheduleAtFixedRate(roomTask, 0, 5, TimeUnit.SECONDS);
        }
    }

    private JPanel createPanel() {
        return new JPanel();
    }

    public ContainerBeanManager initializeBeans() throws Exception {
        log.debug("Starting ContainerBeanManager.");
        ContainerBeanManager manager = ApplicationContext.getManager();

        log.debug("Injecting application properties.");
        ApplicationContext.getProperties().forEach((k, v) -> manager.register(String.valueOf(k), v));

        log.debug("Creating beans and resolving dependencies.");
        executorService = Executors.newScheduledThreadPool(5);
        manager.register(Executor.class, executorService);
        manager.register(DefaultEventBus.class);

        manager.register(HousePlanUI.class, HousePlanSVGLoader.getInstance().load("/plan.svg"));
        manager.register("leftPanel", createPanel());
        manager.register("rightPanel", createPanel());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);

        numberFormat.setMaximumFractionDigits(2);
        manager.register(NumberFormat.class, numberFormat);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        manager.register(DateTimeFormatter.class, dateTimeFormatter);

        manager.register(HouseView.class);
        Arrays.stream(RoomType.values()).forEach(e -> {
            try {
                manager.register(e.getKey(), new RoomView(HousePlanSVGLoader.getInstance().load("/%s.svg".formatted(e.getKey()))));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        manager.register(HouseDetailView.class);
        manager.register(RoomDetailView.class);
        manager.register(LightView.class);
        manager.register(ElectricalView.class);
        manager.register(TemperatureView.class);
        manager.register(SecurityView.class);
        manager.register(ObjectMapper.class, new ObjectMapper());

        manager.register(DefaultWebSocketClientAsync.class);
        manager.register(JsonWebSocketClient.class);
        manager.register(HouseClient.class);
        manager.register(RoomClient.class);

        manager.register(HomeCollectorData.class);
        manager.register(RoomCollectorData.class);
        manager.register(MainMenu.class);

        log.debug("Initializing schedulers.");
        initSchedulers(manager);
        beans = manager.getBeans();
        return manager;
    }

    public void runInitializeMethods() {
        if (beans != null) {
            for (Object instance : beans.values()) {
                runMethod(instance, Initialize.class);
            }
        }
    }

    public void gracefullShutdown() {

        if (beans != null) {
            for (Object instance : beans.values()) {
                runMethod(instance, Shutdown.class);
            }
        }
        if(executorService != null){
            executorService.shutdown();
        }
    }

    private void runMethod(Object instance, Class<? extends Annotation> clazz) {
        Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(clazz))
                .findFirst().ifPresent(method -> {
                    try {
                        MethodHandle mh = lookup.unreflect(method);
                        mh.invoke(instance);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
