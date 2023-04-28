(ns crm.model.person
  (:require [crm.lib.db.filters :as filters]
            [crm.lib.db.utils :as query]))

(def table-name :persons)
(def table-name-view :v_persons)

(defn- apply-filters
  "Returns HoneySQL where-clauses by provided filters."
  [{:keys [id email name]}]
  (cond-> nil
          (some? id)
          (filters/add-in-filter :id id)

          (some? email)
          (filters/add-ilike-filter :email email)

          (some? name)
          (filters/add-full-name-filter name)))

(defn create!
  "Creates new person."
  [connection person]
  (query/insert! connection table-name person))

(defn update!
  "Updates persons with given data by given filters."
  [connection data filters]
  (let [where-clauses (apply-filters filters)]
    (query/update! connection table-name data where-clauses)))

(defn get-by-id!
  "Returns person by given ID."
  [connection id]
  (query/get-by-id! connection table-name-view id))

(defn get-all!
  "Returns persons by given filters."
  [connection filters]
  (let [where-clauses (apply-filters filters)]
    (query/get-all! connection table-name-view where-clauses)))

(defn delete!
  "Deletes person by given ID."
  [connection id]
  (let [where-clauses (apply-filters {:id id})]
    (query/delete! connection table-name where-clauses)))
