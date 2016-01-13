package net.xuele.member.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sso restlet client
 * dependencys
 * org.apache.httpcomponents httpclient 4.5
 * slf4j
 * zhihuan.cai 新建于 2015/7/8 0008.
 */
public class XueleSSOClient {

    private static final Logger logger = LoggerFactory.getLogger(XueleSSOClient.class.getName());

    /**
     * http 请求状态码 201  成功创建TGT
     */

    private static final int HTTP_STATUS_CODE_201 = 201;

    /**
     * http 请求状态码 200
     */

    private static final int HTTP_STATUS_CODE_200 = 200;


    /**
     * 参数验证方法，保证参数不为 null
     *
     * @param object  需要验证的参数
     * @param message 验证的异常信息
     */
    private static void notNull(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 获取 ticket granting ticket
     *
     * @param server   cas 服务 url
     * @param username 验证的用户名
     * @param password 验证的用户密码
     * @return ticket granting ticket
     */

    public String getTicketGrantingTicket(final String server, final String username, final String password) {
        notNull(server, "server must not be null");
        notNull(username, "username must not be null");
        notNull(password, "password must not be null");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(server);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", username));
        nvps.add(new BasicNameValuePair("password", password));
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String content = entity == null ? "" : EntityUtils.toString(entity);
            logger.debug(content);

            switch (response.getStatusLine().getStatusCode()) {
                case HTTP_STATUS_CODE_201: {
                    final Matcher matcher = Pattern.compile(".*action=\\\".*/(.*?)\\\".*").matcher(content);
                    if (matcher.matches()) {
                        return matcher.group(1);
                    }
                }
                default:
                    logger.info("Invalid response code (" + response.getStatusLine() + ") from CAS server!");
                    logger.info("Response : " + content);
                    break;

            }
            EntityUtils.consume(entity);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * @param server
     * @param ticketGrantingTicket
     * @param service
     * @return
     * @throws IOException
     */
    public String getServiceTicket(String server, String ticketGrantingTicket, String service) {
        notNull(server, "server must not be null");
        notNull(ticketGrantingTicket, "ticketGrantingTicket must not be null");
        notNull(service, "service must not be null");


        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(server + "/" + ticketGrantingTicket);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("service", service));

        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            String content = entity == null ? "" : EntityUtils.toString(entity);
            logger.debug(content);

            switch (response.getStatusLine().getStatusCode()) {
                case HTTP_STATUS_CODE_200:
                    return content;
                default:
                    logger.info("Invalid response code ( " + response.getStatusLine().getStatusCode() + " ) from CAS server!");
                    logger.info("Response: " + content);
                    break;
            }
            EntityUtils.consume(entity);
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
            }
        }

        return null;
    }

    /**
     * @param service
     * @param serviceTicket
     * @throws IOException
     */
    public void getServiceCall(String service, String serviceTicket) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(service + "?ticket=" + serviceTicket);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String content = entity == null ? "" : EntityUtils.toString(entity);
            switch (response.getStatusLine().getStatusCode()) {
                case HTTP_STATUS_CODE_200:
                    logger.info("Response: " + content);
                    break;
                default:
                    logger.info("Invalid response code ( " + response.getStatusLine().getStatusCode() + " ) from CAS server!");
                    logger.info("Response: " + content);
                    break;
            }
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
            }
        }
    }

    /**
     * 登出CAS
     *
     * @param server               cas服务器地址
     * @param ticketGrantingTicket TGT
     * @throws IOException
     */
    public void logout(String server, String ticketGrantingTicket) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(server + "/" + ticketGrantingTicket);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            String content = entity == null ? "" : EntityUtils.toString(entity);
            switch (response.getStatusLine().getStatusCode()) {
                case HTTP_STATUS_CODE_200:
                    logger.info("Logged out");
                    break;
                default:
                    logger.info("Invalid response code ( " + response.getStatusLine().getStatusCode() + " ) from CAS server!");
                    logger.info("Response: " + content);
                    break;
            }
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } finally {
            if (response != null) {
                try {

                    response.close();
                } catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
            }
        }
    }

}
