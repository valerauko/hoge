(ns hoge.effects
  (:require [re-frame.core :as rf]
            [hoge.events :as events]))

(rf/reg-fx
 :http
 (fn [_]
   (js/console.log "http" "start")
   (-> (js/fetch "https://example.com")
       (.then (fn [_]
                (js/console.log "http" "success")
                (rf/dispatch [::events/set-title "success"])))
       (.catch (fn [_]
                 (js/console.log "http" "failure")
                 (rf/dispatch [::events/set-title "failure"]))))))
