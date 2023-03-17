(ns crm.api.route.private.user
  (:require [crm.api.controller.user :as controller]
            [crm.api.schema.message :refer [MessageResponse]]
            [crm.api.schema.user :refer [CreateUser UserOutput UserListOutput UpdateUser]]
            [schema.core :as s]))

(def routes
  ["/users"
   {:swagger {:tags ["Users"]}}

   [""
    {:post {:summary    "Creates a user"
            :responses  {201 {:body UserOutput}}
            :parameters {:body CreateUser}
            :handler    (fn [request]
                          (controller/create! request))}

     :get  {:summary   "Returns list of all users"
            :responses {200 {:body UserListOutput}}
            :handler   (fn [request]
                         (controller/get-all! request))}}]
   ["/:id"
    {:get    {:summary    "Returns user by ID"
              :responses  {200 {:body UserOutput}}
              :parameters {:path {:id s/Int}}
              :handler    (fn [request]
                            (controller/get-by-id! request))}

     :put    {:summary    "Updates user by ID"
              :responses  {200 {:body UserOutput}}
              :parameters {:body UpdateUser
                           :path {:id s/Int}}
              :handler    (fn [request]
                            (controller/update! request))}

     :delete {:summary    "Deletes user by ID"
              :responses  {200 {:body MessageResponse}}
              :parameters {:path {:id s/Int}}
              :handler    (fn [request]
                            (controller/delete! request))}}]])
