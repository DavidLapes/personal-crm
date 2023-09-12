(ns microservice.system
  (:require [com.stuartsierra.component :as component]
            [microservice.component.datasource :as datasource]
            [microservice.component.handler :as handler]
            [microservice.component.migration :as migration]
            [microservice.component.param :as params]
            [microservice.component.router :as router]
            [microservice.component.swagger :as swagger]
            [microservice.component.web-server :as web-server]
            [microservice.logging :as logging]
            [microservice.component.proto.impl.date]
            [microservice.component.proto.impl.encoding]
            [microservice.component.proto.impl.pg_json]
            [taoensso.timbre :as timbre]))

(defn make-system
  "Creates new Sierra Component system map."
  [{:keys [profile]}]
  (logging/init-logging)
  (params/init-params profile)
  (component/system-map
    :crm.component/datasource (datasource/new-datasource)
    :crm.component/handler (handler/new-handler :crm.component/router
                                                :crm.component/swagger)
    :crm.component/migration (migration/new-migration :crm.component/datasource)
    :crm.component/router (router/new-router :crm.component/datasource
                                             :crm.component/swagger)
    :crm.component/swagger (swagger/new-swagger-handler)
    :crm.component/web-server (web-server/new-webserver :crm.component/handler)))

(Thread/setDefaultUncaughtExceptionHandler
  (reify Thread$UncaughtExceptionHandler
    (uncaughtException [_ thread ex]
      (timbre/error ex "Uncaught exception on" (.getName thread)))))
