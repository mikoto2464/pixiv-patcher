package net.mikoto.pixiv.crawler;

import net.mikoto.log.Logger;
import net.mikoto.pixiv.pojo.Crawler;
import net.mikoto.pixiv.pojo.Worker;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static net.mikoto.pixiv.util.FileUtil.writeFile;

/**
 * @author mikoto
 * @date 2022/1/2 1:00
 */
public class CrawlerManager {
    private static final CrawlerManager INSTANCE = new CrawlerManager();

    private final Map<String, Crawler> crawlerMap = new HashMap<>();

    public static CrawlerManager getInstance() {
        return INSTANCE;
    }

    public void loadCrawlers(@NotNull Map<String, Properties> crawlers, Logger logger) {
        for (Map.Entry<String, Properties> crawler :
                crawlers.entrySet()) {
            crawlerMap.put(crawler.getKey(), new Crawler(crawler.getValue(), crawler.getKey(), logger));
        }
    }

    public void startAll() {
        for (Crawler crawler :
                crawlerMap.values()) {
            crawler.start();
        }
    }

    public void saveAll() {
        for (Crawler crawler :
                crawlerMap.values()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer
                    .append("WORKER_COUNT=")
                    .append(crawler.getWorkerMap().size())
                    .append("\n");
            for (Map.Entry<Integer, Worker> worker :
                    crawler.getWorkerMap().entrySet()) {
                stringBuffer
                        .append("WORKER_")
                        .append(worker.getKey())
                        .append("_START=")
                        .append(worker.getValue().getStart())
                        .append("\n")
                        .append("WORKER_")
                        .append(worker.getKey())
                        .append("_STOP=")
                        .append(worker.getValue().getStop())
                        .append("\n")
                        .append("WORKER_")
                        .append(worker.getKey())
                        .append("_NOW=")
                        .append(worker.getValue().getNow())
                        .append("\n")
                        .append("WORKER_")
                        .append(worker.getKey())
                        .append("_SERVER=")
                        .append(worker.getValue().getServer())
                        .append("\n");
            }
            try {
                writeFile(new File("crawler\\" + crawler.getCrawlerName() + ".crawler"), stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Crawler> getCrawlerMap() {
        return crawlerMap;
    }
}