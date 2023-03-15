(ns crm.service.user
  (:require [clojure.java.jdbc :as jdbc]
            [crm.lib.security.crypto :as crypto]
            [crm.model.user :as model]))

(defn- encrypt-password
  "Encrypts a password if it is present in given map, otherwise return unchanged."
  [data]
  (let [password (:password data)]
    (if (some? password)
      (assoc data :password (crypto/encrypt password))
      data)))

(defn create!
  "Creates new user."
  [datasource data]
  (jdbc/with-db-transaction [connection {:datasource datasource}]
    (model/create! connection (encrypt-password data))))

(defn update!
  "Updates an existing user."
  [datasource id data]
  (jdbc/with-db-transaction [connection {:datasource datasource}]
    (model/update! connection id (encrypt-password data))))

(defn get-by-id!
  "Returns user by given ID."
  [datasource id]
  (jdbc/with-db-connection [connection {:datasource datasource}]
    (model/get-by-id! connection id)))

(defn get-by-email!
  "Returns user by email."
  [datasource email]
  (jdbc/with-db-connection [connection {:datasource datasource}]
    (model/get-by-email! connection email)))

(defn get-all!
  "Returns users by given filters."
  [datasource filters]
  (jdbc/with-db-connection [connection {:datasource datasource}]
    (model/get-all! connection filters)))

(defn delete!
  "Deletes user by given ID."
  [datasource id]
  (jdbc/with-db-transaction [connection {:datasource datasource}]
    (model/delete! connection id)))

(defn active?
  "Checks if given user is active."
  [user]
  (boolean (:is_active user)))

(defn deleted?
  "Checks if given user is deleted."
  [user]
  (boolean (:is_deleted user)))
