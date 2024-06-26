(ns crm.api.route.private.person
  (:require [crm.api.controller.person :as controller]
            [crm.api.schema.filter :refer [person-filters]]
            [crm.api.schema.message :refer [MessageResponse]]
            [crm.api.schema.person :refer [CreatePerson PersonOutput PersonListOutput UpdatePerson]]
            [schema.core :as s]))

(def routes
  ["/persons"
   {:swagger {:tags ["Persons"]}}

   [""
    {:post {:summary    "Creates a person"
            :responses  {201 {:body PersonOutput}}
            :parameters {:body CreatePerson}
            :handler    (fn [request]
                          (controller/create! request))}
     :get  {:summary   "Returns list of all persons"
            :responses {200 {:body PersonListOutput}}
            :parameters {:query person-filters}
            :handler   (fn [request]
                         (controller/get-all! request))}}]
   ["/:id"
    {:get    {:summary    "Returns person by ID"
              :responses  {200 {:body PersonOutput}}
              :parameters {:path {:id s/Int}}
              :handler    (fn [request]
                            (controller/get-by-id! request))}
     :put    {:summary    "Updates person by ID"
              :responses  {200 {:body PersonOutput}}
              :parameters {:body UpdatePerson
                           :path {:id s/Int}}
              :handler    (fn [request]
                            (controller/update! request))}
     :delete {:summary    "Deletes person by ID"
              :responses  {200 {:body MessageResponse}}
              :parameters {:path {:id s/Int}}
              :handler    (fn [request]
                            (controller/delete! request))}}]])