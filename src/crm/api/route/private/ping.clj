(ns crm.api.route.private.ping
  (:require [crm.api.schema.message :refer [MessageResponse]]
            [crm.api.controller.ping :as controller]))

(def routes
  ["/ping"
   {:get {:summary "Attempts secured ping against the application"
          :responses {200 {:body MessageResponse}}
          :handler (fn [_]
                     (controller/ping))}}])
