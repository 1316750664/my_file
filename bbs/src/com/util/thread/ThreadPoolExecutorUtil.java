package com.util.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hzm on 2014/7/28.
 */
public class ThreadPoolExecutorUtil {
    /**
     * 第1步：PoolSize指的是当前的任务数量。CorePoolSize指的是线程池可以存活着的线程数量。当任务线程数量小于线程池中存活着的线程数量，则直接运行，不用将任务存于阻塞队列中。
     * 第2步：当当前的任务数量PoolSize>CorePoolSize，则将多余的线程任务放到阻塞队列中。
     * 第3步：当第2步不断的累积线程任务到队列无法存的时候，将会创建新的线程到线程池中
     * 第4步：当第3步不断创建线程，直到线程池的最大容量（MaximumPoolSize）的时候，任务线程数量还是不断增加，则有RejectedExecutionHandler来进行处理。
     */
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAXIMUM_POOL_SIZE = 1000;
    private static final long KEEP_ALIVE_TIME = 20;
    private static final ThreadFactory THREADFACTORY = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        }
    };
    private static final RejectedExecutionHandler REJECTEDEXECUTIONHANDLER = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                aWorkQueue.put(r);//add，offer，put三种添加线程到队列的方法只在队列满的时候有区别，add为抛异常，offer返回boolean值，put直到添加成功为止
                //remove，poll，take三种移除队列中线程的方法只在队列为空的时候有区别， remove为抛异常，poll为返回boolean值， take等待直到有线程可以被移除
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private static final LinkedBlockingQueue<Runnable> aWorkQueue;
    private static final ThreadPoolExecutor threadPoolExecutor;

    static {
        aWorkQueue = new LinkedBlockingQueue<Runnable>(2 * CORE_POOL_SIZE);
        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, aWorkQueue, THREADFACTORY, REJECTEDEXECUTIONHANDLER);
    }

    private static class SingletonThreadPoolUtil {
        private static ThreadPoolExecutorUtil INSTANCE = new ThreadPoolExecutorUtil();
    }

    public static ThreadPoolExecutorUtil getInstance() {
        return SingletonThreadPoolUtil.INSTANCE;
    }

    private ThreadPoolExecutorUtil() {
    }

    public void addTask(Runnable task) {
        threadPoolExecutor.execute(task);
    }

    public void stopThreadPoolExecutor() {
        threadPoolExecutor.shutdownNow();
    }
}
