package main.java.mdb.router;

import java.util.HashMap;
import java.util.Map;

public class Request {
    String path;
    String method;
    String body;
    Map<String, String> pathParameters;

    public Request(String method, String path, String body) {
        this.path = path;
        this.method = method;
        this.body = body;
        this.pathParameters = new HashMap<>();
    }

    public void addPathParameter(String name, String value) {
        pathParameters.put(name, value);
    }

    public String getPathParameter(String name) {
        return pathParameters.get(name);
    }

    public String getBody() {
        return body;
    }

}
