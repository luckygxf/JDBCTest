package com.gxf.idserver;

import com.gxf.jdbc.IDServer;
import org.junit.Test;

/**
 * Created by 58 on 2018/1/31.
 */
public class TestServerId {

    @Test
    public void getCurId(){
        IDServer idServer = new IDServer();
        long curId = idServer.getCurId();
        System.out.println("curId: " + curId);
    }

    @Test
    public void getServerId(){
        IDServer idServer = new IDServer();
        long startTime = System.currentTimeMillis();
        long preId = idServer.getId();
        Thread t1 = new Thread(new GetIdTask(idServer, "t1"));
        Thread t2 = new Thread(new GetIdTask(idServer, "t2"));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long curId = idServer.getCurId();
        System.out.println("last id: " + idServer.getId());
        System.out.println("time used: " + (System.currentTimeMillis() - startTime) / 1000);
        System.out.println("preId: " + preId + ", " + "curId: " + curId + ", " + "product id nums: " + (curId - preId));
    }


    class GetIdTask implements Runnable{
        private IDServer idServer;
        private int count = 0;
        private final int MAX_COUNT = 10000;
        private String name;

        public GetIdTask(IDServer idServer, String name) {
            this.idServer = idServer;
            this.name = name;
        }

        public void run() {
            while(count < MAX_COUNT){
                System.out.println(name + ", " + "count:" + count +  ", " +  idServer.getId());
                count++;
            }
        }
    }
}
