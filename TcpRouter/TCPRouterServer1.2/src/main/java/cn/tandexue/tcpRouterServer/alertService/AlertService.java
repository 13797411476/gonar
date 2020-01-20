package cn.tandexue.tcpRouterServer.alertService;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 提供在线监测报警功能
 */
public class AlertService extends Thread {

    private CloseableHttpClient httpClient = null;

    private RequestConfig requestConfig = null;

    private HttpGet httpGet = null;

    private Logger logger = Logger.getLogger(AlertService.class);

    @Override
    public void run() {
        //init...
        httpClient = HttpClients.createDefault();
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setRedirectsEnabled(true)
                .build();

        String stringResult = "";
        SendEmailUtil sendEmail = new SendEmailUtil("smtp.qq.com", "465", "smtp", "22093330@qq.com", "nlpxtuihvbhkcaei", "22093330@qq.com", "nothing", "nothing", null);

        boolean STOP = false;
        int retryTimes = 0;
        int emailTimes = 1;

        while (!STOP) {
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                ;
            }
            //connected...
            httpGet = new HttpGet("http://test.tan.zinsoft.com");
            httpGet.setConfig(requestConfig);
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    //stringResult = httpResponse.getEntity().getContent().toString();
                    //logger.debug("Recv String :"+stringResult);

                    //Normal
                    if (retryTimes > 0) retryTimes = 0;
                    if (emailTimes > 1) {
                        emailTimes = 1;
                        //SendEmail Resume...
                        sendEmail.send("私有云异常恢复", "已恢复");
                        logger.debug("Send Resume Email...");
                    }
                    httpGet.abort();
                    continue;
                } else {
                    //Other Status
                    ;
                }
            } catch (IOException e) {
                ;
            }
            //Error
            logger.debug("error");
            retryTimes++;
            if (retryTimes > 2 * emailTimes) {
                emailTimes = emailTimes * 2;
                //Send Email, ERROR....
                logger.debug("send error email...");
                sendEmail.send("私有云异常！" + retryTimes + "次", "已经重试了" + retryTimes + "次");
            }
            httpGet.abort();
        }
        //close...
        try {
            httpClient.close();
        } catch (IOException o) {
            ;
        }
    }
}
