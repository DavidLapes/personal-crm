(ns crm.api.route.public.health-check
  (:require [crm.api.controller.health-check :as controller]
            [crm.api.schema.message :refer [MessageResponse]]
            [crm.lib.api.router :refer [wrap-with-context]]))

(def routes
  ["/health-check"
   {:get {:summary "Checks the health of the application"
          :responses {200 {:body MessageResponse}}
          :handler (wrap-with-context
                     (fn [_]
                       (controller/health-check)))}}])
