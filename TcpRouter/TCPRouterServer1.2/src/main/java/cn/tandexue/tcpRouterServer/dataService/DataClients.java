package cn.tandexue.tcpRouterServer.dataService;

import cn.tandexue.tcpRouterServer.cmdService.CmdClients;
import cn.tandexue.tcpRouterServer.portService.PortClients;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataClients {
    private static Logger logger = Logger.getLogger(DataClients.class);
    private static int dataSocketDeadRetryTimes = 0;

    /**
     * 数据socket 及其状态
     * 0 初始化
     * 1 可用
     * 2 使用中
     */
    private static Map<ChannelHandlerContext, Integer> dataSocketMap = new HashMap<ChannelHandlerContext, Integer>();
    private static Map<ChannelHandlerContext, Integer> dataSocketMapUnused = new HashMap<ChannelHandlerContext, Integer>();
    private static Map<ChannelHandlerContext, Integer> dataSocketMapUsed = new HashMap<ChannelHandlerContext, Integer>();
    private static Map<ChannelHandlerContext, CountDownLatch> dataSockAck = new Hashtable<ChannelHandlerContext, CountDownLatch>();
    /**
     * 一个在getValidDataSocket方法中用的锁
     */
    private static Lock canGetValidDataSocketLock = new ReentrantLock();
    private static Integer nowGetValidDataSocketThreadNumber = 0;

    /**
     * 添加一个socket，状态值为1
     *
     * @param ctx
     */
    public static void addSocket(ChannelHandlerContext ctx) {
        synchronized (dataSocketMap) {
            dataSocketMap.put(ctx, 1);
            dataSocketMapUnused.put(ctx, null);
            dataSocketMap.notify();
        }
        logger.debug("DataSocket:新增一个空白DataSocket:" + ctx);
    }

    /**
     * 移除一个socket
     *
     * @param ctx
     */
    public static void removeSocket(ChannelHandlerContext ctx) {
        synchronized (dataSocketMap) {
            dataSocketMap.remove(ctx);
            dataSocketMapUnused.remove(ctx);
            dataSocketMapUsed.remove(ctx);
            dataSockAck.remove(ctx);
        }
        //把这个socket对应的 portSocket（如果有的话）也关闭掉
        PortClients.closePortSocketByDataSocket(ctx);
        logger.debug("DataSocket:移除了一个datasocket:" + ctx);
        try {
            ctx.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static void getSocketAck(ChannelHandlerContext dataSocket) {
        CountDownLatch countDownLatch = dataSockAck.get(dataSocket);
        if (countDownLatch != null) {
            countDownLatch.countDown();
            dataSockAck.remove(dataSocket);
        }
    }

    /**
     * 查询一个ctx是否已经被使用了，返回状态值
     */
    public static int getDataSocketStatus(ChannelHandlerContext ctx) {
        Integer dataSocketStatus = dataSocketMap.get(ctx);
        if (dataSocketStatus == null)
            return 0;
        return dataSocketStatus.intValue();
    }

    public static void CloseAllDataSocketRelatedCmdSocket(String host) {
        ArrayList<ChannelHandlerContext> channelHandlerContextsCopys = new ArrayList<ChannelHandlerContext>();
        synchronized (dataSocketMap) {
            Set<ChannelHandlerContext> channelHandlerContexts = dataSocketMap.keySet();
            channelHandlerContexts.forEach(ch -> {
                channelHandlerContextsCopys.add(ch);
            });
        }
        channelHandlerContextsCopys.forEach(ch -> {
            ch.close();
        });
    }

    public static void CloseAllUnusedDataSocketRelatedCmdSocket(String host) {
        ArrayList<ChannelHandlerContext> channelHandlerContextsCopys = new ArrayList<ChannelHandlerContext>();
        synchronized (dataSocketMap) {
            Set<ChannelHandlerContext> channelHandlerContexts = dataSocketMapUnused.keySet();
            channelHandlerContexts.forEach(ch -> {
                channelHandlerContextsCopys.add(ch);
            });
        }
        channelHandlerContextsCopys.forEach(ch -> {
            ch.close();
        });
    }

    public static boolean IfHaveUnusedDataSocket() {
        return dataSocketMapUnused.size() > 0;
    }


    /**
     * 获取一个可用的socket，如果没有，则线程等待
     *
     * @return
     * @throws InterruptedException
     */
    public static ChannelHandlerContext getValidSocket(int port) throws InterruptedException {
        canGetValidDataSocketLock.lock();
        nowGetValidDataSocketThreadNumber++;
        canGetValidDataSocketLock.unlock();
        ChannelHandlerContext resultCtx = null;
        synchronized (dataSocketMap) {
            int i = 1;
            while (true) {
                logger.debug("尝试获取DataSocket,次数：" + i + ",ALL(" + dataSocketMap.size() + ") Unused(" + dataSocketMapUnused.size() + ")");
                if (i > 5) {
                    synchronized (nowGetValidDataSocketThreadNumber) {
                        nowGetValidDataSocketThreadNumber--;
                    }
                    return null;
                }
                i++;
                for (Map.Entry<ChannelHandlerContext, Integer> entity :
                        dataSocketMap.entrySet()) {
                    if (entity.getValue() == 1) {
                        //将状态改成2，不可用
                        resultCtx = entity.getKey();
                        dataSocketMap.put(resultCtx, 2);
                        dataSocketMapUsed.put(resultCtx, null);
                        dataSocketMapUnused.remove(resultCtx);
                        break;
                    }
                }
                if (resultCtx == null) {
                    if (canGetValidDataSocketLock.tryLock()) {
                        try {
                            if (DataClients.dataSocketMapUnused.size() < 30) {
                                logger.debug("没有可用的dataSocket，申请中");
                                if (CmdClients.sendToClient("mallocDataSocket\r\n")) {
                                    logger.debug("申请完dataSocket，等待中");
                                    try {
                                        dataSocketMap.wait(2000);
                                    } catch (Exception e) {
                                        logger.debug("申请dataSocket超时");
                                    }
                                } else {
                                    logger.debug("申请dataSocket过程中发生错误");
                                    //return null;
                                }
                            }
                        } finally {
                            canGetValidDataSocketLock.unlock();
                        }
                    } else {
                        Thread.sleep(100);
                    }
                } else {
                    break;
                }
            }
        }
        //端口Port指定
        logger.debug("dataSocket发送port协议：" + port);
        CountDownLatch dataSocketCountdown = new CountDownLatch(1);
        dataSockAck.put(resultCtx, dataSocketCountdown);
        //发送port
        String msg = "#port#" + port + "#";
        final ByteBuf time = resultCtx.alloc().buffer(msg.length() * 2);
        time.writeBytes(msg.getBytes());
        final ChannelFuture f = resultCtx.writeAndFlush(time);
        try {
            f.await(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Long preAnswerTimeMillion = System.currentTimeMillis();
        if (dataSocketCountdown.await(150, TimeUnit.MILLISECONDS)) {
            //Long AfterAnswerTimeMillion = System.currentTimeMillis();
            //logger.debug("等待port应答时间：" + (AfterAnswerTimeMillion - preAnswerTimeMillion) + "ms");
            dataSocketDeadRetryTimes = 0;
            synchronized (nowGetValidDataSocketThreadNumber) {
                nowGetValidDataSocketThreadNumber--;
            }
            return resultCtx;
        } else {
            logger.debug("等待port应答超时，放弃dataSocket,重试中,失败总次数：" + dataSocketDeadRetryTimes);
            dataSockAck.remove(resultCtx);
            try {
                resultCtx.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
            dataSocketDeadRetryTimes++;
            synchronized (nowGetValidDataSocketThreadNumber) {
                nowGetValidDataSocketThreadNumber--;
            }
            if (dataSocketDeadRetryTimes > 3) {
                if (canGetValidDataSocketLock.tryLock()) {
                    try {
                        if (dataSocketDeadRetryTimes > 3) {
                            logger.debug("等待GetValidSocket线程全部完成，当前nowGetValidDataSocketThreadNumber=" + nowGetValidDataSocketThreadNumber);
                            // at most waiting for 1000times ,10millis for one time;
                            int waitTimesTemp1 = 1000;
                            while (nowGetValidDataSocketThreadNumber > 0) {
                                if (--waitTimesTemp1 > 0) {
                                    Thread.sleep(10);
                                } else {
                                    logger.debug("！！！！！！等待GetValidSocket超过1000次，结束等待");
                                    nowGetValidDataSocketThreadNumber = 0;
                                }
                            }
                            logger.debug("等待port应答超时超过3次，放弃全部未使用的DataSocket");
                            DataClients.CloseAllUnusedDataSocketRelatedCmdSocket("*");
                            logger.debug("等待Unused DataPort全部断开");
                            waitTimesTemp1 = 1000;
                            while (DataClients.IfHaveUnusedDataSocket()) {
                                if (--waitTimesTemp1 > 0) {
                                    Thread.sleep(10);
                                } else {
                                    logger.debug("！！！！！！等待Unused DataPort全部断开超过1000次，结束等待");
                                    break;
                                }
                            }
                            logger.debug("Unused DataPort已经全部断开");
                        }
                    } finally {
                        canGetValidDataSocketLock.unlock();
                    }
                } else {
                    Thread.sleep(100);
                }
            }
            return getValidSocket(port);
        }
    }
}
