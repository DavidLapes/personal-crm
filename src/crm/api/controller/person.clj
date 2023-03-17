(ns crm.api.controller.person
  (:require [crm.service.person :as service]
            [ring.util.http-response :refer [created ok no-content]]))

(defn create!
  "Creates new person."
  [{:keys [ctx body-params]}]
  (let [person (service/create! (:datasource ctx) body-params)]
    (created "/api/private/persons" person)))

(defn update!
  "Updates an existing person."
  [{:keys [ctx parameters body-params]}]
  (let [id   (-> parameters :path :id)
        person (service/update! (:datasource ctx) id body-params)]
    (ok person)))

(defn get-by-id!
  "Returns person by given ID."
  [{:keys [ctx parameters]}]
  (let [id   (-> parameters :path :id)
        person (service/get-by-id! (:datasource ctx) id)]
    (ok person)))

(defn get-all!
  "Returns persons by given filters."
  [{:keys [ctx query-params]}]
  (let [guests (service/get-all! (:datasource ctx) query-params)]
    (ok guests)))

(defn delete!
  "Deletes person by given ID."
  [{:keys [ctx parameters]}]
  (let [id (-> parameters :path :id)]
    (service/delete! (:datasource ctx) id)
    (no-content)))
