(ns hoge.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 ::title
 :title)

(rf/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))
