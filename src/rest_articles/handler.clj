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

;; Define some mock article structs to play with:
(def uncertainty
  (-> Article "1" "Logical Uncertainty" "If you’re anything like me,
      you have some uncertainty about the answer to this question..."
      "Mathematics" "true"))

(def hott
  (-> Article "2" "HoTT" "HoTT provides  a novel notion –
      as well as an example – of theory, which does not
      reduce to a class of propositions but has a further higher..."
      "HoTT" "false"))

(def tuq
  (-> Article "3" "The Ultimate Question" "He has has an interesting story
      that claims to tell you the Ultimate Question, and its Answer..."
      "TUQ" "true"))

(def homology
  (-> Article "4" "I’m excited that over on this thread,
      Mike Shulman has proposed a very plausible theory of magnitude homology...."
      "Mathematics" "false"))

;; Create data struct to hold our record data structs:
(def articles [uncertaity hott tuq homology])

;; Index our data struct using 'defn' so we can execute time after time:
(defn index-data [ctx] articles)

;; Define our Liberator resource for overall infrastructure.
;; We mention allowed REST methods, allowed media-types which point
;; to our previous struct above and when the handle for a request is
;; accepted, 'status code: 200' we return default liberator index-data.
;; First this is done for root index:
(defresource index
  :allowed-methods [:options :get :post :delete]
  :available-media-types default-media-types
  :handle-ok index-data)

;; Define a Liberator resource method for an article via GET:
(defresource article [id]
  :allowed-methods [:options :get]
  :available-media-types ["text/html" "application/json"]
  :handle-ok index-data (fn [_]
                          {:properties
                           (nth articles (Integer/parseInt id))
                           :links [{:rel ["self"]
                                    :href (str "/articles/" id)}
                                   {:rel ["listing"]
                                    :href "/articles"}]}))


;; Research how to do a DELETE method via liberator docs.
;; May have to add :post handle to the above allowed methods.^
;; May not need an additional defresource









(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
