package tdd.vendingMachine.application;

import static ch.lambdaj.Lambda.join;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.runner.RunWith;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=IntegrationTest.TestContextLoader.class)
public abstract class IntegrationTest {

    private String endpointUrl;
    
    public IntegrationTest(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
    
    protected String json(String... fields) {
        StringBuilder json = new StringBuilder("");
        json.append(join(fields, ""));
        json.append("");
        return json.toString().replaceAll("'", "\"");//.replaceAll("\\s", "");
    }
    
    protected String post(String params, String body) throws UnsupportedEncodingException, IOException, ClientProtocolException {
    
        HttpPost post = buildPost(params, body);
        HttpResponse response = execute(post);
        String responseAsText = extractResponse(response);
        assertValidStatusCode(response);
    
        return responseAsText;
    }

    private void assertValidStatusCode(HttpResponse response) {
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
    }

    private HttpResponse execute(HttpRequestBase request) throws IOException, ClientProtocolException {
        return new DefaultHttpClient().execute(request);
    }

    private String extractResponse(HttpResponse response) throws IOException {
    
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(response.getEntity().getContent(), output);
        String responseAsText = output.toString();
        System.err.println(String.format("Response:\n\t%s\n---", responseAsText));
        return responseAsText;
    }

    private HttpPost buildPost(String params, String body) throws UnsupportedEncodingException {
        
        String url = endpointUrl + params;

        System.err.println(String.format("Request (POST):\n\t%s\n%s", url, body));
    
        HttpPost post = new HttpPost(url);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(body));
        return post;
    }

    protected String get(String params) throws IOException {
        
        HttpGet get = buildGet(params);
        HttpResponse response = execute(get);
        String responseAsText = extractResponse(response);
        assertValidStatusCode(response);
    
        return responseAsText;
    }
    
    private HttpGet buildGet(String params) {
        
        String url = endpointUrl + params;
        
        System.err.println(String.format("Request (GET):\n\t%s\n", url));
        
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-Type", "application/json");
        return get;
    }
    
    public static class TestContextLoader extends AnnotationConfigContextLoader{
        
        @Override
        protected void customizeContext(GenericApplicationContext context) {
            context.setParent(IntegrationTestSuite.context());
        }
        
    }
}