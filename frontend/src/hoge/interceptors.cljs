(ns hoge.interceptors
  (:require [re-frame.core :as rf]
            [hoge.config :as config]))

(def debug
  (rf/->interceptor
   {:id ::debug
    :before (fn [{{event :event} :coeffects :as ctx}]
              (js/console.log (js/window.performance.now) (clj->js event))
              ctx)}))

(when config/debug?
  (rf/reg-global-interceptor debug))
