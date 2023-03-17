(ns microservice.component.migration
  (:require [clojure.java.jdbc :as jdbc]
            [com.stuartsierra.component :as component]
            [migratus.core :as migratus]
            [taoensso.timbre :as timbre]
            [crm.lib.db.utils :refer [execute-sql-file]]))

(defn- datasource-schema->migration-schema
  [datasource-schema]
  {:store         :database
   :migration-dir "migrations"
   :db            {:db (:database-name datasource-schema)
                   :host (:server-name datasource-schema)
                   :port (:port-number datasource-schema)
                   :subname (str "//" (:server-name datasource-schema) ":" (:port-number datasource-schema) "/" (:database-name datasource-schema))
                   :subprotocol (:adapter datasource-schema)
                   :user (:username datasource-schema)
                   :password (:password datasource-schema)}})

(defrecord Migration [datasource]
  component/Lifecycle

  (start [this]
    (let [datasource-schema (:datasource-schema datasource)
          datasource (:datasource datasource)
          migration-schema (datasource-schema->migration-schema datasource-schema)]
      (jdbc/with-db-connection [connection {:datasource datasource}]
        (try
          (timbre/info "Checking migrations")
          (migratus/migrate migration-schema)
          (timbre/info "Finished migration")
          (timbre/info "Dropping views")
          (execute-sql-file connection "scripts/postgresql/drop_views.sql")
          (timbre/info "Creating views")
          (execute-sql-file connection "scripts/postgresql/create_views.sql")
          #_(timbre/info "Dropping indexes")
          #_(execute-sql-file connection "scripts/postgresql/drop_indexes.sql")
          #_(timbre/info "Creating indexes")
          #_(execute-sql-file connection "scripts/postgresql/create_indexes.sql")
          (timbre/info "Analyzing database")
          (execute-sql-file connection "scripts/postgresql/analyze.sql")
          (catch Exception ex
            (timbre/error "DB migration has failed" ex))))
      (assoc this :migration-schema migration-schema)))

  (stop [this]
    (dissoc this :migration-schema)))

(defn new-migration
  "Returns instance of Migration component."
  [datasource-ref]
  (component/using
    (map->Migration {})
    {:datasource datasource-ref}))
