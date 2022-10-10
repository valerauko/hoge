const server = Deno.listen({ port: 3000 })
console.log(`Server-side rendering running. Access it at: http://hoge.lvh.me:3000/`)

import * as React from "https://esm.sh/react@17.0.2"
import * as ReactDOMServer from "https://esm.sh/react-dom@17.0.2/server"
import * as app from 'http://hoge.lvh.me:3000/js/compiled.js'

async function serveHttp(conn: Deno.Conn) {
  const httpConn = Deno.serveHttp(conn);
  for await (const event of httpConn) {
    const start = window.performance.now()
    const req = event.request
    const path = new URL(req.url).pathname
    app.setup()
    app.registerOnLoad(({ status, title }) => {
      const expandedTitle = title ? `${title} | HOGE` : 'HOGE'
      const App = app.view
      const body = ReactDOMServer.renderToString(<App />)
      // not using renderToString for the rest of the html
      // because it'd put the data-reactroot on the <html>
      // instead of the actual root of the app, resulting
      // in hydration errors
      // also, tsx-style inline html breaks with the css
      // import
      const html = `
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="icon" type="image/x-icon" href="/favicon.ico"/>
    <style>
      @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+JP:wght@400;700&display=swap')
    </style>
    <title>${expandedTitle}</title>
  </head>
  <body>
    <div id="app">${body}</div>
  </body>
  <script src="/js/app.js"></script>
</html>
`
      event.respondWith(
        new Response(html, {
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
