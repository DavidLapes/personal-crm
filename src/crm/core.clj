(ns crm.core
  (:gen-class)
  (:require [com.stuartsierra.component :refer [start]]
            [microservice.system :as system]
            [taoensso.timbre :as timbre]))

(defn -main
  "Starts CRM application."
  [& args]
  (timbre/info "Starting CRM application")
  (start (system/make-system))
  (timbre/info "Started CRM application")
  :ok)
