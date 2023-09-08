(ns crm.lib.db.filters
  (:require [honey.sql.helpers :as honey])
  (:import (java.util Date)))

(defn add-ilike-filter
  "Adds simple LIKE where-clause to HoneySQL query."
  [query column-name value]
  (honey/where query [:like column-name [:concat "%" value "%"]]))

(defn add-in-filter
  "Adds simple IN where-clause to HoneySQL query."
  [query column-name value]
  (let [value (if (coll? value)
                value
                [value])]
    (honey/where query [:in column-name value])))

(defn add-is-filter
  "Adds simple IS where-clause to HoneySQL query."
  [query column-name value]
  (honey/where query [:is column-name value]))

(defn add-is-not-filter
  "Adds simple IS where-clause to HoneySQL query."
  [query column-name value]
  (honey/where query [:is-not column-name value]))

(defn add-equals-time-filter
  "Adds simple filter for TIMESTAMP column to HoneySQL query."
  [query column-name ^Date ldt]
  (honey/where query [:= column-name ldt]))

(defn add-lower-than-time-filter
  "Adds simple filter for TIMESTAMP with > operator to HoneySQL query."
  [query column-name ^Date ldt]
  (honey/where query [:> column-name ldt]))

(defn add-higher-than-time-filter
  "Adds simple filter for TIMESTAMP with < operator t HoneySQL query."
  [query column-name ^Date ldt]
  (honey/where query [:< column-name ldt]))

(defn add-full-name-filter
  "Adds full-name filter to HoneySQL query."
  ([query full-name-filter]
   (add-full-name-filter query full-name-filter {:first-name-column  :first_name
                                                 :middle-name-column :middle_name
                                                 :last-name-column   :last_name}))
  ([query full-name-filter {:keys [first-name-column middle-name-column last-name-column]}] +
   (honey/where query [:ilike
                       [:replace
                        [:concat
                         [:concat first-name-column " "]
                         [:concat middle-name-column " "]
                         [:concat last-name-column]]
                        "  "
                        " "]
                       [:concat "%" full-name-filter "%"]])))
