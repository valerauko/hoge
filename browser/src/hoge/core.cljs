(ns hoge.core
  (:require ["react-dom" :as react-dom]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
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
  (rf/dispatch [::events/register-on-load callback]))

(defn ^:export setup
  []
  (rf/clear-subscription-cache!)
  (rf/dispatch-sync [::events/initialize-db]))

(defn ^:export hydrate
  []
  (js/console.log "Hydrating...")
  (setup)
  (routes/start-router)
  (let [root (js/document.getElementById "app")]
    (react-dom/hydrate (view) root)))
