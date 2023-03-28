(ns microservice.component.datasource
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as hikari]
            [microservice.component.param :as params]
            [taoensso.timbre :as timbre]))

(defn- credentials []
  {:username        (params/get-param :crm-db-user)
   :password        (params/get-param :crm-db-password)
   :database-name   (params/get-param :crm-db-name)
   :server-name     (params/get-param :crm-db-host)
   :port-number     (params/get-param :crm-db-port)})

(defn- additional-options []
  {:auto-commit        true
   :read-only          false
   :connection-timeout 30000
   :validation-timeout 5000
   :idle-timeout       600000
   :max-lifetime       1800000
   :minimum-idle       10
   :maximum-pool-size  10
   :pool-name          "db-pool"
   :adapter            "postgresql"
   :register-mbeans    false})

(defn- datasource-schema []
  (merge (additional-options) (credentials)))

(defrecord DataSource []
  component/Lifecycle

  (start [this]
    (timbre/info "Starting DataSource component")
    (let [datasource (hikari/make-datasource (datasource-schema))]
      (timbre/info "Started DataSource component")
      (assoc this :datasource        datasource
                  :datasource-schema (datasource-schema))))

  (stop [this]
    (timbre/info "Stopping DataSource component")
    (let [datasource (:datasource this)]
      (hikari/close-datasource datasource)
      (timbre/info "Stopped DataSource component")
      (dissoc this :datasource :datasource-schema))))

(defn new-datasource
  "Returns instance of DataSource component."
  []
  (map->DataSource {}))
