(ns microservices.component.test.db_initializer
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [crm.lib.db.utils :refer [execute-sql-file]]
            [taoensso.timbre :as timbre]))

(defrecord TestDbInitializer [datasource _]
  component/Lifecycle

  (start [this]
    (let [datasource (:datasource datasource)]
      (jdbc/with-db-connection [connection {:datasource datasource}]
        (try
          (timbre/info "Preparing for test data initialization")
          (timbre/info "Data initialization in progress...")
          (timbre/info "Dropping test data")
          (execute-sql-file connection "scripts/postgresql/test/drop_test_data.sql")
          (timbre/info "Creating views")
          (execute-sql-file connection "scripts/postgresql/test/create_test_data.sql")
          (timbre/info "Test data successfully initialized")
          (catch Exception ex
            (timbre/error "DB initialization has failed" ex))))
      (assoc this :db-initializer nil)))

  (stop [this]
    (dissoc this :db-initializer)))

(defn new-db-initializer
  "Returns instance of TestDbInitializer component."
  [datasource-ref migration-ref]
  (component/using
    (map->TestDbInitializer {})
    {:datasource datasource-ref
     :migration  migration-ref}))
