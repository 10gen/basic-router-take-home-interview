package main.java.mdb.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouterTest {

    private Router router;

    @BeforeEach
    void setup() {
        router = new Router();
        router.addRoute("GET", "/echo", request -> new Response(200, request.getBody()));
        router.addRoute("GET", "/foo/{bar}", request -> new Response(200, request.getPathParameter("bar")));
        router.addRoute("POST", "/submit", request -> new Response(201, "Created"));
        router.addRoute("GET", "/items/{itemId}", request -> new Response(200, request.getPathParameter("itemId")));
        router.addRoute("GET", "/users/{userId}/posts/{postId}/comments/{commentId}", request -> {
            String response = "User: " + request.getPathParameter("userId") +
                    ", Post: " + request.getPathParameter("postId") +
                    ", Comment: " + request.getPathParameter("commentId");
            return new Response(200, response);
        });
    }

    @Test
    void testAddRouteWithInvalidPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            router.addRoute("GET", "/echo/{", request -> new Response(200, "Invalid path"));
        }, "Router should throw IllegalArgumentException for invalid path");
    }
    @Test
    void test_Echo_Route() {
        Response response = router.route("GET", "/echo", "Hello, MongoDB!");
        assertEquals(200, response.getResponseCode());
        assertEquals("Hello, MongoDB!", response.getHTTPBody());
    }

    @Test
    void test_PathParameter_Route() {
        Response response = router.route("GET", "/foo/MongoDB", "");
        assertEquals(200, response.getResponseCode());
        assertEquals("MongoDB", response.getHTTPBody());
    }

    @Test
    void test_Post_Route() {
        Response response = router.route("POST", "/submit", "MongoDB");
        assertEquals(201, response.getResponseCode());
        assertEquals("Created", response.getHTTPBody());
    }

    @Test
    void test_NotFound_Route() {
        Response response = router.route("GET", "/notfound", "");
        assertEquals(404, response.getResponseCode());
    }

    @Test
    void test_BadRequest_DueTo_EmptyMethod() {
        Response response = router.route("", "/echo", "Hello, MongoDB!");
        assertEquals(400, response.getResponseCode());
    }

    @Test
    void test_BadRequest_DueTo_NullMethod() {
        Response response = router.route(null, "/echo", "Hello, MongoDB!");
        assertEquals(400, response.getResponseCode());
    }

    @Test
    void test_BadRequest_DueTo_EmptyPath() {
        Response response = router.route("GET", "", "Hello, MongoDB!");
        assertEquals(400, response.getResponseCode());
    }

    @Test
    void test_BadRequest_DueTo_NullPath() {
        Response response = router.route("GET", null, "Hello, MongoDB!");
        assertEquals(400, response.getResponseCode());
    }


    @Test
    void test_MultiplePath_Parameters() {
        router.addRoute("GET", "/users/{userId}/posts/{postId}",
                req -> new Response(200, "User: " + req.getPathParameter("userId") + ", Post: " + req.getPathParameter("postId")));
        Response response = router.route("GET", "/users/anshu/posts/1111", "");
        assertEquals(200, response.getResponseCode());
        assertEquals("User: anshu, Post: 1111", response.getHTTPBody());
    }

    @Test
    void test_Overlapping_Routes() {
        router.addRoute("GET", "/items/special", req -> new Response(200, "Special Items"));
        Response responseForSpecial = router.route("GET", "/items/special", "");
        assertEquals(200, responseForSpecial.getResponseCode());
        assertEquals("Special Items", responseForSpecial.getHTTPBody());

        Response responseForParam = router.route("GET", "/items/11", "");
        assertEquals(200, responseForParam.getResponseCode());
        assertEquals("11", responseForParam.getHTTPBody());
    }

    @Test
    void test_CaseInsensitive_Method() {
        Response response = router.route("get", "/echo", "Hello, MongoDB!");
        assertEquals(200, response.getResponseCode());
        assertEquals("Hello, MongoDB!", response.getHTTPBody());
    }

    @Test
    void test_StaticPath_Priority() {
        router.addRoute("GET", "/foo/static", req -> new Response(200, "Static Route"));
        Response response = router.route("GET", "/foo/static", "");
        assertEquals(200, response.getResponseCode());
        assertEquals("Static Route", response.getHTTPBody());
    }

    @Test
    void test_DifferentMethods_SamePath() {

        router.addRoute("POST", "/echo", req -> new Response(200, "POST Echo"));

        Response getResponse = router.route("GET", "/echo", "Hello, MongoDB!");
        assertEquals(200, getResponse.getResponseCode());
        assertEquals("Hello, MongoDB!", getResponse.getHTTPBody());

        Response postResponse = router.route("POST", "/echo", "");
        assertEquals(200, postResponse.getResponseCode());
        assertEquals("POST Echo", postResponse.getHTTPBody());
    }

    @Test
    void tes_InvalidHttp_Method() {
        Response response = router.route("INVALID", "/echo", "Hello, MongoDB!");
        assertEquals(405, response.getResponseCode());
    }

    @Test
    void test_NestedDynamic_PathParameters() {
        Response response = router.route("GET", "/users/anshu/posts/111/comments/222", "");
        assertEquals(200, response.getResponseCode());
        assertEquals("User: anshu, Post: 111, Comment: 222", response.getHTTPBody());
    }

    @Test
    void test_ResponseWith_NoBody() {
        Response response = router.route("POST", "/submit", "");
        assertEquals(201, response.getResponseCode());
        assertEquals("Created", response.getHTTPBody());
    }

    @Test
    void test_MultipleDynamicParameters() {
        router.addRoute("GET", "/mix/{param1}/{param2}", req -> new Response(200, req.getPathParameter("param1") + "," + req.getPathParameter("param2")));
        Response response = router.route("GET", "/mix/anshu/prasad", "");
        assertEquals(200, response.getResponseCode());
        assertEquals("anshu,prasad", response.getHTTPBody());
    }

}
