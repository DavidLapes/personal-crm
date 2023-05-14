(ns crm.lib.db.utils
  (:refer-clojure :exclude [update set])
  (:require [crm.lib.db.order :as lib-order]
            [crm.lib.db.pagination :as lib-pagination]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io]
            [honey.sql :as sql-core]
            [honey.sql.helpers :as honey]
            [taoensso.timbre :as timbre]))

(defn- insertable-cartesian-product
  "Returns vector of maps for multi-row insert into relation tables

  c1-name       -     name of a column, must be a keyword
  c2-vals       -     vector collection of values
  c2-name       -     name of a column, must be a keyword
  c2-vals       -     vector collection of values."
  [c1-name c1-vals c2-name c2-vals]
  (->> (mapcat
         (fn [c1-val]
           (map
             (fn [c2-val]
               {(keyword c1-name) c1-val (keyword c2-name) c2-val})
             c2-vals))
         c1-vals)
       (into [])))

(defn execute-sql-file
  "Executes SQL file."
  [connection file]
  (timbre/info (str "Executing SQL file - " file))
  (jdbc/db-do-prepared connection (slurp (io/resource file))))

(defn- exec-query!
  "Executes HoneySQL query using HikariCP connection. Don't use this for select! This is only for
   INSERT, UPDATE and DELETE. This is executed inside transaction."
  [connection query]
  (jdbc/execute! connection (sql-core/format query)))

(defn query!
  "Executes HoneySQL query using HikariCP connection. Don't use this for insert, update and delete! This is only for
   SELECT query. This is executed inside transaction."
  [connection query]
  (jdbc/query connection (sql-core/format query)))

(defn- common-filter-count-query!
  "Returns integer result of count query with all common non-nil filters applied."
  [connection table where-clauses-query]
  (let [query (-> where-clauses-query
                  (honey/select :%count.*)
                  (honey/from table))
        result (query! connection query)]
    (if (seq? result)
      (:count (first result))
      (:count result))))

(defn- common-filter-query!
  "Returns result of query with all common non-nil filters applied."
  ([connection table filters]
   (common-filter-query! connection table filters {}))
  ([connection table filters where-clauses-query]
   (let [query (-> where-clauses-query
                   (honey/select :*)
                   (honey/from table))]
     (->> (cond-> query
                  (some? (:limit filters)) (lib-pagination/apply-paging-query filters)
                  (some? (:order_column filters)) (lib-order/apply-order-filter filters))
          (query! connection)))))

(defn update-by-id!
  "Executes insert query. Table must be passed as a keyword and data must be a map."
  [connection table id data]
  (jdbc/update! connection table data ["id = ?" id] {:return-keys true}))

;;TODO: Not related to DB utils but think about upgrading dependencies :)
(defn update!
  "Executes insert query. Table must be passed as a keyword and data must be a map."
  [connection table data where-clauses]
  (exec-query! connection
               (-> where-clauses
                   (honey/update table)
                   (honey/set data))))

(defn insert!
  "Executes insert query. Table must be passed as a keyword and data must be a map.
   Returns updated row after successful insert."
  [connection table data]
  (let [new-row (jdbc/insert! connection table data)]
    (if (seq? new-row)
      (first new-row)
      new-row)))

(defn insert-into-relation-table!
  "Executes insert query for relation table between two entities.

  connection    -     HikariCP datasource
  table-name    -     keyword, name of the table
  c1-name       -     name of a column, must be a keyword
  c2-vals       -     single value or collection of values
  c2-name       -     name of a column, must be a keyword
  c2-vals       -     single value or collection of values."
  [connection table-name c1-name c1-vals c2-name c2-vals]
  (let [c1-vals (if (coll? c1-vals)
                  (into [] c1-vals)
                  [c1-vals])
        c2-vals (if (coll? c2-vals)
                  (into [] c2-vals)
                  [c2-vals])
        insert-data (insertable-cartesian-product c1-name c1-vals c2-name c2-vals)]
    (->> (-> (honey/insert-into table-name)
             (honey/values insert-data)
             (honey/on-conflict c1-name c2-name)
             (honey/do-nothing))
         (exec-query! connection))))

(defn delete!
  "Performs DELETE on given table using filters map."
  [connection table where-clauses]
  (exec-query! connection (honey/delete-from where-clauses table)))

(defn get-by-id!
  "Returns single row by PK value, defaulted to :id PK column by provided ID value.
   If no row is found, returns nil."
  ([connection table id]
   (jdbc/get-by-id connection table id))
  ([connection table id pk-name]
   (jdbc/get-by-id connection table id pk-name)))

(defn get-one!
  "Returns one row for given table with possible filters.
   If no row is found, returns nil."
  [connection table where-clauses]
  (-> (common-filter-query! connection table nil where-clauses)
      first))

(defn get-all!
  "Returns rows for given table with possible filters."
  ([connection table filters]
   (get-all! connection table filters {}))
  ([connection table filters where-clauses-query]
   {:data (common-filter-query! connection table filters where-clauses-query)
    :count (common-filter-count-query! connection table where-clauses-query)}))