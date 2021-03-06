(defproject rest-articles "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/ojd2/rest-lib-client/"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [liberator "0.14.1"]
                 [org.clojure/data "0.2.6"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler rest-articles.handler/app
         :auto-reload? true
         :auto-refresh? true}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
