(ns hoge.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [hoge.effects]
            [hoge.events :as events]
            [hoge.views :as views]))

(defn ^:export view
  [& _]
  (js/console.log "view" "called")
  (reagent/as-element [views/main]))
 
(defn ^:dev/after-load mount-root
  []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main] root-el)))

(defn ^:export setup
  []
  (re-frame/clear-subscription-cache!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch [::events/fetch-example]))

(defn ^:export mount
  []
  (setup)
  (mount-root))
