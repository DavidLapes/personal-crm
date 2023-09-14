(ns crm.core-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [microservice.system :as system]))

(defn- make-test-system []
  "Creates new Sierra Component system map for testing purposes."
  {})

(def test-system
  ^{:doc "Provides access to the current test system components"}
  (atom
    (-> (system/make-system {:profile :test})
        (merge (make-test-system))
        (component/start-system))))
