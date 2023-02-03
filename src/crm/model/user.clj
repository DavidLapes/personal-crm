(ns crm.model.user
  (:require [crm.lib.db.utils :as query :refer [query!]]
            [honeysql.helpers :refer [select from where]]))

(def table-name :users)

(defn create!
  "Creates new user."
  [connection user]
  (let [result (query/insert! connection table-name user)]
    (dissoc result :password)))

(defn update!
  "Updates a user."
  [connection id user]
  (let [result (query/update-by-id! connection table-name id user)]
    (dissoc result :password)))

(defn get-by-id!
  "Returns user by given id."
  [connection id]
  (let [result (query/get-by-id! connection table-name id)]
    (dissoc result :password)))

(defn get-login-detail-by-email!
  "Returns user by given email and password."
  [connection email]
  (let [result (query! connection (-> (select :*)
                                      (from :users)
                                      (where [:= :email email])))]
    (first result)))

(defn get-by-email!
  "Returns user by given email."
  [connection email]
  (let [result (query! connection (-> (select :*)
                                      (from :users)
                                      (where [:= :email email])))]
    (dissoc (first result) :password)))
