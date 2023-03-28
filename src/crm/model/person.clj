(ns crm.model.person
  (:require [crm.lib.db.utils :as query]))

(def table-name :persons)
(def table-name-view :v_persons)

(defn create!
  "Creates new person."
  [connection person]
  (query/insert! connection table-name person))

(defn update!
  "Updates persons with given data by given filters."
  [connection data filters]
  (query/update! connection table-name data filters))

(defn get-by-id!
  "Returns person by given ID."
  [connection id]
  (query/get-by-id! connection table-name-view id))

(defn get-all!
  "Returns persons by given filters."
  [connection filters]
  (query/get-all! connection table-name-view filters))

(defn delete!
  "Deletes person by given ID."
  [connection id]
  (query/delete! connection table-name {:id id}))
