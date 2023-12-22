#!/usr/bin/env python3

"""Starter code for a python router."""

import typing

ResponseCode = int
HTTPBody = str
RequestResponse = typing.Tuple[ResponseCode, HTTPBody]
RequestHandler = typing.Callable[[str], RequestResponse]


class Router:
    """Router class."""

    def __init__(self) -> None:
        """Constructor."""
        pass

    def add_route(
        self,
        method: str,
        path: str,
        handler: RequestHandler,
    ) -> None:
        """Add a route to the router."""
        pass

    def route(self, method: str, path: str, body: HTTPBody) -> RequestResponse:
        """Route an HTTP request, returing the repsonse."""
        pass


def main() -> None:
    """Main method!"""
    router = Router()

    def echo(body: HTTPBody) -> RequestResponse:
        """Echo some data back."""
        return 200, body

    router.add_route("GET", "/echo", echo)
    assert (200, "it works!") == router.route("GET", "/echo", "it works!")


if __name__ == "__main__":
    main()
