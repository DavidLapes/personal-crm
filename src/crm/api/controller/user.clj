(ns crm.api.controller.user
  (:require [crm.service.user :as service]
            [ring.util.http-response :refer [created ok no-content]]))

(defn create!
  "Creates new user."
  [{:keys [ctx body-params]}]
  (let [user (service/create! (:datasource ctx) body-params)]
    (created "/api/private/users" user)))

(defn update!
  "Updates an existing user."
  [{:keys [ctx parameters body-params]}]
  (let [id   (-> parameters :path :id)
        user (service/update! (:datasource ctx) id body-params)]
    (ok user)))

(defn get-by-id!
  "Returns user by given ID."
  [{:keys [ctx parameters]}]
  (let [id   (-> parameters :path :id)
        user (service/get-by-id! (:datasource ctx) id)]
    (ok user)))

(defn get-all!
  "Returns users by given filters."
  [{:keys [ctx query-params]}]
  (let [guests (service/get-all! (:datasource ctx) query-params)]
    (ok guests)))

(defn delete!
  "Deletes user by given ID."
  [{:keys [ctx parameters]}]
  (let [id (-> parameters :path :id)]
    (service/delete! (:datasource ctx) id)
    (no-content)))
