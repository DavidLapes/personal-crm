(ns microservice.component.swagger
  (:require [com.stuartsierra.component :as component]
            [reitit.ring :as ring]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [taoensso.timbre :as timbre]))

(defn- swagger-routes []
  ["/swagger.json"
   {:get {:no-doc  true
          :swagger {:info     {:title "Personal CRM API"}
                    :basePath "/"}
          :handler (swagger/create-swagger-handler)}}])

(defrecord Swagger []
  component/Lifecycle

  (start [this]
    (timbre/info "Starting Swagger component")
    (let [swagger (ring/routes
                    (swagger-ui/create-swagger-ui-handler {:path "/docs"})
                    (ring/create-default-handler))]
      (timbre/info "Started Swagger component")
      (assoc this :swagger swagger
                  :swagger-routes (swagger-routes))))

  (stop [this]
    (timbre/info "Stopping Swagger component")
    (timbre/info "Stopped Swagger component")
    (dissoc this :swagger)))

(defn new-swagger-handler
  "Returns instance of Swagger component."
  []
  (map->Swagger {}))
