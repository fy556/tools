package swt;


import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class httpconn {

	public static void main(String args[]) throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://238055yn22.qicp.vip:33979/testlink/login.php");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		int statusCode=response1.getStatusLine().getStatusCode();
		try {
		    System.out.println(response1.getStatusLine());
		    
		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    EntityUtils.consume(entity1);
		    System.out.println();
		} finally {
		    response1.close();
		}

	}

}
