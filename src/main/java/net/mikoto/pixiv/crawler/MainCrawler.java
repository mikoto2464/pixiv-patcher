package net.mikoto.pixiv.crawler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.mikoto.log.Logger;
import net.mikoto.pixiv.Main;
import net.mikoto.pixiv.pojo.Worker;

import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mikoto
 * @date 2021/12/19 4:14
 */
public class MainCrawler {
    /**
     * 单例
     */
    private static final MainCrawler INSTANCE = new MainCrawler();

    /**
     * 线程池
     */
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Integer.parseInt(Main.PROPERTIES.getProperty("WORKER_COUNT")), Integer.parseInt(Main.PROPERTIES.getProperty("WORKER_COUNT")), 1, TimeUnit.HOURS, new LinkedBlockingDeque<>(), new ThreadFactoryBuilder().setNameFormat("mikoto-pixiv-worker-%d").build());

    private boolean startFlag = false;
    private Logger logger;

    public static MainCrawler getInstance() {
        return INSTANCE;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void startCrawler(Map<Integer, Worker> workerMap) {
        if (!startFlag) {
            logger.log("===================================================================================================");
            logger.log(",--.   ,--.,--.,--. ,--. ,-----. ,--------. ,-----.        ,------. ,--.,--.   ,--.,--.,--.   ,--. ");
            logger.log("|   `.'   ||  ||  .'   /'  .-.  ''--.  .--''  .-.  ',-----.|  .--. '|  | \\  `.'  / |  | \\  `.'  /  ");
            logger.log("|  |'.'|  ||  ||  .   ' |  | |  |   |  |   |  | |  |'-----'|  '--' ||  |  .'    \\  |  |  \\     /   ");
            logger.log("|  |   |  ||  ||  |\\   \\'  '-'  '   |  |   '  '-'  '       |  | --' |  | /  .'.  \\ |  |   \\   /    ");
            logger.log("`--'   `--'`--'`--' '--' `-----'    `--'    `-----'        `--'     `--''--'   '--'`--'    `-'");
            logger.log("===================================================================================================");
            startFlag = true;
            for (Map.Entry<Integer, Worker> entry :
                    workerMap.entrySet()) {
                threadPoolExecutor.execute(entry.getValue().getRunnable());
            }
        }
    }

    public void stopCrawler() {
        if (startFlag) {
            startFlag = false;
            threadPoolExecutor.shutdownNow();
        }
    }
}