(ns crm.api.route.public.ping
  (:require [crm.api.schema.message :refer [MessageResponse]]
            [crm.api.controller.ping :as controller]))

(def routes
  ["/ping"
   {:get {:summary "Attempts ping against the application"
          :responses {200 {:body MessageResponse}}
          :handler (fn [_]
                     (controller/ping))}}])
