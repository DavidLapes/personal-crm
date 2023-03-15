(ns crm.api.route.public.health-check
  (:require [crm.api.schema.message :refer [MessageResponse]]
            [crm.lib.api.http-response :refer [response-message]]
            [crm.lib.api.router :refer [wrap-with-context]]
            [ring.util.http-response :refer [ok]]))

(def routes
  ["/health-check"
   {:get {:summary "Checks the health of the application"
          :responses {200 {:body MessageResponse}}
          :handler (wrap-with-context
                     (fn [_]
                       (ok (response-message "The API is healthy"))))}}])
