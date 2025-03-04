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

;; Define pages
(defn navbar [request]
   (h/html
      [:section.hero.is-medium
       {:style {:background (format "center top/cover url('%s');" (link/file-path request "/images/fachada.png"))}}
       [:div.hero-head
        [:div.is-flex.is-justify-content-space-around
         [:div
          [:a.pl-3 {:href "https://www.facebook.com/profile.php?id=100063620186389"} [:i.fa-brands.fa-facebook]]
          [:a.pl-3 {:href "mailto:prepametropolitanatam@gmail.com"} [:i.fa-solid.fa-envelope]]]
         [:div.is-italic.has-text-weight-light
            [:i.fa-solid.fa-phone.has-text-link]
            [:span.pl-2.has-text-white "(833) 288 7233"]]]]
       [:div.hero-body
        [:div.container.has-text-centered
         [:h1.title.is-size-1.has-text-white
          "Preparatoria Metropolitana"]]]
       [:div.hero-foot
        [:nav.navbar.is-link
         {:role "navigation" 
          :aria-label "main navigation"}
         [:div.navbar-brand
          [:a.navbar-burger
           {:role "button" :aria-label "menu" :aria-expanded "false"}
           [:span {:aria-hidden "true"}]
           [:span {:aria-hidden "true"}]
           [:span {:aria-hidden "true"}]
           [:span {:aria-hidden "true"}]]]
         [:div.navbar-menu.is-justify-content-space-evenly.is-size-5
           [:a.navbar-item.has-text-white {:href "/"} "Inicio"]
           [:a.navbar-item.has-text-white {:href "/quienessomos.html"} "¿Quienes somos?"]
           [:a.navbar-item.has-text-white {:href "/niveles.html"} "Niveles"]
           [:a.navbar-item.has-text-white {:href "/instalaciones.html"} "Instalaciones"]
           [:a.navbar-item.has-text-white {:href "/ubicacion.html"} "Ubicación"]
           [:a.navbar-item.has-text-white {:href "/contacto.html"} "Contacto"]
           [:a.navbar-item.has-text-white {:href "/certificado.html"} "Certificado"]]]]]))


