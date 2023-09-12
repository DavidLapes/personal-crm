(ns dev.repl.db
  (:require [clojure.java.jdbc :as jdbc]
            [crm.lib.db.filters :as db-filters]
            [dev.repl.system :refer [get-system-component]]
            [honey.sql :as sql-core]
            [honey.sql.helpers :as honey]
            [crm.service.user :as user])
  (:import (java.time LocalDateTime)))

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

(defn- select-users-time-created-filter []
  (-> (honey/select :*)
      (honey/from :users)
      (honey/where [:> :time_created (LocalDateTime/parse "2023-03-16T18:05:23.334578")])
      (exec-query)))

(defn- get-by-credentials []
  (user/get-by-credentials! (datasource) "admin@admin.com" "admin"))
