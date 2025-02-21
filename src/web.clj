(ns prepametro.web
  (:require [stasis.core :as stasis]
            [clojure.java.io :as io]
            [hiccup2.core :as h]
            [optimus.link :as link] ; New
            [optimus.prime :as optimus]
            [optimus.assets :as assets]
            [optimus.optimizations :as optimizations]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [optimus.strategies :refer [serve-live-assets]]
            [ring.adapter.jetty :as jt]
            [markdown.core :as md]
            [clojure.string :as str]
            [optimus.export]
            [sail.core :as sail]))

;; Get assets

(defn get-assets []
  (assets/load-assets "public" [#".*"]))
  
(comment
 (format "background-image: url('%s');" "hello" 
  (get-assets)))


;; Define pages
(defn nav-bar [request]
   (h/html
      [:div.relative.font-sans.before:absolute.before:w-full.before:h-full.before:inset-0.before:bg-black.before:opacity-50.before:z-10.h-70
       [:img
        {:src "https://readymadeui.com/cardImg.webp",
         :alt "Banner Image",
         :class "absolute inset-0 w-full h-full object-cover"}]
       [:div
        {:class
         "relative z-50 h-full max-w-6xl mx-auto flex flex-col justify-center items-center text-center text-white p-6"}
        [:h2
         {:class "sm:text-4xl text-2xl font-bold mb-6"}
         "Preparatoria Metropolitano"]
        [:div.flex.justify-around.w-200.mt-12

         [:a {:href "first_post"}
          [:button.bg-transparent.text-white.text-base.py-3.px-6.border.border-white.rounded-lg.hover:bg-white.hover:text-black.transition.duration-300
           {:type "button"}
           "Book Now"]]
         [:a
            [:button.bg-transparent.text-white.text-base.py-3.px-6.border.border-white.rounded-lg.hover:bg-white.hover:text-black.transition.duration-300
             {:type "button"}
             "Book Now"]]
         [:a
            [:button.bg-transparent.text-white.text-base.py-3.px-6.border.border-white.rounded-lg.hover:bg-white.hover:text-black.transition.duration-300
             {:type "button"}
             "Book Now"]]
         [:a
            [:button.bg-transparent.text-white.text-base.py-3.px-6.border.border-white.rounded-lg.hover:bg-white.hover:text-black.transition.duration-300
             {:type "button"}
             "Book Now"]]
         [:a
            [:button.bg-transparent.text-white.text-base.py-3.px-6.border.border-white.rounded-lg.hover:bg-white.hover:text-black.transition.duration-300
             {:type "button"}
             "Book Now"]]]]]))

(defn layout-page [request page]
  (str
   (h/html
       [:head
        [:meta {:charset "utf-8"}]
        [:meta {:name "viewport"
                :content "width=device-width, initial-scale=1.0"}]
        [:title "Preparatoria Metropolitano"]
        [:link {:rel "stylesheet" :href (link/file-path request "/styles/style.css")}]
        [:script {:src "https://unpkg.com/@tailwindcss/browser@4"}]]
       [:body
        [:div.nav (nav-bar request)]
        [:div.body page]])))

(defn home-page [request]
  (->> (h/html
         (take 10 (repeatedly #(identity [:p.text-slate-500 "HOLA A TODOS BYE BYE"]))))
    (layout-page request)))

(defn about-page [request]
  (->> (h/html
         [:div "HOLA A TODOS"])
    (layout-page request)))

;; Blogs
(defn markdown-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "/") (keys pages))
          (map #(fn [req] (layout-page req (md/md-to-html-string %)))
               (vals pages))))
(comment 
  (sail/watch "./resources/public/styles/style.css" {:paths ["./src"]})
  (sail/build "./resources/public/styles/style.css" {:paths ["./src"]}))

;; Serve pages

(defn get-pages []
  (merge (stasis/merge-page-sources 
           {:public (stasis/slurp-directory "resources/public" #".*\.(html|css|js)$")
            :blog (markdown-pages (stasis/slurp-directory "resources/posts" #".*\.md$"))})
         {"/" home-page}
         {"/about/" about-page}))


; (def app (stasis/serve-pages get-pages))
    
(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimizations/all serve-live-assets)
             wrap-content-type))


(comment 
  (about-page nil)
  (get-pages)
  (println (handler nil))
  (println (app {:uri "/first_post"}))
  (jt/run-jetty app {:port 3000
                     :join? false}))


(str (h/html [:span.text-red-900 "bar"]))

;; Export pages
(def target-dir "./target")
(defn export []
  (let [assets (optimizations/all (get-assets) {})
          pages (get-pages)]
      (stasis/empty-directory! target-dir)
    (optimus.export/save-assets assets target-dir)
    (stasis/export-pages pages target-dir {:optimus-assets assets})))
(comment
  (export))