(defn layout-page [request page]
  (str
   (h/html
      (h/raw "<!DOCTYPE html>")
      [:html {:lang "es" :data-theme "light"}
       [:head
        [:meta {:charset "utf-8"}]
        [:meta {:name "viewport"
                :content "width=device-width, initial-scale=1.0"}]
        [:title "Preparatoria Metropolitana"]
        [:link {:rel "stylesheet" :href (link/file-path request "/styles/style.css")}]
        [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"}]
        [:script {:src (link/file-path request "/scripts/dist/bulma.js")}]]
       [:body
        [:div (navbar request)]
        [:div.container.px-6.my-6 
         [:div.box page]]]])))

    

(defn home-page [request]
  (->> (h/html
         [:p "Aquí estara un blog, se encuentra en construcción"])
    (layout-page request)))

(defn about-page [request]
  (->> (h/html
         [:section.section
          [:div.columns.is-vcentered.is-6
           [:div.column
            [:div
             [:h1.title.is-1 "Misión"]
             [:p.is-size-5 "Brindar una educación accesible y de calidad a los jóvenes de Tampico, Tamaulipas, fomentando el desarrollo integral de cada estudiante a través de un modelo académico innovador, incluyente y basado en valores. Nos comprometemos a formar ciudadanos críticos, responsables y preparados para enfrentar los retos del futuro, impulsando el crecimiento personal y profesional en un entorno de respeto, equidad y excelencia."]]]
           [:div.column.is-three-fifths
            [:img {:src (link/file-path request "/images/estudiantes1.png")}]]]]

         [:section.section
          [:div.columns.is-vcentered.is-6
           [:div.column
            [:div
             [:h1.title.is-1 "Visión"]
             [:p.is-size-5 "Ser la preparatoria de referencia en el centro de Tampico, reconocida por su accesibilidad, calidad educativa y compromiso con la comunidad. Aspiramos a formar generaciones de estudiantes con alto nivel académico, conciencia social y habilidades para transformar positivamente su entorno, mediante un modelo educativo dinámico, actualizado y centrado en el bienestar del estudiante."]]]
           [:div.column.is-three-fifths
            [:img {:src (link/file-path request "/images/estudiantes2.png")}]]]]
         [:section.section
          [:div.columns.is-vcentered.is-6
           [:div.column
            [:div.content
             [:h1.title.is-1 "Valores"]
             [:dl.is-size-5
              [:dt.has-text-weight-bold "Excelencia"]
              [:dd "Buscamos siempre la mejora continua en la enseñanza y el aprendizaje."]
              [:dt.has-text-weight-bold "Inclusión"]
              [:dd "Garantizamos un espacio educativo accesible para todos, sin distinción."]
              [:dt.has-text-weight-bold "Compromiso"]
              [:dd "Fomentamos la responsabilidad y el esfuerzo como base del éxito."]
              [:dt.has-text-weight-bold "Respeto"]
              [:dd "Promovemos la convivencia armoniosa y el reconocimiento de la diversidad."]
              [:dt.has-text-weight-bold "Innovación"]
              [:dd "Aplicamos nuevas metodologías y herramientas para un aprendizaje efectivo."]
              [:dt.has-text-weight-bold "Solidaridad"]
              [:dd "Impulsamos el apoyo mutuo y el servicio a la comunidad como pilares fundamentales."]]]]
           [:div.column.is-three-fifths
            [:img {:src (link/file-path request "/images/estudiantes3.png")}]]]])
    (layout-page request)))





(defn levels-page [request]
  (->> (h/html
         [:div "HOLA A TODOS"])
    (layout-page request)))

(defn building-page [request]
  (->> (h/html
         [:div "HOLA A TODOS"])
    (layout-page request)))

(defn location-page [request]
  (->> (h/html
         [:div "HOLA A TODOS"])
    (layout-page request)))

(defn contact-page [request]
  (->> (h/html
         [:div.notification.is-success.is-hidden
          [:button.delete]
          "Gracias por ponerte en contacto con nosotros, te responderemos tan pronto nos sea posible."]
         [:h1.title "Contactanos"]
         [:form.static-form
           [:div.field
            [:label.label "Correo"]
            [:div.control.has-icons-left
              [:input.input {:type "text" :placeholder "correo@mail.com" :name "email"}]
              [:span.icon.is-small.is-left
               [:i.fas.fa-envelope]]]]
           [:div.field
            [:label:label "Nombre"]
            [:div.control
             [:input.input {:type "text" :placeholder "bla" :name "algo"}]]]])

    (layout-page request)))

(defn certificates-page [request]
  (->> (h/html
         [:h1.title "Verifica la validez de tu cerficado"]
         [:p "En esta pagina puede validar los certificados generados por la preparatoria Metropolitana, para esto, ingresa el folio en el certificado en el siguiente campo de texto:"]
         [:form.form
          [:div.field.has-addons.has-addons-centered
           [:div.control
             [:input#input.input {:type "text" :placeholder "Folio de tu certificado:"}]]
           [:div.control
             [:button#search.button "Buscar"]]]]
         [:p [:strong "Nota: "] "Si se acaba de generar tu certificado, puede tardar un tiempo máximo de 3 días para ser reflejado en el sistema."]
         [:script (h/raw (slurp "src/certificate.js"))])
    (layout-page request)))

;; Blogs
(defn markdown-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "/") (keys pages))
          (map #(fn [req] (layout-page req (md/md-to-html-string %)))
               (vals pages))))

;; Serve pages

(defn get-pages []
  (merge (stasis/merge-page-sources 
           {:public (stasis/slurp-directory "resources/public" #".*\.(html|css|js|txt|map)$")
            :blog (markdown-pages (stasis/slurp-directory "resources/posts" #".*\.md$"))})
         {"/" home-page}
         {"/quienessomos.html" about-page}
         {"/niveles.html" levels-page}
         {"/instalaciones.html" building-page}
         {"/ubicacion.html" location-page}
         {"/contacto.html" contact-page}
         {"/certificado.html" certificates-page}))


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
