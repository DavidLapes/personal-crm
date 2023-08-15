(ns crm.api.route.public.health-check
  (:require [crm.api.schema.message :refer [MessageResponse]]
            [crm.api.controller.health-check :as controller]))

(def routes
  ["/health-check"
   {:get {:summary "Checks the health of the application"
          :responses {200 {:body MessageResponse}}
          :handler (fn [_]
                     (controller/health-check))}}])
