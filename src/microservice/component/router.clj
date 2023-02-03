(ns microservice.component.router
  (:require [com.stuartsierra.component :as component]
            [microservice.component.middleware.auth :as auth]
            [microservice.component.middleware.cors :as cors]
            [microservice.component.middleware.exception :as exception]
            [muuntaja.core :as m]
            [reitit.coercion.schema :as reitit-schema]
            [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as coercion]
            [ring.logger :as logger]
            [taoensso.timbre :as timbre]
            [crm.api.route.health-check :as health-check]
            [crm.auth.middleware :as authentication]))

(defrecord Router [datasource swagger audit-logger]
  component/Lifecycle

  (start [this]
    (timbre/info "Starting Router component")
    (let [datasource (:datasource datasource)
          router (ring/router
                   [(:swagger-routes swagger)
                    health-check/routes

                    ["/api"
                     ["/public"]
                     ["/private"
                      {:middleware [(partial authentication/wrap-with-jwt-middleware datasource)
                                    auth/wrap-authentication-check]}]]]

                   {:data {:coercion   reitit-schema/coercion
                           :ctx        {:datasource datasource
                                        :audit-logger audit-logger}
                           :muuntaja   m/instance
                           :middleware [;; ring handler logger
                                        logger/wrap-with-logger
                                        ;; ring cors middleware
                                        cors/cors-middleware
                                        ;; query-params & form-params
                                        parameters/parameters-middleware
                                        ;; content-negotiation
                                        muuntaja/format-negotiate-middleware
                                        ;; encoding response body
                                        muuntaja/format-response-middleware
                                        ;; exception handling
                                        exception/exception-middleware
                                        ;; decoding request body
                                        muuntaja/format-request-middleware
                                        ;; coercing response body
                                        coercion/coerce-response-middleware
                                        ;; coercing request parameters
                                        coercion/coerce-request-middleware]}})]
      (timbre/info "Started Router component")
      (assoc this :router router)))

  (stop [this]
    (timbre/info "Stopping Router component")
    (timbre/info "Stopped Router component")
    (dissoc this :router)))

(defn new-router
  "Returns instance of Router component."
  [datasource-ref swagger-ref audit-logger-ref]
  (component/using
    (map->Router {})
    {:datasource                  datasource-ref
     :swagger                     swagger-ref
     :audit-logger                audit-logger-ref}))
