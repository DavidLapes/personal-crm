(ns dev.user
    (:require [com.stuartsierra.component.repl :as repl]
      [microservice.system :refer [make-system]]
      [taoensso.timbre :as timbre])
    (:import (clojure.lang IDeref)))

(repl/set-init
  (fn [& args]
      (make-system)))

(def system
  ^{:doc "Provides access to the current system components"}
  (reify IDeref
         (deref [this]
                repl/system)))

(defn reset
      "Reset the system and reload code changes"
      []
      (timbre/info "Re-starting CRM application")
      (repl/reset)
      (timbre/info "Re-started CRM application")
      :reset)

(defn start
      "Start the system"
      []
      (timbre/info "Starting CRM application")
      (repl/start)
      (timbre/info "Started CRM application")
      :start)

(defn stop
      "Stop the system"
      []
      (timbre/info "Stopping CRM application")
      (repl/stop)
      (timbre/info "Stopped CRM application")
      :stop)
