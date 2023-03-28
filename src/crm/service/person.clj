(ns crm.service.person
  (:require [clojure.java.jdbc :as jdbc]
            [crm.model.person :as model]))

(defn create!
  "Creates new person."
  [datasource data]
  (jdbc/with-db-transaction [connection {:datasource datasource}]
    (let [new-person (model/create! connection data)]
      (model/get-by-id! connection (:id new-person)))))

(defn update!
  "Updates an existing person."
  [datasource id data]
  (jdbc/with-db-transaction [connection {:datasource datasource}]
    (let [_ (model/update! connection data {:id id})]
      (model/get-by-id! connection id))))

(defn get-by-id!
  "Returns person by given ID."
  [datasource id]
  (jdbc/with-db-connection [connection {:datasource datasource}]
    (model/get-by-id! connection id)))

(defn get-all!
  "Returns persons by given filters."
  [datasource filters]
  (jdbc/with-db-connection [connection {:datasource datasource}]
    (model/get-all! connection filters)))

(defn delete!
  "Deletes person by given ID."
  [datasource id]
  (jdbc/with-db-transaction [connection {:datasource datasource}]
    (model/delete! connection id)))
