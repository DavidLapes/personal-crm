(ns crm.service.user
  (:require [clojure.java.jdbc :as jdbc]
            [crm.model.user :as model]))

(defn create!
  "Creates new user."
  [datasource data]
  (jdbc/with-db-transaction [connection {:datasource datasource}]
    (model/create! connection data)))

(defn update!
  "Updates an existing user."
  [datasource data]
  (jdbc/with-db-transaction [connection {:datasource datasource}]
    (model/update! connection id data)))

(defn get-by-id!
  "Returns user by given id."
  [datasource id]
  (jdbc/with-db-connection [connection {:datasource datasource}]
    (model/get-by-id! connection id)))

(defn get-by-email!
  "Returns user by email."
  [datasource email]
  (jdbc/with-db-connection [connection {:datasource datasource}]
    (model/get-by-email! connection email)))

(defn active?
  "Checks if given user is active."
  [user]
  (boolean (:is_active user)))

(defn deleted?
  "Checks if given user is deleted."
  [user]
  (boolean (:is_deleted user)))
