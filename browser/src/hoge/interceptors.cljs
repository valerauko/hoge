(ns hoge.interceptors
  (:require [re-frame.core :as rf]))

(def debug
  (rf/->interceptor
   {:id ::debug
    :before (fn [{{event :event} :coeffects :as ctx}]
              (js/console.log (js/window.performance.now) (clj->js event))
              ctx)}))

(rf/reg-global-interceptor debug)
