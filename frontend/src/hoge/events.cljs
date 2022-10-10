(ns hoge.events
  (:require [re-frame.core :as rf]
            [reitit.frontend.controllers :as router]))

(rf/reg-event-fx
 ::initialize-db
 (fn [_ _]
   {:db {:title "hello, world"}}))

(rf/reg-event-fx
 ::set-title
 (fn [{db :db} [_ title]]
   {:db (assoc db :title title)
    :title title}))

(rf/reg-event-fx
 ::http
 (fn [{{{n :in-flight} :app-state :as db} :db :or {n 0}} _]
   {:db (assoc-in db [:app-state :in-flight] (inc n))
    :dispatch [::set-title "fetching..."]
    :http {}}))

(rf/reg-event-fx
 ::http-success
 (fn [_ _]
   (let [title "success"]
     {:dispatch-n [[::set-title title]
                   [::http-finish]]})))

(rf/reg-event-fx
 ::http-error
 (fn [{db :db} _]
   (let [title "error"]
     {:db (assoc-in db [:app-state :error?] true)
      :dispatch-n [[::set-title title]
                   [::http-finish]]})))

(rf/reg-event-fx
 ::http-finish
 (fn [{{{:keys [in-flight error?]} :app-state title :title :as db} :db
       :or {in-flight 1}} _]
   (let [new-in-flight (dec in-flight)]
     {:db (assoc-in db [:app-state :in-flight] new-in-flight)
      :dispatch (when (< new-in-flight 1)
                  [::loaded (clj->js {:title title
                                      :status (if error? 404 200)})])})))

(rf/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match (:current-route db)
         controllers (router/apply-controllers
                      (:controllers old-match)
                      new-match)]
     (assoc db :current-route
            (assoc new-match :controllers controllers)))))

(rf/reg-event-fx
 ::not-found
 (fn [{db :db} _]
   (when-let [old-match (:current-route db)]
     (doseq [controller (reverse (:controllers old-match))]
       (router/apply-controller controller :stop)))
   {:db (assoc db :current-route nil)
    :dispatch [::loaded (clj->js {:status 404})]}))

(rf/reg-event-db
 ::register-on-load
 (fn [db [_ f]]
   (assoc db :on-load f)))

(rf/reg-event-fx
 ::loaded
 (fn [{{:keys [on-load] :as db} :db} [_ & args]]
   {:db (dissoc db :on-load)
    :loaded [on-load args]}))
