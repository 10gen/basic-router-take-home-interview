# MDB Take Home Project!
Welcome to the MDB Take Home. Today we'll be implementing a basic HTTP router.

Here are some pointers:

    * Pick any modern language you like! Some options are python, golang, java,
      javascript, rust, etc.
    * We prefer clean code that's easy to follow.
    * All solutions should be runnable easily.
    * All solutions should have tests verifying implemented functionality.

## What's the problem?
We (you!) are going to build an HTTP Router. We're looking for a clean
implementation that can:

    * Route an HTTP path and method to a function, and return an error code and
      message.
    * Support path parameters for HTTP paths (i.e. support "/foo/{bar}" where
      the same handler would execute for both "/foo/asdf" and "/foo/qwerty".
    * Support adding and executing arbitrary middlewares in the router.
      Middleware should support performing some functionality on both the
      incoming request and the outgoing response.

We've added some starter code in the `getting_started` directory. Feel free to
use that as a way to get moving, adapt it to the language of your choosing, or
disregard it completely for your own solution.

## How to submit a solution?
To submit a solution:

    * Fork this repository
    * Make your edits and push them to your fork
    * Open a PR!

## Questions?
If something here is unclear, feel free to reach out to you recruiter, and
they'll get you in contact with somebody who can answer your questions.
