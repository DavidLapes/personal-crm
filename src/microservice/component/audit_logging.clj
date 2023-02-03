(ns microservice.component.audit-logging
  (:require [clojure.java.jdbc :as jdbc]
            [com.stuartsierra.component :as component]
            [microservice.component.proto.audit :refer [AuditLogger]]
            [taoensso.timbre :as timbre])
  (:import (java.time LocalDateTime)))

(defrecord AuditLoggerImpl [datasource]
  AuditLogger
  (-persist [_ {:keys [event payload] :as data}]
    (let [timestamp (-> (LocalDateTime/now) (.toString))
          datasource (-> datasource :datasource)]
      (timbre/info (str "Audit log persisting - {" timestamp "} data " data))
      (jdbc/with-db-transaction [connection {:datasource datasource}]
        (jdbc/insert! connection :audit_log {:event event
                                             :payload payload}))))

  component/Lifecycle
  (start [this]
    (timbre/info "Starting AuditLoggerImpl component")
    (timbre/info "Started AuditLoggerImpl component")
    this)

  (stop [this]
    (timbre/info "Stopping AuditLoggerImpl component")
    (timbre/info "Stopped AuditLoggerImpl component")
    this))

(defn new-audit-logger-impl
  "Returns instance of AuditLoggerImpl component."
  [datasource-ref]
  (component/using
    (map->AuditLoggerImpl {})
    {:datasource datasource-ref}))
