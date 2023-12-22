#!/usr/bin/env python3

import typing

"""Starter code for a python router."""

ErrorCode = int
HTTPBody = str
RequestResponse = typing.Tuple[ErrorCode, HTTPBody]

class Router:
    """Router class."""

    def __init__(self):
        """Constructor."""
        pass

    def add_route(self, method: str, path: str, handler: typing.typing.Callable[[], RequestResponse]) -> None:
        """Add a route to the router."""
        pass

    def route(self, method: str, path: str, body: str) -> RequestResponse:
        """Route an HTTP request, returing the repsonse."""
        pass

def main() -> None:
	"""Main method!"""
	router = Router()
	def echo(body) -> RequestResponse:
		"""Echo some data back."""
		return 200, body
	router.add_route("GET", "/echo", echo)
	assert (200, "it works!") == router.route("GET", "/echo", "it works!")

if __name__ == "__main__":
	main()
