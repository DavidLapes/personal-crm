(ns crm.core-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [microservice.system :as system]))

(defn- make-test-system []
  {})

(def test-system
  ^{:doc "Provides access to the current test system components"}
  (delay
    (-> (system/make-system)
        (merge (make-test-system))
        (component/start-system))))
