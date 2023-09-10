(ns microservice.component.router
  (:require [com.stuartsierra.component :as component]
            [microservice.component.middleware.auth :as auth]
            [microservice.component.middleware.cors :as cors]
            [microservice.component.middleware.ctx :as ctx]
            [microservice.component.middleware.exception :as exception]
            [microservice.component.middleware.filter :as filters]
            [microservice.component.middleware.param :as params]
            [muuntaja.core :as m]
            [reitit.coercion.schema :as reitit-schema]
            [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :as ring-parameters]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.coercion :as coercion]
            [ring.logger :as logger]
            [taoensso.timbre :as timbre]
            [crm.auth.middleware :as authentication]
            [crm.api.route.private.person :as person-private]
            [crm.api.route.private.ping :as ping-private]
            [crm.api.route.private.user :as user-private]
            [crm.api.route.public.auth :as auth-public]
            [crm.api.route.public.health-check :as health-check-public]
            [crm.api.route.public.ping :as ping-public]))

(defrecord Router [datasource swagger]
  component/Lifecycle

  (start [this]
    (timbre/info "Starting Router component")
    (let [ctx {:datasource (:datasource datasource)}
          router (ring/router
                   [(:swagger-routes swagger)
                    ["/api"
                     ["/public"
                      auth-public/routes
                      health-check-public/routes
                      ping-public/routes]
                     ["/private"
                      {:middleware [(partial authentication/wrap-with-jwt-middleware ctx)
                                    auth/wrap-authentication-check]}
                      user-private/routes
                      person-private/routes
                      ping-private/routes]]]

                   {:data {:coercion   reitit-schema/coercion
                           :muuntaja   m/instance
                           :middleware [logger/wrap-with-logger
                                        ;; injection of ctx
                                        (partial ctx/wrap-with-ctx-middleware ctx)
                                        ;; ring cors middleware
                                        cors/cors-middleware
                                        ;; query-params & form-params
                                        ring-parameters/parameters-middleware
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
                                        coercion/coerce-request-middleware
                                        ;; format query params
                                        params/format-params-middleware
                                        ;; extract filters
                                        filters/extract-filters-middleware]}})]
      (timbre/info "Started Router component")
      (assoc this :router router)))

  (stop [this]
    (timbre/info "Stopping Router component")
    (timbre/info "Stopped Router component")
    (dissoc this :router)))

(defn new-router
  "Returns instance of Router component."
  [datasource-ref swagger-ref]
  (component/using
    (map->Router {})
    {:datasource                  datasource-ref
     :swagger                     swagger-ref}))
