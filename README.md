# Server-side rendering example `HOGE`

Featuring:

* [re-frame](https://day8.github.io/re-frame/)
* [reitit](https://cljdoc.org/d/metosin/reitit/0.5.18/doc/introduction)
* [shadow-cljs](https://shadow-cljs.github.io/docs/UsersGuide.html)
* [Deno](https://deno.land/)
* docker-compose

## What's HOGE

The Japanese version of `foo`.

## How it works

It uses nginx as load balancer/ingress. It pushes every resource call (JS, CSS etc) to shadow-cljs's server, while everything else goes through to server-side rendering.

Before hydrating the rendered HTML, `hoge.core/hydrate` rebuilds the application state and waits for completion (using the `::loaded` event as callback). It's the same as it works when it gets rendered on Deno, so there should be no hydration errors.

## Stuff to pay attention to

### No History

Deno has no [History API](https://developer.mozilla.org/en-US/docs/Web/API/History_API), so Reitit's defaults don't work there. In the current version it's worked around by manually calling `hoge.routes/navigate` from the renderer-side script. If you want to utilize some Reitit features that depend on History, such as `reitit.frontend.easy/href`, you'll have to stub a `reitit.frontend.history/History` object for the server-side rendering.

### Fetch is different

The [fetch API](https://developer.mozilla.org/en-US/docs/Web/API/fetch) works slightly differently from what most people are used to:

>A fetch() promise only rejects when a network error is encountered (which is usually when there's a permissions issue or similar). A fetch() promise does not reject on HTTP errors (404, etc.). Instead, a then() handler must check the Response.ok and/or Response.status properties.

On the other hand, the [Response.json](https://developer.mozilla.org/en-US/docs/Web/API/Response/json) method makes it pretty convenient to use when it comes to API calls.

### Callback hell

Both the server-side response and the client-side hydration depends on the `::loaded` event being called when the app is ready. In this example it's fired when all HTTP requests are finished, but that's arbitrary. The important thing is that it *must* be called somewhere, or the app will never start (Deno won't respond and nginx will throw a timeout).

### No document

There's no `document` on Deno, so anything that would touch it (such as setting the page title) has to check if it `exists?` or result in an error.

This is also the reason that Deno has to reference the `:compiled` JS: the dev build shadow-cljs generates has `document` references that make the server crash.
