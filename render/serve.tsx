const server = Deno.listen({ port: 3050 })
console.log(`HTTP webserver running. Access it at: http://localhost:3050/`)

import * as React from "https://esm.sh/react@17.0.2"
import * as ReactDOMServer from "https://esm.sh/react-dom@17.0.2/server"
import * as app from 'http://browser:8280/js/compiled.js'

function App({ title }) {
  const Elem = app.view
  const expandedTitle = title ? `${title} | HOGE` : 'HOGE'
  return (
    <html>
      <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>{expandedTitle}</title>
      </head>
      <body>
        <div id="app">
          <Elem/>
        </div>
      </body>
      <script src="http://localhost:8280/js/app.js"/>
    </html>
  )
}

async function serveHttp(conn: Deno.Conn) {
  const httpConn = Deno.serveHttp(conn);
  for await (const event of httpConn) {
    const start = window.performance.now()
    const req = event.request
    const path = new URL(req.url).pathname
    app.setup()
    app.registerOnLoad(({ status, title }) => {
      const body = ReactDOMServer.renderToString(<App title={title} />)
      event.respondWith(
        new Response(body, {
          headers: {
            'content-type': 'text/html; charset=UTF-8'
          },
          status,
        }),
      )
      console.log(`Completed ${status} ${req.method} ${path} in ${window.performance.now() - start}ms`)
    })
    app.navigate(path)
  }
}

for await (const conn of server) {
  serveHttp(conn)
}
