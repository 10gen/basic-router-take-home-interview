package main.java.mdb.router;

@FunctionalInterface
public interface RequestHandler {
    Response handle(Request request);
}