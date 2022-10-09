(ns hoge.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [hoge.effects]
            [hoge.interceptors]
            [hoge.events :as events]
            [hoge.routes :as routes]
            [hoge.views :as views]))

(defn ^:export view
  [& _]
  (reagent/as-element [views/main]))

(defn ^:export register-on-load
  [callback]
  (re-frame/dispatch [::events/register-on-load callback]))

(defn ^:dev/after-load mount-root
  []
  (routes/start-router)
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main] root-el)))

(defn ^:export setup
  []
  (re-frame/clear-subscription-cache!)
  (re-frame/dispatch-sync [::events/initialize-db]))

(defn ^:export mount
  []
  (setup)
  (mount-root))
