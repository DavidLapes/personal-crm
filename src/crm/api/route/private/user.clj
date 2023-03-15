(ns crm.api.route.private.user
  (:require [crm.api.controller.user :as controller]
            [crm.api.schema.message :refer [MessageResponse]]
            [crm.api.schema.user :refer [UserOutput UserListOutput UpdateUser]]
            [crm.lib.api.router :refer [wrap-with-context]]
            [schema.core :as s]))

(def routes
  ["/users"
   {:swagger {:tags ["Users"]}}

   [""
    {:get {:summary "Returns list of all users"
           :responses {200 {:body UserListOutput}}
           :handler (wrap-with-context
                       (fn [request]
                         (controller/get-all! request)))}}]
   ["/:id"
    {:get    {:summary   "Returns user by ID"
              :responses {200 {:body UserOutput}}
              :parameters {:path {:id s/Int}}
              :handler   (wrap-with-context
                           (fn [request]
                             (controller/get-by-id! request)))}
     :put    {:summary    "Updates user by ID"
              :responses  {200 {:body UserOutput}}
              :parameters {:body UpdateUser
                           :path {:id s/Int}}
              :handler    (wrap-with-context
                            (fn [request]
                              (controller/update! request)))}
     :delete {:summary    "Deletes user by ID"
              :responses  {200 {:body MessageResponse}}
              :parameters {:path {:id s/Int}}
              :handler    (wrap-with-context
                            (fn [request]
                              (controller/delete! request)))}}]])
