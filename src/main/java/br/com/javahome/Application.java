package br.com.javahome;

import br.com.javahome.di.Bootstrap;
import br.com.javahome.enums.RoomType;
import br.com.javahome.enums.ViewType;
import br.com.javahome.logging.Logger;
import br.com.javahome.logging.LoggerFactory;
import br.com.javahome.ui.MainMenu;
import br.com.javahome.views.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class Application extends JFrame{

    private final Logger log = LoggerFactory.getLogger(Application.class);
    private final JPanel leftPanel;
    private final JPanel rightPanel;

    static {
        try {
            ApplicationContext.loadProperties(Application.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Application() throws Exception {

        Bootstrap bootstrap = new Bootstrap();
        var manager = bootstrap.initializeBeans();
        bootstrap.runInitializeMethods();
        frameInitialize();

        log.debug("Creating left and right panels.");
        leftPanel =  manager.getBean("leftPanel");
        leftPanel.setLayout(new CardLayout());
        rightPanel =  manager.getBean("rightPanel");
        rightPanel.setLayout(new CardLayout());

        var content = new JPanel();
        content.setLayout(new BorderLayout());
        MainMenu mainMenu = manager.getBean(MainMenu.class);
        content.add(rightPanel, BorderLayout.CENTER);

        content.add(mainMenu, BorderLayout.SOUTH);
        add(leftPanel);
        add(content);

        Runtime.getRuntime().addShutdownHook(new Thread(bootstrap::gracefullShutdown));
    }

    private void frameInitialize() {
        log.debug("Initializing UI interface.");
        setTitle(ApplicationContext.getProperty("app.title"));
        setSize(604, 431);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridLayout grid = new GridLayout(1, 2, 0, 0);
        setLayout(grid);
        setVisible(true);
    }


    public void start() {
        try{
            var manager = ApplicationContext.getManager();
            log.debug("Creating and load svg from house and rooms in left panel");
            leftPanel.add(manager.getBean(HouseView.class), ViewType.HOME.name());

            Arrays.stream(RoomType.values())
                    .forEach(roomType -> leftPanel.add(manager.getBean(roomType.getKey()), roomType.name()));
            log.debug("Creating and load views in right panel");

            rightPanel.add(manager.getBean(HouseDetailView.class).$$$getRootComponent$$$(), ViewType.HOME_DETAIL.name());
            rightPanel.add(manager.getBean(RoomDetailView.class).$$$getRootComponent$$$(), ViewType.ROOM_DETAIL.name());
            rightPanel.add(manager.getBean(LightView.class).$$$getRootComponent$$$(), ViewType.LIGHT.name());
            rightPanel.add(manager.getBean(ElectricalView.class).$$$getRootComponent$$$(), ViewType.ELECTRICAL.name());
            rightPanel.add(manager.getBean(TemperatureView.class).$$$getRootComponent$$$(), ViewType.TEMPERATURE.name());
            rightPanel.add(manager.getBean(SecurityView.class).$$$getRootComponent$$$(), ViewType.SECURITY.name());
        }catch (Exception e) {
            log.error("Error while starting app: ", e);
        }
    }


    public static Application init() throws Exception {
        return new Application();
    }

    public static void main(String[] args) throws Exception {
        var app = Application.init();
        app.start();
    }
}
