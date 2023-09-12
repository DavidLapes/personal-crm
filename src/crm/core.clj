(ns crm.core
  (:gen-class)
  (:require [com.stuartsierra.component :refer [start]]
            [microservice.system :as system]
            [taoensso.timbre :as timbre]))

(defn -main
  "Starts CRM application."
  [& args]
  (let [profile (keyword (System/getProperty "Profile"))]
    (timbre/info "Starting CRM application")
    (start (system/make-system profile))
    (timbre/info "Started CRM application")
    :ok))
