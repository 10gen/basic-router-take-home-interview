package main.java.mdb.router;

public class Main {
    public static void main(String[] args) {

        Router router = new Router();
        router.addRoute("GET", "/echo", req -> new Response(200, req.getBody()));
        router.addRoute("POST", "/submit", req -> new Response(201, "Created"));
        router.addRoute("PUT", "/update", req -> new Response(200, "Updated"));
        router.addRoute("DELETE", "/delete", req -> new Response(200, "Deleted"));
        router.addRoute("GET", "/foo/{bar}", request -> new Response(200, "Received parameter: " + request.getPathParameter("bar")));

        Response echoResponse = router.route("GET", "/echo", "Hello, MongoDB!");
        System.out.println("GET Response: " + echoResponse.getHTTPBody());

        Response postResponse = router.route("POST", "/submit", "POST");
        System.out.println("POST Response: " + postResponse.getHTTPBody());

        Response putResponse = router.route("PUT", "/update", "PUT");
        System.out.println("PUT Response: " + putResponse.getHTTPBody());

        Response deleteResponse = router.route("DELETE", "/delete", "");
        System.out.println("DELETE Response: " + deleteResponse.getHTTPBody());

        Response patternResponse = router.route("GET", "/foo/MongoDB", "GET Dynamic");
        System.out.println("GET Response for Dynamic route : " + patternResponse.getHTTPBody());

    }
}
