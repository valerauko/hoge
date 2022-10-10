(ns hoge.dev
  (:require [clojure.java.io :as io]
            [shadow.cljs.devtools.api :as shadow]
            [shadow.cljs.devtools.server.fs-watch :as fs-watch]))

(defn watch-compile-for-ssr
  {:shadow/requires-server true}
  []
  (shadow/compile :compiled)
  (fs-watch/start
   {}
   [(io/file "src" "hoge")]
   ["cljs"]
   (fn [_]
     (shadow/compile :compiled))))

(defn watch
  {:shadow/requires-server true}
  [& _]
  (watch-compile-for-ssr)
  (shadow/watch :app))
