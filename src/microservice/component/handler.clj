(ns microservice.component.handler
  (:require [com.stuartsierra.component :as component]
            [reitit.ring :as ring]
            [taoensso.timbre :as timbre]))

(defrecord Handler [router swagger]
  component/Lifecycle

  (start [this]
    (timbre/info "Starting Ring Handler component")
    (let [router  (:router router)
          swagger (:swagger swagger)
          handler (ring/ring-handler router swagger)]
      (timbre/info "Started Ring Handler component")
      (assoc this :handler handler)))

  (stop [this]
    (timbre/info "Stopping Ring Handler component")
    (timbre/info "Stopped Ring Handler component")
    (dissoc this :handler)))

(defn new-handler
  "Returns instance of DataSource component."
  [router-ref swagger-ref]
  (component/using
    (map->Handler {})
    {:router  router-ref
     :swagger swagger-ref}))
