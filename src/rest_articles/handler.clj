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
;; our API to accept:
(def default-media-types
  ["application/json"
   "text/plain"
   "text/html"])

;; Liberator default multimethod set to switch upon media type.
;; 'data' signifies a data map and 'context' signifies a cojoined key/pair value of 'data':
(defmulti render-map-generic "dispatch on media type"
  (fn [data context] (get-in context [:representation :media-type]))) 

;; Now define a method for our 'render-map-generic' method above.
;; Implement a method to extract 'data' key / value pairs and cojoin:
(defmethod render-map-generic "application/json" [data context]
  ["json/write" (conj (:links data (:properties data)))])






(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
