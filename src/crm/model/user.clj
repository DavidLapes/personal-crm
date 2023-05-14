(ns crm.model.user
  (:require [crm.lib.db.filters :as filters]
            [crm.lib.db.utils :as query]
            [honey.sql.helpers :as honey]))

(def table-name :users)
(def table-name-view :v_users)

(defn- apply-filters
  "Returns HoneySQL where-clauses by provided filters."
  ([filters]
   (apply-filters nil filters))
  ([query {:keys [id email name person_id is_active is_deleted
                  birthdate_from birthdate_to time_created_from time_created_to]}]
   (cond-> query
           (some? id)
           (filters/add-in-filter :id id)

           (some? email)
           (filters/add-ilike-filter :email email)

           (some? name)
           (filters/add-full-name-filter name)

           (some? person_id)
           (filters/add-in-filter :person_id person_id)

           (some? is_active)
           (filters/add-is-filter :is_active is_active)

           (some? is_deleted)
           (filters/add-is-filter :is_deleted is_deleted)

           (some? birthdate_from)
           (filters/add-higher-than-time-filter :birthdate birthdate_from)

           (some? birthdate_to)
           (filters/add-lower-than-time-filter :birthdate birthdate_to)

           (some? time_created_from)
           (filters/add-higher-than-time-filter :time_created time_created_from)

           (some? time_created_to)
           (filters/add-lower-than-time-filter :time_created time_created_to))))

(defn create!
  "Creates new user."
  [connection user]
  (let [result (query/insert! connection table-name user)]
    (dissoc result :password)))

(defn update!
  "Updates users with given data by given filters."
  [connection data filters]
  (let [where-clauses (apply-filters filters)
        result (query/update! connection table-name data where-clauses)]
    (dissoc result :password)))

(defn get-by-id!
  "Returns user by given ID."
  [connection id]
  (let [result (query/get-by-id! connection table-name-view id)]
    (dissoc result :password)))

(defn get-by-email!
  "Returns user by given email."
  [connection email]
  (let [where-clauses (apply-filters {:email email})
        result (query/get-one! connection table-name-view where-clauses)]
    (dissoc result :password)))

(defn get-all!
  "Returns users by given filters."
  [connection filters]
  (let [where-clauses (apply-filters filters)
        result (query/get-all! connection table-name-view where-clauses)]
    (update result :data (fn [data]
                           (reduce
                             (fn [coll val]
                               (conj coll (dissoc val :password)))
                             []
                             data)))))
(defn get-by-credentials!
  "Returns user by given admin and password credentials."
  [connection email password]
  (let [where-clauses (-> (honey/where [:= :email email])
                          (honey/where [:= :password password]))
        result (query/get-one! connection table-name where-clauses)]
    (dissoc result :password)))

(defn delete!
  "Deletes user by given ID."
  [connection id]
  (let [where-clauses (apply-filters {:id id})]
    (query/delete! connection table-name where-clauses)))
