(ns crm.core-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [microservice.system :as system]))

(def test-system
  ^{:doc "Provides access to the current test system components"}
  (delay
    (-> (system/make-system)
        (component/start-system))))
