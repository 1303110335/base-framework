package com.xuleyan.frame.core.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * 测试JDK的WatchService监听文件变化
 */
public class WatchServiceUtil {

    public static void main(String[] args) throws IOException {
        // 要监听的目录
        String path = "/Users/xuleyan/code/base-framework/";

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path p = Paths.get(path);
        p.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_CREATE);

        Thread thread = new Thread(() -> {
           try {
               while (true) {
                   WatchKey watchKey = watchService.take();
                   List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                   for (WatchEvent<?> event : watchEvents) {
                       //TODO 根据事件类型采取不同的操作。。。。。。。
                       System.out.println("["+path+"/"+event.context()+"]文件发生了["+event.kind()+"]事件");
                   }
               }
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        });
        thread.setDaemon(false);
        thread.start();

        // 增加jvm关闭的钩子来关闭监听
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                watchService.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }
}
