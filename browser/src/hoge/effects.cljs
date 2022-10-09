(ns hoge.effects
  (:require [re-frame.core :as rf]
            [hoge.events :as events]))

(rf/reg-fx
 :http
 (fn [_]
   (-> (js/fetch "http://localhost:8280")
       (.then #(rf/dispatch [::events/http-success %]))
       (.catch #(rf/dispatch [::events/http-error %])))))

(rf/reg-fx
 :loaded
 (fn [[f args]]
   (when f
     (apply f args))))

(rf/reg-fx
 :title
 (fn [title]
   (when (exists? js/document)
     (set! js/document.title (str title " | HOGE")))))
