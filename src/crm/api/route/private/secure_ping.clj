(ns crm.api.route.private.secure-ping
  (:require [crm.api.schema.message :refer [MessageResponse]]
            [crm.api.controller.secure-ping :as controller]))

(def routes
  ["/secure-ping"
   {:get {:summary "Attempts secured ping against the application"
          :responses {200 {:body MessageResponse}}
          :handler (fn [_]
                     (controller/secure-ping))}}])
