(ns crm.api.route.private.person
  (:require [crm.api.controller.person :as controller]
            [crm.api.schema.message :refer [MessageResponse]]
            [crm.api.schema.person :refer [CreatePerson PersonOutput PersonListOutput UpdatePerson]]
            [crm.lib.api.router :refer [wrap-with-context]]
            [schema.core :as s]))

(def routes
  ["/persons"
   {:swagger {:tags ["Persons"]}}

   [""
    {:post {:summary    "Creates a person"
            :responses  {201 {:body PersonOutput}}
            :parameters {:body CreatePerson}
            :handler    (wrap-with-context
                          (fn [request]
                            (controller/create! request)))}
     :get  {:summary   "Returns list of all persons"
            :responses {200 {:body PersonListOutput}}
            :handler   (wrap-with-context
                         (fn [request]
                           (controller/get-all! request)))}}]
   ["/:id"
    {:get    {:summary    "Returns person by ID"
              :responses  {200 {:body PersonOutput}}
              :parameters {:path {:id s/Int}}
              :handler    (wrap-with-context
                            (fn [request]
                              (controller/get-by-id! request)))}
     :put    {:summary    "Updates person by ID"
              :responses  {200 {:body PersonOutput}}
              :parameters {:body UpdatePerson
                           :path {:id s/Int}}
              :handler    (wrap-with-context
                            (fn [request]
                              (controller/update! request)))}
     :delete {:summary    "Deletes person by ID"
              :responses  {200 {:body MessageResponse}}
              :parameters {:path {:id s/Int}}
              :handler    (wrap-with-context
                            (fn [request]
                              (controller/delete! request)))}}]])