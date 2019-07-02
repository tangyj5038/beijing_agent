package com.hs.beijing_agent.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 */
@Component("httpClientTemplate")
public class HttpClientTemplate {

	private static Logger logger = LoggerFactory.getLogger(HttpClientTemplate.class);
	
    private MultiThreadedHttpConnectionManager multiThreadConnManager;

    private HttpClient client;
    
    private static CloseableHttpClient clientTwo;

    public HttpClientTemplate() {
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler());
        params.setMaxTotalConnections(10);
        params.setDefaultMaxConnectionsPerHost(5);
        params.setSoTimeout(10 * 1000);
        params.setConnectionTimeout(10 * 1000);
        multiThreadConnManager = new MultiThreadedHttpConnectionManager();
        multiThreadConnManager.setParams(params);
    }

    private HttpClient getHttpClient() {
        if (client == null)
            synchronized (this) {
                client = new HttpClient(multiThreadConnManager);
                HttpClientParams params = new HttpClientParams();
                params.setContentCharset("UTF-8");
                client.setParams(params);
            }
        return client;
    }

    private GetMethod genMethod(String baseUrl, String queryString) {
        GetMethod get = new GetMethod(baseUrl);
        get.setQueryString(queryString);
        return get;
    }

    private GetMethod genMethod(String baseUrl, Map<String, Object> paramMap) {
        GetMethod get = new GetMethod(baseUrl);
        HttpMethodParams params = new HttpMethodParams();
        params.setContentCharset("UTF-8");
        get.setParams(params);
        if (paramMap != null) {
            NameValuePair[] nvp = new NameValuePair[paramMap.values().size()];
            int i = 0;
            for (Map.Entry<String, Object> entry: paramMap.entrySet()) {
                nvp[i++] = new NameValuePair(entry.getKey(), entry.getValue()
                    .toString());
            }
            get.setQueryString(nvp);
        }
        return get;
    }

    public String execute(String baseUrl, Map<String, Object> paramMap) {
    	if(logger.isDebugEnabled()) {
    		logger.debug("[sending] url={}, param={}", baseUrl, JSON.toJSONString(paramMap));
    	}
    	try {
	        GetMethod method = genMethod(baseUrl, paramMap);
	        String result = execute(method);
	        if(logger.isDebugEnabled()) {
	    		logger.debug("[receiving] result ={}, url={}, param={}",result, baseUrl, JSON.toJSONString(paramMap));
	    	}
	        return result;
    	}
    	catch(Exception e) {
    		logger.error("[http exception] url={}, param={}", baseUrl, JSON.toJSONString(paramMap), e);
    		return null;
    	}   
    }
    
    public String excuteJson(String baseUrl, Map<String, Object> paramMap)
    	throws HttpException, IOException{
    	if(logger.isDebugEnabled()) {
    		logger.debug("[sending json] url={}, param={}", baseUrl, JSON.toJSONString(paramMap));
    	}
    	 JSONObject json = new JSONObject();
    	 json.putAll(paramMap);
    	 PostMethod method = new PostMethod(baseUrl);
    	 RequestEntity requestEntity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
    	 method.setRequestEntity(requestEntity);
    	 String result = execute(method);
    	 if(logger.isDebugEnabled()) {
     		logger.debug("[receiving json] result ={}, url={}, param={}",result, baseUrl, JSON.toJSONString(paramMap));
     	}
    	 return result;
    }
 

    public String executePost(String baseUrl, Map<String, Object> paramMap){
    	try {
	    	if(logger.isDebugEnabled()) {
	    		logger.debug("[sending post] url={}, param={}", baseUrl, JSON.toJSONString(paramMap));
	    	}
	        PostMethod method = new PostMethod(baseUrl);
	        HttpMethodParams params = new HttpMethodParams();
	        params.setContentCharset("UTF-8");
	        method.setParams(params);
	        if (paramMap != null) {
	            NameValuePair[] nvp = new NameValuePair[paramMap.values().size()];
	            int i = 0;
	            for (Map.Entry<String, Object> entry: paramMap.entrySet()) {
	                nvp[i++] = new NameValuePair(entry.getKey(), entry.getValue()
	                    .toString());
	            }
	            method.addParameters(nvp);
	        }
	        String result = execute(method);
	        if(logger.isDebugEnabled()) {
	     		logger.debug("[receiving post] result ={}, url={}, param={}",result, baseUrl, JSON.toJSONString(paramMap));
	     	}
	    	 return result;
    	}
    	catch(Exception e) {
    		logger.error("[http post exception] url={}, param={}", baseUrl, JSON.toJSONString(paramMap), e);
    		return null;
    	}
    }

    public String executePost(String baseUrl, Map<String, Object> paramMap,
        Map<String, Object> headerParams) throws HttpException, IOException {
    	if(logger.isDebugEnabled()) {
    		logger.debug("[sending head] url={}, param={}, head={}", baseUrl, 
    				JSON.toJSONString(paramMap), JSON.toJSONString(headerParams));
    	}
        PostMethod method = new PostMethod(baseUrl);
        HttpMethodParams params = new HttpMethodParams();
        params.setContentCharset("UTF-8");
        method.setParams(params);
        if (paramMap != null) {
            NameValuePair[] nvp = new NameValuePair[paramMap.values().size()];
            int i = 0;
            for (Map.Entry<String, Object> entry: paramMap.entrySet()) {
                nvp[i++] = new NameValuePair(entry.getKey(), entry.getValue()
                    .toString());
            }
            method.addParameters(nvp);
        }

        if (headerParams != null) {
            // HttpMethodParams mparams = new HttpMethodParams();
            // mparams.setContentCharset("UTF-8");
            // mparams.setVirtualHost(headerParams.get("hostUrl").toString());
            // method.setParams(mparams);
            method.getParams().setVirtualHost(
                headerParams.get("hostUrl").toString());
        }
        String result = execute(method, headerParams);
        if(logger.isDebugEnabled()) {
     		logger.debug("[receiving head] result ={}, url={}, param={}, head={}",result, baseUrl, 
     				JSON.toJSONString(paramMap), JSON.toJSONString(headerParams));
     	}
    	 return result;
    }

    public String execute(String url) throws HttpException, IOException {
        GetMethod method = new GetMethod(url);
        return execute(method);
    }

    public String execute(String baseUrl, String queryString)
        throws HttpException, IOException {
        GetMethod method = genMethod(baseUrl, queryString);
        return execute(method);
    }

    private String execute(HttpMethod method) throws HttpException, IOException {
        try {
            int statusCode = getHttpClient().executeMethod(method);
            if (200 != statusCode) {
                throw new HttpException("status code: " + statusCode);
            } else {
               // return method.getResponseBodyAsString();
            	InputStream resStream = method.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                    resStream, "UTF-8"));
                StringBuffer resBuffer = new StringBuffer();
                String resTemp = "";
                while ((resTemp = br.readLine()) != null) {
                    resBuffer.append(resTemp);
                }
                return resBuffer.toString();
            }
        } finally {
            if (null != method)
                method.releaseConnection();
        }
    }

    private String execute(HttpMethod method, Map<String, Object> headerParams)
        throws HttpException, IOException {
        try {
            HttpClient httpClient = getHttpClient();
            if (headerParams != null) {
                HostConfiguration hf = new HostConfiguration();
                if (headerParams.get("hostUrl") != null) {
                    hf.setHost(headerParams.get("hostUrl").toString(), 80,
                        Protocol.getProtocol("http"));
                }
                httpClient.setHostConfiguration(hf);
            }
            int statusCode = httpClient.executeMethod(method);
            if (200 != statusCode) {
                throw new HttpException("status code: " + statusCode);
            } else {
                // return method.getResponseBodyAsString();
                InputStream resStream = method.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                    resStream, "UTF-8"));
                StringBuffer resBuffer = new StringBuffer();
                String resTemp = "";
                while ((resTemp = br.readLine()) != null) {
                    resBuffer.append(resTemp);
                }
                return resBuffer.toString();
            }
        } finally {
            if (null != method)
                method.releaseConnection();
        }
    }

    public static class Factory {
        private static HttpClientTemplate instance = new HttpClientTemplate();

        public static HttpClientTemplate getClient() {
            return Factory.instance;
        }
    }
    
    
    public static String postXml(String url, String xmlData){  
    	try{
    		if (HttpClientTemplate.clientTwo == null){  
            	clientTwo = HttpClients.createDefault();  
            }  
            HttpPost post = new HttpPost(url);  
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            parameters.add(new BasicNameValuePair("xml", xmlData));  
            post.setEntity(new UrlEncodedFormEntity(parameters,"UTF-8"));  
            HttpResponse response = clientTwo.execute(post);  
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();  
            String result = EntityUtils.toString(entity, "UTF-8");  
            return result;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        return "";  
    }  
}
