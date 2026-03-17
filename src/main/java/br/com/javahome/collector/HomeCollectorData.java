package br.com.javahome.collector;

import br.com.javahome.client.HouseClient;
import br.com.javahome.event.EventBus;
import br.com.javahome.logging.Logger;
import br.com.javahome.logging.LoggerFactory;

import static br.com.javahome.event.EventType.HOME_DATA_FETCH;

public class HomeCollectorData implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(HomeCollectorData.class);
    private final HouseClient houseClient;
    private final EventBus eventBus;

    public HomeCollectorData(HouseClient houseClient, EventBus eventBus) {
        log.info("Initializing home collector data.");
        this.houseClient = houseClient;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {

        houseClient.fetchHomeData().thenAccept(homeData -> eventBus.publish(HOME_DATA_FETCH, () -> homeData));
    }
}
