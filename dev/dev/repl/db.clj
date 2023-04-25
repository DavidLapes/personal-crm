(ns dev.repl.db
  (:require [clojure.java.jdbc :as jdbc]
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

(defn- select-users []
  (-> (honey/select :*)
      (honey/from :users)
      (exec-query)))

(defn- select-users-concat []
  (-> (honey/select :*)
      (honey/from :users)
      (honey/where [:like [:concat :email :last_name] "%admin%"])
      (exec-query)))
