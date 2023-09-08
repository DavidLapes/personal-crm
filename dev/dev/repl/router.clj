(ns dev.repl.router
  (:require [dev.repl.system :refer [get-system-component]]))

(defn router []
  (-> (get-system-component)
      :crm.component/router
      :router))

(defn handler []
  (-> (get-system-component)
      :crm.component/handler
      :handler))

(defn call-health-check []
  (let [handler (handler)]
    (handler {:request-method :get
              :uri "/api/public/health-check"})))
