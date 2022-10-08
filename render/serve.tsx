const server = Deno.listen({ port: 3050 });
console.log(`HTTP webserver running. Access it at: http://localhost:3050/`);

import * as React from "https://esm.sh/react@17.0.2";
import * as ReactDOMServer from "https://esm.sh/react-dom@17.0.2/server";
import * as app from 'http://browser:8280/js/compiled.js';

function App(_) {
  app.setup()
  const Elem = app.view
  return (
    <html>
      <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>HOGE</title>
      </head>
      <body>
        <Elem/>
      </body>
    </html>
  );
}

async function serveHttp(conn: Deno.Conn) {
  const httpConn = Deno.serveHttp(conn);
  for await (const requestEvent of httpConn) {
    const body = ReactDOMServer.renderToString(<App />);
    requestEvent.respondWith(
      new Response(body, {
        status: 200,
      }),
    );
  }
}

for await (const conn of server) {
  serveHttp(conn);
}
