(ns crm.core-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [microservices.component.test.db_initializer :refer [new-db-initializer]]
            [microservice.system :as system]))

(defn- make-test-system []
  "Creates new Sierra Component system map for testing purposes."
  (component/system-map
    :crm.component/db-initializer (new-db-initializer :crm.component/datasource
                                                      :crm.component/migration)))

(def test-system
  ^{:doc "Provides access to the current test system components"}
  (atom
    (-> (system/make-system {:profile :test})
        (merge (make-test-system))
        (component/start-system))))
