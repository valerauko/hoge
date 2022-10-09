(ns hoge.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::title
 (fn [db]
   (:title db)))

(rf/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))

(rf/reg-sub
 ::http-in-flight
 (fn [db]
   (get-in db [:app-state :in-flight] 0)))
