(ns dev.repl.db
  (:require [clojure.java.jdbc :as jdbc]
            [crm.lib.db.filters :as db-filters]
            [dev.user :refer [system]]
            [honey.sql :as sql-core]
            [honey.sql.helpers :as honey]))

(defn- get-system-component []
  @system)

(defn- datasource []
  (-> (get-system-component)
      :crm.component/datasource
      :datasource))

(defn- exec-query [query]
  (jdbc/with-db-connection [connection {:datasource (datasource)}]
    (println (sql-core/format query))
    (jdbc/query connection (sql-core/format query))))

(defn- exec [query]
  (jdbc/with-db-transaction [connection {:datasource (datasource)}]
    (println (sql-core/format query))
    (jdbc/execute! connection (sql-core/format query))))

(defn- select-users []
  (-> (honey/select :*)
      (honey/from :users)
      (exec-query)))

(defn- select-users-full-name-filter [filter]
  (-> (honey/select :*)
      (honey/from :users)
      (db-filters/add-full-name-filter filter)
      (exec-query)))
