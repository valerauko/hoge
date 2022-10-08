const server = Deno.listen({ port: 3050 });
console.log(`HTTP webserver running. Access it at: http://localhost:3050/`);

import * as React from "https://esm.sh/react@17.0.2";
import * as ReactDOMServer from "https://esm.sh/react-dom@17.0.2/server";
import * as app from 'http://browser:8280/js/compiled.js';

app.setup()

function App(_) {
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
  for await (const req of httpConn) {
    app.navigate("/a")
    setTimeout(() => {
      const body = ReactDOMServer.renderToString(<App />);
      req.respondWith(
        new Response(body, {
          status: 200,
        }),
      );
    }, 50);
  }
}

for await (const conn of server) {
  serveHttp(conn);
}
