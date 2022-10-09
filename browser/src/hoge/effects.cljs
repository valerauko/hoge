(ns hoge.effects
  (:require [re-frame.core :as rf]
            [hoge.events :as events]))

(rf/reg-fx
 :http
 (fn [_]
   (-> (js/fetch "http://localhost:8280")
       (.then (fn [_]
                (rf/dispatch [::events/set-title "success"])
                (rf/dispatch [::events/loaded])))
       (.catch (fn [_]
                 (rf/dispatch [::events/set-title "failure"])
                 (rf/dispatch [::events/loaded]))))))

(rf/reg-fx
 :loaded
 (fn [f]
   (when f
     (f))))
