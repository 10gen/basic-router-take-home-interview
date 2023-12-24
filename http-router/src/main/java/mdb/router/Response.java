package main.java.mdb.router;

public class Response {

    Integer responseCode;
    String HTTPBody;

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getHTTPBody() {
        return HTTPBody;
    }

    public Response(Integer responseCode, String HTTPBody) {
        this.responseCode = responseCode;
        this.HTTPBody = HTTPBody;
    }

}
