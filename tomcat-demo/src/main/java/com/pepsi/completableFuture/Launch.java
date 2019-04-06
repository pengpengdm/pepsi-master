package com.pepsi.completableFuture;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/16
 * describe:
 */
public class Launch {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        List<String> list = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        final List<Integer> taskList = Lists.newArrayList(2,1,3,4,5,6,7,8,9,10);
        for(int i=0;i<taskList.size();i++){
                final int j=i;
                //异步执行
                CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        return calc(taskList.get(j));
                    }
                }, executorService)
                    //Integer转换字符串    thenAccept只接受不返回不影响结果
                    .thenApply(e->Integer.toString(e))
                    //如需获取任务完成先后顺序，此处代码即可
                    .whenComplete((v, e) -> {
                        System.out.println("任务"+v+"完成!result="+v+"，异常 e="+e+","+new Date());
                        list2.add(v);
                    });
                futureList.add(future);
            }
        //流式获取结果：此处是根据任务添加顺序获取的结果
        list = sequence(futureList).get();
        System.out.println(list.size());
    }

    public static Integer calc(Integer i){
        try {
            if(i==1){
                //任务1耗时3秒
                Thread.sleep(3000);
            }else if(i==5){
                //任务5耗时5秒
                Thread.sleep(5000);
            }else{
                //其它任务耗时1秒
                Thread.sleep(1000);
            }
            System.out.println("task线程："+Thread.currentThread().getName()+"任务i="+i+",完成！+"+new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     *
     * @Description 组合多个CompletableFuture为一个CompletableFuture,所有子任务全部完成，
     * 组合后的任务才会完成。带返回值，可直接get.
     * @param futures List
     * @return
     * @author diandian.zhang
     * @date 2017年6月19日下午3:01:09
     * @since JDK1.8
     */
    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        //1.构造一个空CompletableFuture，子任务数为入参任务list size
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        //2.流式（总任务完成后，每个子任务join取结果，后转换为list）
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    /**
     *
     * @Description Stream流式类型futures转换成一个CompletableFuture,所有子任务全部完成，组合后的任务才会完成。带返回值，可直接get.
     * @param futures Stream
     * @return
     * @author diandian.zhang
     * @date 2017年6月19日下午6:23:40
     * @since JDK1.8
     */
    public static <T> CompletableFuture<List<T>> sequence(Stream<CompletableFuture<T>> futures) {
        List<CompletableFuture<T>> futureList = futures.filter(f -> f != null).collect(Collectors.toList());
        return sequence(futureList);
    }

}
