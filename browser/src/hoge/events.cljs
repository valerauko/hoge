(ns hoge.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-fx
 ::initialize-db
 (fn [_ _]
   (js/console.log "init db")
   {:db {:title "hello, world"}}))

(rf/reg-event-db
 ::set-title
 (fn [db [_ new-title]]
   (assoc db :title new-title)))

(rf/reg-event-fx
 ::fetch-example
 (fn [_ _]
   (js/console.log "dispatch http")
   {:db {:title "fetching..."}
    :http {}}))
