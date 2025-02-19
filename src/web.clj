(ns prepametro.web
  (:require [stasis.core :as stasis]
            [clojure.java.io :as io]
            [hiccup2.core :as h]
            [optimus.prime :as optimus]
            [optimus.assets :as assets]
            [optimus.optimizations :as optimizations]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.adapter.jetty :as jt]
            [markdown.core :as md]
            [sail.core :as sail]))

;; Get assets

(defn get-assets []
  (assets/load-assets "resources/public/" [#"/images/.*\.png"]))
  
(comment
  
  (get-assets))

;; Define pages
(defn nav-bar []
   (h/html
     [:nav.bg-cover.bg-center.h-1-6 {:style "background-image: url('logo.png');"}
      [:div.container.mx-auto.h-full..bg-centerflex.flex-col.justify-center.items-center
       [:h1.text-red.text-3xl.font-bold.mb-4 "Preparatoria Metropolitano"]
       [:ul.flex.space-x-6
        [:li
         [:a.text-red.hover:underline {:href "https://www.google.com"} "Inicio"]]
        [:li
         [:a.text-red.hover:underline {:href "https://www.google.com"} "Home"]]
        [:li
         [:a.text-red.hover:underline {:href "https://www.google.com"} "Home"]]
        [:li
         [:a.text-red.hover:underline {:href "https://www.google.com"} "Home"]]]]]))

(defn layout-page [page]
  (str
   (h/html
       [:head
        [:meta {:charset "utf-8"}]
        [:meta {:name "viewport"
                :content "width=device-width, initial-scale=1.0"}]
        [:title "Preparatoria Metropolitano"]
        [:link {:rel "stylesheet" :href "./styles/style.css"}]]
       [:body
        [:div.nav (nav-bar)]
        [:div.body page]])))

(defn about-page [request]
  (layout-page (slurp (io/resource "partials/about.html"))))

(comment 
  (sail/watch "./resources/public/styles/style.css" {:paths ["./src"]})
  (sail/build "./resources/public/styles/style.css" {:paths ["./src"]}))

;; Serve pages

(defn get-pages []
  (merge (stasis/slurp-directory "resources/public" #".*\.(html|css|js)$")
         {"/" about-page}))
    
(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimizations/all serve-live-assets)
             wrap-content-type))


(comment 
  (about-page nil)
  (get-pages)
  (println (handler nil))
  (println (app {:uri "/"}))
  (jt/run-jetty app {:port 3000
                     :join? false}))


(str (h/html [:span.text-red-900 "bar"]))

;; Export pages
(def target-dir "./target")
(defn export []
  (stasis/empty-directory! target-dir)
  (stasis/export-pages (get-pages) target-dir))
(comment
  (export))
