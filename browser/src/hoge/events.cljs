(ns hoge.events
  (:require [re-frame.core :as rf]
            [reitit.frontend.controllers :as router]))

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
 (fn [{db :db} _]
   (js/console.log "dispatch http")
   {:db (assoc db :title "fetching...")
    :http {}}))

(rf/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match (:current-route db)
         controllers (router/apply-controllers
                      (:controllers old-match)
                      new-match)]
     (assoc db :current-route
            (assoc new-match :controllers controllers)))))

(rf/reg-event-db
 ::register-on-load
 (fn [db [_ f]]
   (js/console.log "register event" f)
   (assoc db :on-load f)))

(rf/reg-event-fx
 ::loaded
 (fn [{{:keys [on-load] :as db} :db} _]
   (js/console.log "loaded" (clj->js db))
   {:db (dissoc db :on-load)
    :loaded on-load}))
