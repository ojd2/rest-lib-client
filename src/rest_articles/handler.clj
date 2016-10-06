;; Set up some namespaces for the project:
(ns rest-articles.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [liberator.core :refer [resource defresource]]
            [liberator.representation :refer [render-map-generic]]
            [hiccup.core :refer [html]]
            [clojure.data :as json]
            [compojure.core :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;; Set up some default media types for
;; our API to accept, we need media types for
;; writing out requests and internal data:
(def default-media-types
  ["application/json"
   "text/plain"
   "text/html"])

;; Using a Liberator multimethod function, define a function that will
;; 'dispatch on media type' a simple 'conj' method to cojoin our data
;; and the chosen media type as 'context':
(defmulti render-map-generic "dispatch on media type"
  (fn [data context] (get-in context [:representation :media-type])))

;; Define one method for writing 'json' media type:
(defmethod render-map-generic "application/json" [data context]
  ["json/write" (conj (:links data (:properties data)))])

;; Define one method for writing as a 'hmtl' media type:
(defmethod render-map-generic "application/html" [data context]
  (html [:div
         [:h1 (-> data :class first)]
         [:dl
          (mapcat (fn [[key value]] [[:dt key] [:dd value]])
                  (:properties data))]]))

;; Define a handler to create our data object for an article:
(defrecord Article [id headline full channel status])





(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
