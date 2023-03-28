(ns microservice.component.web-server
  (:require [com.stuartsierra.component :as component]
            [microservice.component.param :as params]
            [org.httpkit.server :as server]
            [taoensso.timbre :as timbre]))

(defrecord WebServer [handler port thread-count]
  component/Lifecycle

  (start [this]
    (timbre/info "Starting WebServer component")
    (let [handler (:handler handler)
          server (server/run-server handler {:port   port
                                             :thread thread-count})]
      (timbre/info "Started WebServer component")
      (timbre/info (str "SERVER PORT - { " port " }"))
      (assoc this :web-server server)))

  (stop [this]
    (timbre/info "Stopping WebServer component")
    (let [server (:web-server this)]
      (server)
      (timbre/info "Stopped WebServer component")
      (dissoc this :web-server))))

(defn new-webserver
  "Returns instance of WebServer component."
  [handler-ref]
  (let [port (params/get-param :crm-web-server-port)
        thread-count (params/get-param :crm-web-server-thread-count)]
    (component/using
      (map->WebServer {:port (if (number? port)
                               port
                               (Integer/parseInt port))
                       :thread-count (if (number? thread-count)
                                       thread-count
                                       (Integer/parseInt thread-count))})
      {:handler handler-ref})))
