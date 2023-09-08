(ns dev.repl.system
  (:require [dev.user :refer [system]]))

(defn get-system-component []
  @system)
