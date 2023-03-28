(ns crm.model.user
  (:require [crm.lib.db.utils :as query]))

(def table-name :users)
(def table-name-view :v_users)

(defn create!
  "Creates new user."
  [connection user]
  (let [result (query/insert! connection table-name user)]
    (dissoc result :password)))

(defn update!
  "Updates users with given data by given filters."
  [connection data filters]
  (let [result (query/update! connection table-name data filters)]
    (dissoc result :password)))

(defn get-by-id!
  "Returns user by given ID."
  [connection id]
  (let [result (query/get-by-id! connection table-name-view id)]
    (dissoc result :password)))

(defn get-by-email!
  "Returns user by given email."
  [connection email]
  (let [result (query/get-one! connection table-name-view {:email email})]
    (dissoc result :password)))

(defn get-all!
  "Returns users by given filters."
  [connection filters]
  (let [result (query/get-all! connection table-name-view filters)]
    (update result :data (fn [data]
                           (reduce
                             (fn [coll val]
                               (conj coll (dissoc val :password)))
                             []
                             data)))))

(defn delete!
  "Deletes user by given ID."
  [connection id]
  (query/delete! connection table-name {:id id}))
